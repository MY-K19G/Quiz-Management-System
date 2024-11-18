<%@ include file="loader.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Upload Quiz Data</title>
    <link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">
    <style>
        /* Basic Reset */
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
        }

        /* Container styling */
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f3f4f6;
            color: #333;
        }

        .upload-container {
            width: 100%;
            max-width: 500px;
            padding: 20px;
            border-radius: 10px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            text-align: center;
        }

        /* Title */
        h2 {
            color: #444;
            margin-bottom: 20px;
            font-size: 24px;
        }

        /* Form styling */
        form {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        input[type="file"] {
            border: 2px solid #ddd;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            width: 100%;
            cursor: pointer;
            background-color: #f9f9f9;
            transition: background-color 0.3s;
        }

        input[type="file"]:hover {
            background-color: #f1f1f1;
        }

        /* Button styling */
        button {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            background-color: #4CAF50;
            color: #fff;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #45a049;
        }

        /* Message styling */
        .message {
            margin-top: 20px;
            font-weight: bold;
            font-size: 16px;
        }

        .success {
            color: #4CAF50;
        }

        .error {
            color: #e74c3c;
        }
    </style>
</head>
<body>
    <div class="upload-container">
        <h2>Upload Quiz Data</h2>
        <form  id="uploadForm" enctype="multipart/form-data">
            <input type="file" id="fileInput" name="jsonfile" accept=".json" required>
            <button type="button" onclick="uploadFile()">Upload</button>
        </form>

        <!-- Message Display -->
        <div id="message" class="message"></div>
    </div>

    <script>
        async function uploadFile() {
        	showLoader();
            const fileInput = document.getElementById('fileInput');
            const messageDiv = document.getElementById('message');

            if (!fileInput.files.length) {
                messageDiv.textContent = "Please select a file to upload.";
                messageDiv.className = "message error";
                return;
            }

            const formData = new FormData();
            formData.append("jsonfile", fileInput.files[0]);

            try {
                const response = await fetch("/api/uploadJson", {
                    method: "POST",
                    body: formData
                });

                if (response.ok) {
                    const successMessage = await response.text(); // Get the success message from the server
                    messageDiv.textContent = successMessage; // Display the message
                    messageDiv.className = "message success";
                } else {
                    const errorText = await response.text(); // Get the error message from the server
                    messageDiv.textContent = "Error: " + errorText; // Display the error message
                    messageDiv.className = "message error";
                }
            } catch (error) {
                console.error('Error:', error);
                alert('File upload failed'); // More appropriate error message
            } finally {
                hideLoader(); // Hide the loader after the API call completes (success or error)
            }
        }
    </script>
</body>
</html>
    