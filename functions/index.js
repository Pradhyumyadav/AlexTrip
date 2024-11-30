const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { Pool } = require('pg');

admin.initializeApp({
    credential: admin.credential.applicationDefault()
});

// PostgreSQL connection pool configuration
const pool = new Pool({
    connectionString: 'postgresql://postgres:123@localhost:5432/alextrip',
    ssl: {
        rejectUnauthorized: false // Adjust based on your SSL configuration requirements
    }
});

// Function to save users to PostgreSQL database
const saveUsersToDatabase = async (users) => {
    const client = await pool.connect(); // Properly acquire a client
    try {
        await client.query('BEGIN'); // Start a transaction
        for (const user of users) {
            const queryText = 'INSERT INTO users (firebase_uid, email, username, password) VALUES ($1, $2, $3, $4) ON CONFLICT (firebase_uid) DO NOTHING;';
            const values = [user.uid, user.email, user.displayName || '', user.passwordHash || ''];
            await client.query(queryText, values); // Execute a query
        }
        await client.query('COMMIT'); // Commit the transaction
    } catch (e) {
        await client.query('ROLLBACK'); // Rollback in case of an error
        throw e; // Re-throw the error after rollback
    } finally {
        client.release(); // Always release the client back to the pool
    }
};

// Cloud Function to sync users from Firebase to PostgresSQL
exports.syncUsers = functions.https.onRequest(async (request, response) => {
    try {
        const userRecords = await admin.auth().listUsers(); // List users from Firebase
        const users = userRecords.users.map(user => ({
            uid: user.uid,
            email: user.email,
            displayName: user.displayName,
            passwordHash: user.passwordHash // Ensure you manage hashed passwords securely
        }));

        await saveUsersToDatabase(users); // Save users to the database
        response.send("Users fetched and saved successfully.");
    } catch (error) {
        console.error("Failed to fetch or save users:", error);
        response.status(500).send("Failed to sync users.");
    }
});