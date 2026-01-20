<#ftl output_format="HTML" auto_esc=true>
<header>
    <nav class="navbar">
        <div class="nav-brand">
            <a href="${contextPath}/">🎵 SoundGate</a>
        </div>

        <div class="nav-links">
            <#if user??>
                <span class="user-info">Welcome, ${user.username} (${user.role})</span>
                <a href="${contextPath}/profile" class="nav-link">Profile</a>
                <#if user.role == 'ARTIST'>
                    <a href="${contextPath}/artist/dashboard" class="nav-link">Dashboard</a>
                    <a href="${contextPath}/submission/create" class="nav-link">Submit Track</a>
                <#elseif user.role == 'LABEL'>
                    <a href="${contextPath}/label/dashboard" class="nav-link">Dashboard</a>
                    <a href="${contextPath}/service/manage" class="nav-link">Services</a>
                </#if>
                <a href="${contextPath}/logout" class="nav-link">Logout</a>
            <#else>
                <a href="${contextPath}/auth/login" class="nav-link">Login</a>
                <a href="${contextPath}/auth/register" class="nav-link">Register</a>
            </#if>
        </div>
    </nav>
</header>

<style>
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
</style>