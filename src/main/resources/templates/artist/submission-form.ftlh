<#ftl output_format="HTML" auto_esc=true>
<#assign title="Submit Track">
<#import "../layouts/base.ftlh" as layout>

<@layout.base>
    <div class="submission-container">
        <div class="submission-header">
            <h1>Submit Your Track</h1>
            <p>Share your music with labels</p>
        </div>

        <div class="submission-form-container">
            <form action="${contextPath}/submission/create" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken!}">

                <div class="form-group">
                    <label for="trackTitle">Track Title *</label>
                    <input type="text" id="trackTitle" name="trackTitle" required
                           value="${trackTitle!}" maxlength="200"
                           placeholder="Enter the title of your track">
                </div>

                <div class="form-group">
                    <label for="labelId">Select Label *</label>
                    <select id="labelId" name="labelId" required>
                        <option value="">Choose a label...</option>
                        <#list labels as label>
                            <option value="${label.id}"
                                    <#if selectedLabelId?? && selectedLabelId == label.id>selected</#if>>
                                ${label.labelName}
                            </option>
                        </#list>
                    </select>
                </div>

                <div class="form-group">
                    <label for="serviceId">Select Service *</label>
                    <select id="serviceId" name="serviceId" required>
                        <option value="">Choose a service...</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="trackFile">Upload Track *</label>
                    <input type="file" id="trackFile" name="trackFile" required
                           accept="audio/mpeg,audio/wav,audio/mp3"
                           data-preview="file-preview">
                    <small>Supported formats: MP3, WAV. Max size: 10MB</small>
                    <div id="file-preview" class="file-preview"></div>
                </div>

                <div class="form-group">
                    <label for="notes">Additional Notes (Optional)</label>
                    <textarea id="notes" name="notes" rows="3"
                              placeholder="Any additional information about your track...">${notes!}</textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Submit Track</button>
                    <a href="${contextPath}/artist/dashboard" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>

        <div class="submission-info">
            <h3>Submission Guidelines</h3>
            <ul>
                <li>Ensure your track is properly mixed and mastered</li>
                <li>File size should not exceed 10MB</li>
                <li>Only submit original content you have rights to</li>
                <li>Labels typically respond within 7-14 days</li>
                <li>You can track your submission status in your dashboard</li>
            </ul>
        </div>
    </div>

    <script>
        // Все сервисы переданы из сервлета
        const allServices = [
            <#list allServices as service>
            {
                id: ${service.id},
                labelId: ${service.labelId},
                name: "${service.name}",
                basePrice: ${service.basePrice}
            },
            </#list>
        ];

        document.getElementById('labelId').addEventListener('change', function() {
            const labelId = this.value;
            const serviceSelect = document.getElementById('serviceId');

            serviceSelect.innerHTML = '<option value="">Choose a service...</option>';

            if (labelId) {
                // Ищем сервисы для выбранного лейбла
                const services = allServices.filter(service => service.labelId == labelId);

                services.forEach(service => {
                    const option = document.createElement('option');
                    option.value = service.id;
                    option.textContent = service.name + (service.basePrice > 0 ? ' ($' + service.basePrice + ')' : ' (Free)');
                    serviceSelect.appendChild(option);
                });

                if (services.length === 0) {
                    serviceSelect.innerHTML = '<option value="">No services available for this label</option>';
                }
            }
        });

        // File preview
        document.getElementById('trackFile').addEventListener('change', function(e) {
            const file = e.target.files[0];
            const preview = document.getElementById('file-preview');

            if (file) {
                preview.innerHTML =
                    '<div class="file-info">' +
                    '<strong>Selected file:</strong> ' + file.name + '<br>' +
                    '<strong>Size:</strong> ' + (file.size / 1024 / 1024).toFixed(2) + ' MB<br>' +
                    '<strong>Type:</strong> ' + file.type +
                    '</div>';
            } else {
                preview.innerHTML = '';
            }
        });
    </script>

    <style>
        .submission-container {
            max-width: 800px;
            margin: 0 auto;
        }

        .submission-header {
            text-align: center;
            margin-bottom: 2rem;
            color: white;
        }

        .submission-form-container {
            background: white;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            margin-bottom: 2rem;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: bold;
            color: #333;
        }

        .form-group input,
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 12px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            background: white;
            color: #333;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .form-group input:focus,
        .form-group textarea:focus,
        .form-group select:focus {
            outline: none;
            border-color: #667eea;
        }

        .form-group input::placeholder,
        .form-group textarea::placeholder {
            color: #666;
        }

        .form-group small {
            display: block;
            margin-top: 0.5rem;
            color: #666;
            font-size: 0.8rem;
        }

        .file-preview {
            margin-top: 0.5rem;
            padding: 1rem;
            background: #f8f9fa;
            border-radius: 5px;
            border-left: 4px solid #667eea;
            color: #333;
        }

        .file-info {
            font-size: 0.9rem;
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
            display: inline-block;
            text-align: center;
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
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
            transform: translateY(-2px);
        }

        .submission-info {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            padding: 2rem;
            border-radius: 15px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: white;
        }

        .submission-info h3 {
            margin-bottom: 1rem;
            color: #ffd700;
        }

        .submission-info ul {
            list-style: none;
            padding: 0;
        }

        .submission-info li {
            margin-bottom: 0.5rem;
            padding-left: 1.5rem;
            position: relative;
        }

        .submission-info li:before {
            content: "✓";
            position: absolute;
            left: 0;
            color: #4CAF50;
            font-weight: bold;
        }
    </style>
</@layout.base>