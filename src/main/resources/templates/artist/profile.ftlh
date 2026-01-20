<#ftl output_format="HTML" auto_esc=true>
<#assign title="Artist Profile">
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="profile-container">
        <div class="profile-header">
            <h1>Artist Profile</h1>
            <p>Manage your artist information</p>
        </div>

        <div class="profile-form-container">
            <form action="${contextPath}/profile/artist" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken!}">

                <div class="form-group">
                    <label for="artistName">Artist/Band Name *</label>
                    <input type="text" id="artistName" name="artistName" required
                           value="${profile.artistName!}" maxlength="100">
                </div>

                <div class="form-group">
                    <label for="genre">Music Genre</label>
                    <select id="genre" name="genre">
                        <option value="">Select Genre</option>
                        <option value="Rock" <#if profile.genre?? && profile.genre == "Rock">selected</#if>>Rock</option>
                        <option value="Pop" <#if profile.genre?? && profile.genre == "Pop">selected</#if>>Pop</option>
                        <option value="Hip-Hop" <#if profile.genre?? && profile.genre == "Hip-Hop">selected</#if>>Hip-Hop</option>
                        <option value="Electronic" <#if profile.genre?? && profile.genre == "Electronic">selected</#if>>Electronic</option>
                        <option value="Jazz" <#if profile.genre?? && profile.genre == "Jazz">selected</#if>>Jazz</option>
                        <option value="Classical" <#if profile.genre?? && profile.genre == "Classical">selected</#if>>Classical</option>
                        <option value="Country" <#if profile.genre?? && profile.genre == "Country">selected</#if>>Country</option>
                        <option value="R&B" <#if profile.genre?? && profile.genre == "R&B">selected</#if>>R&B</option>
                        <option value="Metal" <#if profile.genre?? && profile.genre == "Metal">selected</#if>>Metal</option>
                        <option value="Other" <#if profile.genre?? && profile.genre == "Other">selected</#if>>Other</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="description">Artist Description</label>
                    <textarea id="description" name="description" rows="4"
                              placeholder="Tell us about your music, influences, and style...">${profile.description!}</textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Save Profile</button>
                    <a href="${contextPath}/artist/dashboard" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>

        <#if profile?? && profile.artistName??>
        <div class="profile-preview">
            <h3>Profile Preview</h3>
            <div class="preview-card">
                <h4>${profile.artistName}</h4>
                <#if profile.genre??>
                    <p class="genre">Genre: ${profile.genre}</p>
                </#if>
                <#if profile.description??>
                    <p class="description">${profile.description}</p>
                </#if>
            </div>
        </div>
        </#if>
    </div>
</@layout.base>

<style>
.profile-container {
    max-width: 800px;
    margin: 0 auto;
}

.profile-header {
    text-align: center;
    margin-bottom: 2rem;
    color: white;
}

.profile-form-container {
    background: white;
    padding: 2rem;
    border-radius: 15px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    margin-bottom: 2rem;
}

.form-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-start;
}

.profile-preview {
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    padding: 2rem;
    border-radius: 15px;
    border: 1px solid rgba(255, 255, 255, 0.2);
    color: white;
}

.preview-card {
    background: rgba(255, 255, 255, 0.1);
    padding: 1.5rem;
    border-radius: 10px;
    margin-top: 1rem;
}

.preview-card h4 {
    margin-bottom: 0.5rem;
    font-size: 1.3rem;
}

.genre {
    font-style: italic;
    color: #ffd700;
}

.description {
    margin-top: 1rem;
    line-height: 1.5;
}
.profile-form-container {
    background: white;
    padding: 2rem;
    border-radius: 15px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    margin-bottom: 2rem;
}

/* ДОБАВЬ ЭТИ СТИЛИ ДЛЯ ФОРМЫ */
.profile-form-container .form-group label {
    color: #333; /* Черный цвет для меток */
    font-weight: bold;
}

.profile-form-container .form-group input,
.profile-form-container .form-group select,
.profile-form-container .form-group textarea {
    color: #333; /* Черный цвет текста */
    background: white;
    border: 2px solid #e1e5e9;
}

.profile-form-container .form-group input::placeholder {
    color: #666; /* Серый для placeholder */
}
</style>