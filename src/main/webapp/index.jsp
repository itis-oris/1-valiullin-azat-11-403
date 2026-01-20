<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Music Label Platform</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <header>
            <h1>🎵 Music Label Platform</h1>
            <p>Connect artists with music labels</p>
        </header>

        <main>
            <div class="hero-section">
                <h2>Start Your Music Journey</h2>
                <div class="cta-buttons">
                    <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-primary">Login</a>
                    <a href="${pageContext.request.contextPath}/auth/register" class="btn btn-secondary">Register</a>
                </div>
            </div>

            <div class="features">
                <div class="feature">
                    <h3>For Artists</h3>
                    <p>Submit your tracks to labels and get discovered</p>
                </div>
                <div class="feature">
                    <h3>For Labels</h3>
                    <p>Discover new talent and manage submissions</p>
                </div>
            </div>
        </main>

        <footer>
            <p>&copy; 2025 Music Label Platform. All rights reserved.</p>
        </footer>
    </div>
</body>
</html>