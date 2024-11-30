<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logout - AlexTrip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/WEB-INF/views/css/styles.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            background: linear-gradient(to bottom, rgba(0, 86, 179, 0.9), var(--background-color));
            color: var(--text-color);
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            text-align: center;
        }

        .logout-container {
            background: rgba(255, 255, 255, 0.95);
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
            max-width: 500px;
            width: 90%;
        }

        .logout-container h1 {
            color: var(--primary-color);
            font-size: 2.5rem;
            margin-bottom: 20px;
        }

        .logout-container p {
            font-size: 1.2rem;
            color: var(--dark-gray);
        }

        .logout-container .btn {
            margin-top: 20px;
            background-color: var(--primary-color);
            color: var(--white);
            padding: 10px 20px;
            font-weight: bold;
            border-radius: 6px;
            border: none;
            transition: background-color 0.2s ease-in-out;
        }

        .logout-container .btn:hover {
            background-color: var(--primary-color-dark);
        }
    </style>
</head>
<body>
<div class="logout-container">
    <h1>You Have Successfully Logged Out</h1>
    <p>Thank you for using AlexTrip. We hope to see you again soon!</p>
    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Login Again</a>
</div>
</body>
</html>