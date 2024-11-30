<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/HostPanel/HostNavbar.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Host Support</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            color: #fff;
            font-family: 'Arial', sans-serif;
            margin: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        .support-container {
            background: rgba(255, 255, 255, 0.95);
            padding: 40px 30px;
            border-radius: 12px;
            max-width: 600px;
            width: 100%;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
            margin: 20px auto;
        }

        h2 {
            font-size: 28px;
            font-weight: bold;
            color: #6a11cb;
            text-align: center;
            margin-bottom: 20px;
        }

        p {
            text-align: center;
            font-size: 16px;
            color: #555;
            margin-bottom: 30px;
        }

        p a {
            color: #2575fc;
            text-decoration: none;
            font-weight: bold;
        }

        p a:hover {
            text-decoration: underline;
        }

        .form-control {
            border-radius: 10px;
            padding: 12px;
            font-size: 16px;
            border: 1px solid #ddd;
        }

        .form-control:focus {
            border-color: #6a11cb;
            box-shadow: 0 0 6px rgba(106, 17, 203, 0.5);
        }

        .btn-primary {
            background: linear-gradient(to right, #6a11cb, #2575fc);
            border: none;
            font-size: 16px;
            padding: 12px 20px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background: linear-gradient(to right, #2575fc, #6a11cb);
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
        }

        footer {
            background: rgba(255, 255, 255, 0.9);
            color: #6c757d;
            text-align: center;
            padding: 10px 0;
            font-size: 14px;
            margin-top: auto;
        }
    </style>
</head>
<body>

<div class="support-container">
    <h2>Contact Host Support</h2>
    <p>If you have any questions or need assistance, feel free to contact us at
        <a href="mailto:alextrip@alex.com">alextrip@alex.com</a> or use the form below.
    </p>
    <form action="HostSupport" method="post">
        <div class="mb-4">
            <label for="name" class="form-label">Your Name</label>
            <input type="text" id="name" name="name" class="form-control" placeholder="Enter your name" required>
        </div>
        <div class="mb-4">
            <label for="email" class="form-label">Your Email</label>
            <input type="email" id="email" name="email" class="form-control" placeholder="Enter your email" required>
        </div>
        <div class="mb-4">
            <label for="subject" class="form-label">Subject</label>
            <input type="text" id="subject" name="subject" class="form-control" placeholder="Enter the subject" required>
        </div>
        <div class="mb-4">
            <label for="message" class="form-label">Message</label>
            <textarea id="message" name="message" class="form-control" placeholder="Enter your message" rows="5" required></textarea>
        </div>
        <div class="text-center">
            <button type="submit" class="btn btn-primary">Submit Query</button>
        </div>
    </form>
</div>
<footer>
    <p>&copy; 2024 AlexTripAgencyManagementSystem. All rights reserved.</p>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>