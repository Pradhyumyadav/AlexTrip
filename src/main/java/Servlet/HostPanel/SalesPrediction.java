package Servlet.HostPanel;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.classifiers.functions.LinearRegression;
import weka.core.SerializationHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SalesPrediction", urlPatterns = {"/SalesPrediction"})
public class SalesPrediction extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SalesPrediction.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Instances data2023 = loadDataset();
            LinearRegression model = loadModel();

            List<Map<String, Object>> predictedData2024 = simulate2024Predictions(model, data2023);
            List<Map<String, Object>> actualData2023 = prepareActualData(data2023);

            request.setAttribute("salesData2024", predictedData2024);
            request.setAttribute("salesData2023", actualData2023);
            request.getRequestDispatcher("/WEB-INF/views/HostPanel/salesPrediction.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing sales prediction", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing sales prediction: " + e.getMessage());
        }
    }

    private Instances loadDataset() throws Exception {
        File file = new File(getServletContext().getRealPath("/WEB-INF/data/Sales_Data_2023.csv"));
        if (!file.exists()) {
            throw new IOException("Dataset file not found: " + "/WEB-INF/data/Sales_Data_2023.csv");
        }

        CSVLoader loader = new CSVLoader();
        loader.setSource(file);
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1); // Set the class index to the last attribute
        return data;
    }

    private LinearRegression loadModel() throws Exception {
        String modelPath = getServletContext().getRealPath("/WEB-INF/model/SalesPrediction_LinearRegression.model");
        return (LinearRegression) SerializationHelper.read(modelPath);
    }

    private List<Map<String, Object>> simulate2024Predictions(LinearRegression model, Instances data2023) throws Exception {
        List<Map<String, Object>> predictions = new ArrayList<>();
        for (int i = 0; i < data2023.numInstances(); i++) {
            Instance instance = data2023.instance(i);
            double originalCost = instance.value(data2023.attribute("Advertising Cost(£)"));
            double increasedCost = originalCost * 1.10;  // Simulating a 10% increase
            instance.setValue(data2023.attribute("Advertising Cost(£)"), increasedCost);
            double predictedRevenue = model.classifyInstance(instance);

            predictions.add(createDataMap(instance, originalCost, increasedCost, predictedRevenue));
        }
        return predictions;
    }

    private List<Map<String, Object>> prepareActualData(Instances data) {
        List<Map<String, Object>> actual = new ArrayList<>();
        for (int i = 0; i < data.numInstances(); i++) {
            Instance instance = data.instance(i);
            double originalCost = instance.value(data.attribute("Advertising Cost(£)"));
            double actualRevenue = instance.classValue();  // Assuming the last column is revenue

            actual.add(createDataMap(instance, originalCost, originalCost, actualRevenue));
        }
        return actual;
    }

    private Map<String, Object> createDataMap(Instance instance, double originalCost, double increasedCost, double revenue) {
        Map<String, Object> dataMap = new HashMap<>();
        int monthIndex = (int) instance.value(instance.dataset().attribute("Month")) - 1;  // Adjusting for zero-based index
        dataMap.put("month", monthIndex + 1);  // Display month as 1-based for user clarity
        dataMap.put("originalCost", originalCost);
        dataMap.put("advertisingCost", increasedCost);
        dataMap.put("revenue", revenue);
        return dataMap;
    }
}