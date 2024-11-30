package Servlet.HostPanel;

import Model.Offer;
import Model.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.OfferService;
import service.TripService;
import utils.DatabaseConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HostPanelController", urlPatterns = {"/HostPanel"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class HostPanelController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(HostPanelController.class);
    private static final String IMAGE_UPLOAD_DIR = "/uploaded_photos";

    private TripService tripService;
    private OfferService offerService;

    @Override
    public void init() throws ServletException {
        try {
            // Initializing TripService and OfferService using DatabaseConnectionManager
            tripService = new TripService(DatabaseConnectionManager.getDataSource());
            offerService = new OfferService();
            logger.info("HostPanelController initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to initialize HostPanelController", e);
            throw new ServletException("Initialization failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("hostId") == null) {
            logger.warn("Invalid session or user not authenticated. Redirecting to login.");
            response.sendRedirect("/HostLogin");
            return;
        }

        int hostId = (Integer) session.getAttribute("hostId");
        try {
            List<Trip> trips = tripService.getAllTripsForHost(hostId);
            List<Offer> offers = offerService.getAllOffersForHost(hostId);
            request.setAttribute("trips", trips);
            request.setAttribute("offers", offers);
            logger.info("Trips and offers successfully loaded for host ID: {}", hostId);
            request.getRequestDispatcher("/WEB-INF/views/HostPanel/HostPanel.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error fetching trips or offers for host.", e);
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("hostId") == null) {
            response.sendRedirect("/HostLogin.jsp");
            return;
        }

        int hostId = (Integer) session.getAttribute("hostId");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "addTrip":
                    handleAddOrUpdateTrip(request, null, hostId);
                    break;
                case "updateTrip":
                    Integer tripId = parseIntegerParameter(request, "trip_id");
                    handleAddOrUpdateTrip(request, tripId, hostId);
                    break;
                case "deleteTrip":
                    int deleteTripId = parseIntegerParameter(request, "trip_id");
                    tripService.deleteTrip(deleteTripId, hostId);
                    break;
                case "addOffer":
                    handleAddOrUpdateOffer(request, null, hostId);
                    break;
                case "updateOffer":
                    Integer offerId = parseIntegerParameter(request, "offer_id");
                    handleAddOrUpdateOffer(request, offerId, hostId);
                    break;
                case "deleteOffer":
                    int deleteOfferId = parseIntegerParameter(request, "offer_id");
                    offerService.deleteOffer(deleteOfferId, hostId);
                    break;
                default:
                    logger.warn("Unknown action: {}", action);
                    response.sendRedirect("HostPanel");
                    return;
            }
            response.sendRedirect("HostPanel");
        } catch (Exception e) {
            logger.error("Error handling action: {}", action, e);
            response.sendRedirect("/WEB-INF/views/error.jsp");
        }
    }

    private void handleAddOrUpdateTrip(HttpServletRequest request, Integer tripId, int hostId) throws Exception {
        Trip trip = new Trip();
        trip.setTripId(tripId != null ? tripId : 0);
        trip.setHostId(hostId);
        trip.setTripName(request.getParameter("trip_name"));
        trip.setDestination(request.getParameter("destination"));
        trip.setDuration(parseIntegerParameter(request, "duration"));
        trip.setPrice(new BigDecimal(request.getParameter("price")));
        trip.setMaxParticipants(parseIntegerParameter(request, "max_participants"));
        trip.setActivityType(request.getParameter("activity_type"));
        trip.setDescription(request.getParameter("description"));
        trip.setStartDate(parseLocalDate(request.getParameter("start_date")));
        trip.setEndDate(parseLocalDate(request.getParameter("end_date")));
        trip.setCancellationPolicy(request.getParameter("cancellation_policy"));
        trip.setItinerary(request.getParameter("itinerary"));
        trip.setInclusions(request.getParameter("inclusions"));
        trip.setExclusions(request.getParameter("exclusions"));
        trip.setDifficultyLevel(request.getParameter("difficulty_level"));
        trip.setPackingList(request.getParameter("packing_list"));
        trip.setBookingDeadline(parseLocalDate(request.getParameter("booking_deadline")));
        trip.setPaymentTerms(request.getParameter("payment_terms"));
        trip.setHostName(request.getParameter("host_name"));
        trip.setHostContactEmail(request.getParameter("host_contact_email"));
        trip.setHostContactPhone(request.getParameter("host_contact_phone"));
        trip.setPhotoPaths(processUploadedPhotos(request, hostId));
        tripService.saveTrip(trip);
    }

    private void handleAddOrUpdateOffer(HttpServletRequest request, Integer offerId, int hostId) throws Exception {
        Offer offer = new Offer();
        offer.setId(offerId != null ? offerId : 0);
        offer.setHostId(hostId);
        offer.setDetails(request.getParameter("details"));
        offer.setDiscountedPrice(new BigDecimal(request.getParameter("discounted_price")));
        offer.setActive(Boolean.parseBoolean(request.getParameter("is_active")));
        offer.setTripId(parseIntegerParameter(request, "trip_id"));
        offerService.saveOffer(offer);
    }

    private int parseIntegerParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(paramName + " is required and cannot be empty.");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(paramName + " must be a valid integer.");
        }
    }

    private LocalDate parseLocalDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            logger.error("Invalid date format: {}", dateStr, e);
            return null; // Handle appropriately, e.g., default value or error
        }
    }

    private List<String> processUploadedPhotos(HttpServletRequest request, int hostId) throws IOException, ServletException {
        List<String> photoPaths = new ArrayList<>();
        String uploadPath = request.getServletContext().getRealPath("") + IMAGE_UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new IOException("Failed to create upload directory.");
        }

        for (Part part : request.getParts()) {
            if (part.getName().equals("photos") && part.getSize() > 0) {
                String fileName = hostId + "_" + System.currentTimeMillis() + "_" + Paths.get(part.getSubmittedFileName()).getFileName();
                String fileSavePath = uploadPath + File.separator + fileName;
                part.write(fileSavePath);
                photoPaths.add(IMAGE_UPLOAD_DIR + "/" + fileName);
            }
        }
        return photoPaths;
    }
}