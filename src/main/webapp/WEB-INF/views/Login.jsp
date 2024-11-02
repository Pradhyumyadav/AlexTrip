<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - AlexTrip</title>

    <!-- Firebase App (the core Firebase SDK) -->
    <script src="https://www.gstatic.com/firebasejs/9.0.0/firebase-app.js"></script>
    <!-- Firebase Auth -->
    <script src="https://www.gstatic.com/firebasejs/9.0.0/firebase-auth.js"></script>
</head>
<body>
<h2>Login to AlexTrip</h2>

<form id="loginForm">
    <label for="email">Email:</label><br>
    <input type="email" id="email" name="email" required><br>
    <label for="password">Password:</label><br>
    <input type="password" id="password" name="password" required><br><br>
    <button type="submit">Login</button>
</form>

<div id="message"></div>

<script>
    // Firebase configuration
    const firebaseConfig = {
        apiKey: "AIzaSyB47ekyUah0S7OjySd9kP8PQ7mvqX9XC5A",
        authDomain: "alextrip-fb.firebaseapp.com",
        projectId: "alextrip-fb",
        storageBucket: "alextrip-fb.appspot.com",
        messagingSenderId: "87747207647",
        appId: "1:87747207647:web:389732bc154459b7af646a",
        measurementId: "G-XSFWLK8Q69"
    };

    // Initialize Firebase
    firebase.initializeApp(firebaseConfig);

    // Handle form submission
    const loginForm = document.getElementById('loginForm');
    loginForm.addEventListener('submit', (e) => {
        e.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // Firebase authentication
        firebase.auth().signInWithEmailAndPassword(email, password)
            .then((userCredential) => {
                userCredential.user.getIdToken().then((token) => {
                    console.log('Firebase token:', token);

                    // Send the token to the backend (LoginServlet)
                    fetch('/AlexTrip/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ token: token })
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                document.getElementById('message').innerHTML = "Login successful!";
                                window.location.href = '/AlexTrip/home'; // Redirect to home
                            } else {
                                document.getElementById('message').innerHTML = "Login failed: " + data.error;
                            }
                        })
                        .catch(error => {
                            document.getElementById('message').innerHTML = "Error: " + error.message;
                        });
                });
            })
            .catch((error) => {
                document.getElementById('message').innerHTML = "Authentication failed: " + error.message;
            });
    });
</script>
</body>
</html>