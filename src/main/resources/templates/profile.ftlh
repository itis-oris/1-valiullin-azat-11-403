<#ftl output_format="HTML" auto_esc=true>
<#assign title="User Profile">
<#import "layouts/base.ftlh" as layout>

<@layout.base>
    <div class="profile-container">
        <div class="profile-header">
            <h1>User Profile</h1>
            <p>Manage your account information</p>
        </div>

        <#if message??>
            <div class="alert alert-success">
                ${message}
            </div>
        </#if>

        <#if error??>
            <div class="alert alert-error">
                ${error}
            </div>
        </#if>

        <div class="profile-content">
            <!-- Основная информация пользователя -->
            <div class="profile-info">
                <div class="info-card">
                    <h3>Account Details</h3>
                    <div class="info-group">
                        <label>Username:</label>
                        <span>${user.username!}</span>
                    </div>
                    <div class="info-group">
                        <label>Email:</label>
                        <span>${user.email!}</span>
                    </div>
                    <div class="info-group">
                        <label>Role:</label>
                        <span class="role-badge ${user.role?lower_case}">${user.role!}</span>
                    </div>
                    <div class="info-group">
                        <label>User ID:</label>
                        <span>${user.id!}</span>
                    </div>
                </div>

                <#-- Дополнительная информация профиля -->
                <#if user.role == 'ARTIST' && artistProfile??>
                    <div class="profile-card">
                        <h3>Artist Profile</h3>
                        <div class="info-group">
                            <label>Artist Name:</label>
                            <span>${artistProfile.artistName!user.username}</span>
                        </div>
                        <div class="info-group">
                            <label>Bio:</label>
                            <span>${artistProfile.bio!'Not specified'}</span>
                        </div>
                        <div class="info-group">
                            <label>Genre:</label>
                            <span>${artistProfile.genre!'Not specified'}</span>
                        </div>
                    </div>
                <#elseif user.role == 'LABEL' && labelProfile??>
                    <div class="profile-card">
                        <h3>Label Profile</h3>
                        <div class="info-group">
                            <label>Label Name:</label>
                            <span>${labelProfile.labelName!user.username}</span>
                        </div>
                        <div class="info-group">
                            <label>Description:</label>
                            <span>${labelProfile.description!'Not specified'}</span>
                        </div>
                        <div class="info-group">
                            <label>Genre:</label>
                            <span>${labelProfile.genre!'Not specified'}</span>
                        </div>
                        <div class="info-group">
                            <label>Founded:</label>
                            <span>${labelProfile.foundedYear!'Not specified'}</span>
                        </div>
                    </div>
                </#if>
            </div>

            <!-- Форма обновления профиля -->
            <div class="profile-actions">
                <div class="update-form">
                    <h3>Update Account</h3>
                    <form action="${contextPath}/profile" method="POST">
                        <div class="form-group">
                            <label for="username">Username:</label>
                            <input type="text" id="username" name="username" value="${user.username!}" required>
                        </div>
                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="email" id="email" name="email" value="${user.email!}" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Update Profile</button>
                    </form>
                </div>

                <!-- Действия в зависимости от роли -->
                <div class="role-actions">
                    <#if user.role == 'ARTIST'>
                        <h3>Artist Actions</h3>
                        <div class="action-links">
                            <a href="${contextPath}/artist/dashboard" class="btn btn-primary">
                                <i class="fas fa-tachometer-alt"></i> Artist Dashboard
                            </a>
                            <a href="${contextPath}/submission/create" class="btn btn-secondary">
                                <i class="fas fa-upload"></i> Submit New Track
                            </a>
                            <a href="${contextPath}/artist/profile" class="btn btn-outline">
                                <i class="fas fa-edit"></i> Edit Artist Profile
                            </a>
                        </div>
                    <#elseif user.role == 'LABEL'>
                        <h3>Label Actions</h3>
                        <div class="action-links">
                            <a href="${contextPath}/label/dashboard" class="btn btn-primary">
                                <i class="fas fa-tachometer-alt"></i> Label Dashboard
                            </a>
                            <a href="${contextPath}/submission/pending" class="btn btn-secondary">
                                <i class="fas fa-inbox"></i> Pending Submissions
                            </a>
                            <a href="${contextPath}/label/profile" class="btn btn-outline">
                                <i class="fas fa-edit"></i> Edit Label Profile
                            </a>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <style>
        .profile-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 2rem;
        }

        .profile-header {
            text-align: center;
            margin-bottom: 2rem;
            color: white;
        }

        .profile-header h1 {
            margin-bottom: 0.5rem;
            font-size: 2.5rem;
        }

        .profile-content {
            display: grid;
            gap: 2rem;
        }

        .profile-info, .profile-actions {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            padding: 2rem;
            border-radius: 15px;
            border: 1px solid rgba(255, 255, 255, 0.2);
        }

        .profile-card {
            margin-top: 1.5rem;
            padding-top: 1.5rem;
            border-top: 1px solid rgba(255, 255, 255, 0.1);
        }

        .info-card h3, .profile-card h3, .update-form h3, .role-actions h3 {
            color: #ffd700;
            margin-bottom: 1.5rem;
            border-bottom: 2px solid rgba(255, 215, 0, 0.3);
            padding-bottom: 0.5rem;
        }

        .info-group {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0.75rem 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }

        .info-group label {
            font-weight: bold;
            color: white;
        }

        .info-group span {
            color: rgba(255, 255, 255, 0.9);
        }

        .role-badge {
            padding: 0.25rem 0.75rem;
            border-radius: 20px;
            font-size: 0.875rem;
            font-weight: bold;
            text-transform: uppercase;
        }

        .role-badge.artist {
            background: #667eea;
            color: white;
        }

        .role-badge.label {
            background: #f093fb;
            color: white;
        }

        .form-group {
            margin-bottom: 1rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: white;
            font-weight: bold;
        }

        .form-group input {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid rgba(255, 255, 255, 0.2);
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.1);
            color: white;
            font-size: 1rem;
        }

        .form-group input:focus {
            outline: none;
            border-color: #667eea;
        }

        .action-links {
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
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
            text-align: center;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
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

        .btn-outline {
            background: transparent;
            color: white;
            border: 2px solid rgba(255, 255, 255, 0.3);
        }

        .btn-outline:hover {
            border-color: #667eea;
            transform: translateY(-2px);
        }

        .alert {
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1rem;
            text-align: center;
        }

        .alert-success {
            background: rgba(72, 187, 120, 0.2);
            color: #48bb78;
            border: 1px solid rgba(72, 187, 120, 0.3);
        }

        .alert-error {
            background: rgba(245, 101, 101, 0.2);
            color: #f56565;
            border: 1px solid rgba(245, 101, 101, 0.3);
        }

        @media (max-width: 768px) {
            .profile-container {
                padding: 1rem;
            }

            .action-links {
                flex-direction: column;
            }

            .info-group {
                flex-direction: column;
                align-items: flex-start;
                gap: 0.5rem;
            }
        }
    </style>
</@layout.base>