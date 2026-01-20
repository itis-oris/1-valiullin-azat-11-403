<#ftl output_format="HTML" auto_esc=true>
<#assign title="Label Profile">
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="profile-container">
        <div class="profile-header">
            <h1>Label Profile</h1>
            <p>Manage your label information and branding</p>
        </div>

        <#if successMessage??>
            <div class="alert alert-success">
                ${successMessage}
            </div>
        </#if>

        <#if errorMessage??>
            <div class="alert alert-error">
                ${errorMessage}
            </div>
        </#if>

        <div class="profile-form-container">
            <form action="${contextPath}/label/profile" method="post">
                <div class="form-group">
                    <label for="labelName">Label Name *</label>
                    <input type="text" id="labelName" name="labelName" required
                           value="<#if profile??>${profile.labelName!}</#if>" maxlength="100"
                           placeholder="Enter your label name">
                </div>

                <div class="form-group">
                    <label for="description">Label Description</label>
                    <textarea id="description" name="description" rows="5"
                              placeholder="Describe your label, music genre focus, philosophy, and what artists can expect when working with you..."><#if profile??>${profile.description!}</#if></textarea>
                    <small>This description will be visible to artists considering your services</small>
                </div>

                <div class="form-group">
                    <label for="contactEmail">Contact Email</label>
                    <input type="email" id="contactEmail" name="contactEmail"
                           value="<#if profile??>${profile.contactEmail!}<#else>${user.email!}</#if>"
                           placeholder="Public contact email for artists">
                </div>

                <div class="form-group">
                    <label for="website">Website</label>
                    <input type="url" id="website" name="website"
                           value="<#if profile??>${profile.website!}</#if>"
                           placeholder="https://your-label.com">
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Save Profile</button>
                    <a href="${contextPath}/label/dashboard" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>

        <#if profile?? && profile.labelName??>
        <div class="profile-preview">
            <h3>Profile Preview</h3>
            <div class="preview-card">
                <h4>${profile.labelName}</h4>
                <#if profile.description??>
                    <p class="description">${profile.description}</p>
                </#if>
                <div class="contact-info">
                    <#if profile.contactEmail??>
                        <p><strong>Email:</strong> ${profile.contactEmail}</p>
                    </#if>
                    <#if profile.website??>
                        <p><strong>Website:</strong> <a href="${profile.website}" target="_blank">${profile.website}</a></p>
                    </#if>
                </div>
            </div>
        </div>
        </#if>
    </div>
</@layout.base>

<style>
.profile-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 2rem;
}

.profile-header {
    text-align: center;
    margin-bottom: 2rem;
}

.profile-header h1 {
    color: white;
    margin-bottom: 0.5rem;
}

.profile-header p {
    color: rgba(255, 255, 255, 0.8);
    font-size: 1.1rem;
}

.profile-form-container {
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    padding: 2rem;
    border-radius: 15px;
    border: 1px solid rgba(255, 255, 255, 0.2);
    margin-bottom: 2rem;
}

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
.form-group textarea {
    width: 100%;
    padding: 12px;
    border: 2px solid rgba(255, 255, 255, 0.2);
    border-radius: 8px;
    background: rgba(255, 255, 255, 0.1);
    color: white;
    font-size: 1rem;
    transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus {
    outline: none;
    border-color: #667eea;
}

.form-group input::placeholder,
.form-group textarea::placeholder {
    color: rgba(255, 255, 255, 0.5);
}

.form-group small {
    display: block;
    margin-top: 0.5rem;
    color: rgba(255, 255, 255, 0.7);
    font-size: 0.8rem;
}

.form-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-start;
    margin-top: 2rem;
}

.btn {
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

.profile-preview {
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    padding: 2rem;
    border-radius: 15px;
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.profile-preview h3 {
    color: white;
    margin-bottom: 1rem;
}

.preview-card {
    background: rgba(255, 255, 255, 0.05);
    padding: 1.5rem;
    border-radius: 10px;
    border: 1px solid rgba(255, 255, 255, 0.1);
}

.preview-card h4 {
    color: white;
    margin-bottom: 1rem;
    font-size: 1.3rem;
}

.preview-card .description {
    color: rgba(255, 255, 255, 0.9);
    line-height: 1.6;
    margin-bottom: 1rem;
}

.contact-info {
    margin-top: 1rem;
    padding-top: 1rem;
    border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.contact-info p {
    margin: 0.5rem 0;
    color: rgba(255, 255, 255, 0.8);
}

.contact-info strong {
    color: white;
}

.contact-info a {
    color: #ffd700;
    text-decoration: none;
}

.contact-info a:hover {
    text-decoration: underline;
}

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
</style>