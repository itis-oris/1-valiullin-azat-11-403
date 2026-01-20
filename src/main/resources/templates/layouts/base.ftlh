<#ftl output_format="HTML" auto_esc=true>
<#macro base>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title!"Music Label Platform"} - SoundGate</title>

    <!-- INLINE STYLES -->
    <style>
        /* Reset and base styles */
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            color: white;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            min-height: 80vh;
        }

        /* Header styles */
        .navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1rem 2rem;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }

        .nav-brand a {
            font-size: 1.5rem;
            font-weight: bold;
            color: white;
            text-decoration: none;
        }

        .nav-links {
            display: flex;
            gap: 1.5rem;
            align-items: center;
        }

        .nav-link {
            color: white;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        .nav-link:hover {
            background: rgba(255, 255, 255, 0.2);
        }

        .user-info {
            color: white;
            font-weight: 500;
        }

        /* Form styles */
        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: bold;
            color: white;
        }

        .form-group input,
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 12px;
            border: 2px solid rgba(255, 255, 255, 0.2);
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.1);
            color: white;
            font-size: 1rem;
        }

        .form-group input:focus,
        .form-group textarea:focus,
        .form-group select:focus {
            outline: none;
            border-color: #667eea;
        }

        .form-group input::placeholder,
        .form-group textarea::placeholder {
            color: rgba(255, 255, 255, 0.5);
        }

        .btn {
            display: inline-block;
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            font-weight: bold;
            text-decoration: none;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 1rem;
        }

        .btn-primary {
            background: #667eea;
            color: white;
        }

        .btn-primary:hover {
            background: #5a6fd8;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background: rgba(255, 255, 255, 0.2);
            color: white;
        }

        .btn-secondary:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
        }

        /* Alert styles */
        .alert {
            padding: 1rem 1.5rem;
            margin-bottom: 2rem;
            border-radius: 8px;
            border-left: 4px solid;
        }

        .alert-success {
            background: rgba(45, 206, 137, 0.2);
            border-color: #2dce89;
            color: #2dce89;
        }

        .alert-error {
            background: rgba(245, 101, 101, 0.2);
            border-color: #f56565;
            color: #f56565;
        }

        .alert-info {
            background: rgba(91, 155, 213, 0.2);
            border-color: #5b9bd5;
            color: #5b9bd5;
        }

        .alert-close {
            background: none;
            border: none;
            font-size: 1.5rem;
            cursor: pointer;
            color: inherit;
            opacity: 0.7;
            float: right;
        }

        .alert-close:hover {
            opacity: 1;
        }

        /* Profile specific styles */
        .profile-container {
            max-width: 800px;
            margin: 0 auto;
        }

        .profile-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .profile-form-container {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            padding: 2rem;
            border-radius: 15px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            margin-bottom: 2rem;
        }

        .form-actions {
            display: flex;
            gap: 1rem;
            margin-top: 2rem;
        }

        /* Auth specific styles */
        .auth-container {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 70vh;
        }

        .form-container {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            max-width: 400px;
            width: 100%;
        }

        .text-center {
            text-align: center;
        }

        .btn-block {
            width: 100%;
            margin-top: 1rem;
        }

        .auth-links {
            margin-top: 1.5rem;
            text-align: center;
        }

        .auth-links a {
            color: #667eea;
            text-decoration: none;
        }

        .auth-links a:hover {
            text-decoration: underline;
        }

        /* Footer */
        footer {
            text-align: center;
            padding: 2rem 0;
            color: rgba(255, 255, 255, 0.8);
        }
    </style>

    <#if csrfToken??>
        <meta name="_csrf" content="${csrfToken}">
    </#if>
</head>
<body>
    <#include "header.ftlh">

    <main class="container">
        <#-- Flash messages -->
        <#if flashError??>
            <div class="alert alert-error">
                ${flashError}
                <button type="button" class="alert-close" onclick="this.parentElement.remove()">×</button>
            </div>
        </#if>

        <#if flashSuccess??>
            <div class="alert alert-success">
                ${flashSuccess}
                <button type="button" class="alert-close" onclick="this.parentElement.remove()">×</button>
            </div>
        </#if>

        <#if flashInfo??>
            <div class="alert alert-info">
                ${flashInfo}
                <button type="button" class="alert-close" onclick="this.parentElement.remove()">×</button>
            </div>
        </#if>

        <#-- Page content -->
        <#nested>
    </main>

    <#include "footer.ftlh">

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>
</#macro>