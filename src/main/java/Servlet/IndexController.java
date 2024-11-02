package Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/index")
public class IndexController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the user's location, using a default if none is provided
        String userLocation = request.getParameter("userLocation");
        if (userLocation == null || userLocation.trim().isEmpty()) {
            userLocation = "Default Location";
        }

        // Generate the list of popular destinations around the user's location
        List<Destination> popularDestinations = getPopularDestinationsAround(userLocation);

        // Ensure that the popularDestinations list is not null
        if (popularDestinations == null) {
            popularDestinations = new ArrayList<>(); // Safeguard against null list
        }

        // Setting attributes in request
        request.setAttribute("popularDestinations", popularDestinations);
        request.setAttribute("userLocation", userLocation);

        // Forward request to index.jsp
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    /**
     * This method simulates fetching popular destinations based on the user's location.
     *
     * @param location User's location to customize the destinations shown.
     * @return A list of Destination objects around the specified location.
     */
    private List<Destination> getPopularDestinationsAround(String location) {
        List<Destination> destinations = new ArrayList<>();

        // Populate destinations based on the user's location
        switch (location.toLowerCase()) {
            case "paris":
                destinations.add(new Destination("Versailles", "Famous for its palace", "/images/versailles.jpg"));
                destinations.add(new Destination("Lyon", "A city known for its cuisine", "/images/lyon.jpg"));
                break;
            case "new york":
                destinations.add(new Destination("Brooklyn", "Known for the Brooklyn Bridge", "/images/brooklyn.jpg"));
                destinations.add(new Destination("Boston", "Historical city with great universities", "/images/boston.jpg"));
                break;
            default:
                destinations.add(new Destination("Paris", "The city of light", "/images/paris.jpg"));
                destinations.add(new Destination("New York", "The city that never sleeps", "/images/nyc.jpg"));
                destinations.add(new Destination("Tokyo", "Land of the rising sun", "/images/tokyo.jpg"));
                break;
        }

        // Return the populated list of destinations
        return destinations;
    }

    /**
     * Inner static class representing a destination.
     */
    public static class Destination {
        private final String name;
        private final String description;
        private final String imageUrl;

        /**
         * Constructor with null safety checks for each field.
         */
        public Destination(String name, String description, String imageUrl) {
            this.name = name != null && !name.isEmpty() ? name : "Unknown Destination";
            this.description = description != null && !description.isEmpty() ? description : "No description available.";
            this.imageUrl = imageUrl != null && !imageUrl.isEmpty() ? imageUrl : "/images/placeholder.jpg";
        }

        // Getters for JSP access
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getImageUrl() { return imageUrl; }
    }
}