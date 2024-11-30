<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.json.JSONArray" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sales Prediction</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, rgba(58,123,213,0.8), rgba(0,210,255,0.8));
            color: #fff;
        }
        h1 {
            color: #fff;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
        }
        .table-container, .chart-container {
            background-color: rgba(255, 255, 255, 0.8);
            padding: 1rem;
            border-radius: 8px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
            margin-top: 20px;
        }
        .table {
            border-radius: 10px;
            overflow: hidden;
        }
        th {
            background-color: #198754;
            color: #ffffff;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        footer {
            margin-top: 20px;
            padding: 10px 0;
            background: rgba(0,0,0,0.1);
            border-top: 1px solid rgba(255,255,255,0.2);
            color: #6c757d;
            text-align: center;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/HostPanel/HostNavbar.jsp" />
<div class="container mt-5">
    <h1>Sales Prediction for the Year 2024</h1>
    <div class="total-revenue">
        Total Revenue for 2024: <span id="totalRevenue"></span>
    </div>
    <div class="table-container">
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>Month</th>
                <th>Original Advertising Cost (£)</th>
                <th>Increased Advertising Cost (£)</th>
                <th>Predicted Revenue (£)</th>
            </tr>
            </thead>
            <tbody>
            <%
                String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                List<Map<String, Object>> salesData2024 = (List<Map<String, Object>>) request.getAttribute("salesData2024");
                List<Map<String, Object>> salesData2023 = (List<Map<String, Object>>) request.getAttribute("salesData2023");
                double totalRevenue2024 = 0.0;
                JSONArray revenueData2024 = new JSONArray();
                JSONArray revenueData2023 = new JSONArray();
                JSONArray monthLabels = new JSONArray();

                for (int i = 0; i < months.length; i++) {
                    if (salesData2024 != null && !salesData2024.isEmpty()) {
                        Map<String, Object> data = salesData2024.get(i);
                        double originalCost = (Double) data.get("originalCost");
                        double advertisingCost = (Double) data.get("advertisingCost");
                        double revenue = (Double) data.get("revenue");
                        revenueData2024.put(revenue);
                        monthLabels.put(months[i]);
                        totalRevenue2024 += revenue;
            %>
            <tr>
                <td><%= months[i] %></td>
                <td>£<%= String.format("%.2f", originalCost) %></td>
                <td>£<%= String.format("%.2f", advertisingCost) %></td>
                <td>£<%= String.format("%.2f", revenue) %></td>
            </tr>
            <%
                    }
                    if (salesData2023 != null && !salesData2023.isEmpty()) {
                        revenueData2023.put((Double) salesData2023.get(i).get("revenue"));
                    }
                }
            %>
            </tbody>
        </table>
    </div>
    <div class="chart-container">
        <canvas id="salesChart"></canvas>
    </div>
    <footer>
        <p>&copy; 2024 AlexTripAgencyManagementSystem. All rights reserved.</p>
    </footer>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const totalRevenueSpan = document.getElementById('totalRevenue');
        const ctx = document.getElementById('salesChart').getContext('2d');
        const monthLabelsArray = <%= monthLabels.toString() %>;
        const revenueData2024Array = <%= revenueData2024.toString() %>;
        const revenueData2023Array = <%= revenueData2023.toString() %>;
        totalRevenueSpan.textContent = '£' + <%= new java.text.DecimalFormat("#.##").format(totalRevenue2024) %>;

        const chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: monthLabelsArray,
                datasets: [{
                    label: 'Predicted Revenue 2024 (£)',
                    data: revenueData2024Array,
                    borderColor: '#198754',
                    backgroundColor: 'rgba(25, 135, 84, 0.5)',
                    tension: 0.4
                }, {
                    label: 'Actual Revenue 2023 (£)',
                    data: revenueData2023Array,
                    borderColor: '#007bff',
                    backgroundColor: 'rgba(0, 123, 255, 0.5)',
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top'
                    },
                    title: {
                        display: true,
                        text: 'Revenue Comparison 2023 vs 2024'
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Revenue (£)'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'Months'
                        }
                    }
                }
            }
        });
    });
</script>
</body>
</html>