<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="loader.jsp" %>
<%@page import="k19g.quiz.entity.Level"%>
<%@ page isELIgnored="true" %>
<%@page import="java.util.List"%>
<%@page import="k19g.quiz.entity.Quiz"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Export Quiz</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #e2dcdc;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }

        h1 {
            margin-bottom: 20px;
            font-size: 2rem;
            color: #333;
        }

        .filter-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 10px;
            margin-bottom: 20px;
            width: 100%;
        }

        select {
            padding: 8px;
            border: 1px solid black;
            font-size: 1rem;
            background-color: #f9f9f9;
            width: 100%;
            max-width: 250px;
            box-sizing: border-box;
        }

        select:focus {
            outline: none;
            border-color: orangered;
        }

        .export-buttons {
            display: flex;
            justify-content: center;
            width: 100%;
        }

        button {
            padding: 10px 20px;
            border: 1px solid black;
            background-color: white;
            color: black;
            font-size: 1rem;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #0056b3;
        }

        @media (max-width: 768px) {
            h1 {
                font-size: 1.5rem;
                margin-bottom: 15px;
            }

            .filter-container {
                flex-direction: column;
                align-items: center;
            }

            select {
                max-width: 50%;
                margin: 10px 0;
            }

            button {
                padding: 8px 16px;
                font-size: 0.9rem;
            }
        }

        @media (max-width: 480px) {
            h1 {
                font-size: 1.2rem;
                margin-bottom: 10px;
            }

            .filter-container {
                gap: 8px;
            }

            select {
                padding: 6px;
                font-size: 0.9rem;
                max-width: 50%;
                margin: 10px 0;
            }

            button {
                padding: 6px 12px;
                font-size: 0.85rem;
            }
        }
    </style>
</head>
<body>
    <h1>Quiz Export</h1>
		<div class="filter-container">

        <% 
			List<String> allCategorys = (List<String>) request.getAttribute("allCategorys");
			List<String> allTypes = (List<String>) request.getAttribute("allTypes");
		    List<Level> allLevels = (List<Level>) request.getAttribute("allLevels");
		%>

		<!-- Level Dropdown -->
        <select name=level id="levelFilter">
            <option value="">All Levels</option>
            <% if (allLevels !=null) {
            	for (Level level : allLevels) { %>
            <option value="<%= level.name() %>">
                <%= level.name() %>
            </option>
            <% } } %>
        </select>

		<!-- Type Dropdown -->
        <select name="type" id="typeFilter">
            <option value="">All Types</option>
            <% if (allTypes !=null) {
           		 for (String type : allTypes) { %>
            <option value="<%= type %>">
                <%= type %>
            </option>
            <% } } %>
        </select>
                
        <!-- Category Dropdown -->
        <select name="category" id="categoryFilter">
            <option value="">All Categories</option>
            <% if (allCategorys !=null) { 
            	for(String category: allCategorys){ %>
            <option value="<%=category %>">
                <%=category %>
            </option>
            <% } } %>
        </select>

    </div>

    <div class="export-buttons">
        <button id="downloadButton">Download JSON</button>
    </div>
	
    <script>
    document.getElementById('downloadButton').addEventListener('click', function () {
        showLoader();

        const category = document.getElementById("categoryFilter").value;
        const type = document.getElementById("typeFilter").value;
        const level = document.getElementById("levelFilter").value;

        fetch('/filterQuestions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                category: category,
                type: type,
                level: level,
            })
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errMessage => {
                        throw new Error(errMessage);
                    });
                }

                const contentDisposition = response.headers.get('Content-Disposition');
                let filename = 'quiz.json';

                if (contentDisposition) {
                    const matches = /filename="?([^"]+)"?/.exec(contentDisposition);
                    if (matches && matches[1]) {
                        filename = matches[1];
                    }
                }

                return response.blob().then(blob => ({ blob, filename }));
            })
            .then(({ blob, filename }) => {
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = filename;
                document.body.appendChild(a);
                a.click();
                a.remove();
            })
            .catch(error => {
                console.error('Error during download:', error);
                
                if (error.message === 'Failed to fetch') {
                    alert('Network error: Unable to reach the server. Please check your internet connection and try again.');
                } else {
                    alert('An error occurred: ' + error.message);
                }
            })
            .finally(() => {
                hideLoader(); 
            });
    	});


    </script>
</body>
</html>
