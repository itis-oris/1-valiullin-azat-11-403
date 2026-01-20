<#ftl output_format="HTML" auto_esc=true>
<#assign title="Login">
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="auth-container">
        <div class="form-container">
            <h2 class="text-center">Login to SoundGate</h2>

            <form action="${contextPath}/auth/login" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken!}">

                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required
                           value="${email!}" placeholder="Enter your email">
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required>
                </div>

                <button type="submit" class="btn btn-primary btn-block">Login</button>
            </form>

            <div class="auth-links">
                <p>Don't have an account? <a href="${contextPath}/auth/register">Register here</a></p>
            </div>
        </div>
    </div>
</@layout.base>

<style>
.auth-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 70vh;
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
</style>