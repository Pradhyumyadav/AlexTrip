package Servlet;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private static volatile boolean isFirebaseInitialized = false;

    @Override
    public void init() throws ServletException {
        synchronized (LoginServlet.class) {
            if (!isFirebaseInitialized) {
                try {
                    // Construct path to Firebase credentials file within the project
                    String filePath = getServletContext().getRealPath("/WEB-INF/classes/alextrip-fb-firebase-adminsdk-ir6ji-d2a1ed992c.json");
                    InputStream serviceAccount = new FileInputStream(filePath);

                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();

                    FirebaseApp.initializeApp(options);
                    isFirebaseInitialized = true;
                    logger.info("Firebase has been initialized successfully.");
                } catch (IOException e) {
                    logger.error("Error initializing Firebase: ", e);
                    throw new ServletException("Failed to initialize Firebase", e);
                }
            }
        }
    }
}