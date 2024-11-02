package Servlet;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.auth.oauth2.GoogleCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private static volatile boolean isFirebaseInitialized = false;

    @Override
    public void init() throws ServletException {
        synchronized (LoginServlet.class) {
            if (!isFirebaseInitialized) {
                try {
                    FileInputStream serviceAccount = new FileInputStream(
                            getServletContext().getRealPath("/WEB-INF/classes/alextrip-fb-firebase-adminsdk-ir6ji-d2a1ed992c.json")
                    );

                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();

                    FirebaseApp.initializeApp(options);
                    isFirebaseInitialized = true;
                    logger.info("Firebase Admin SDK initialized successfully.");
                } catch (Exception e) {
                    logger.error("Failed to initialize Firebase Admin SDK", e);
                    throw new ServletException("Initialization of Firebase Admin SDK failed", e);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"error\": \"No token provided\"}");
            return;
        }

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();

            HttpSession session = request.getSession(true);
            session.setAttribute("uid", uid);

            response.getWriter().write("{\"success\": true, \"uid\": \"" + uid + "\"}");
            logger.info("Token verified successfully for UID: {}", uid);
        } catch (FirebaseAuthException e) {
            logger.error("Token verification failed for token: {}", token, e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"error\": \"Token verification failed: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            logger.error("Unexpected error during login", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"error\": \"Internal server error\"}");
        }
    }
}