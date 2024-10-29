<%@page import="java.util.List"%>
<%@ page isELIgnored="true"%>

<!DOCTYPE html>

<html lang="en">

<head>
    <!-- Character encoding for the page -->
    <meta charset="UTF-8">

    <!-- Responsive design for mobile and desktop -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Page title -->
    <title>Quiz Prompt Page</title>
    
    <link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">

    <!-- Bootstrap CSS for styling -->
   <link rel="stylesheet" href="/assets/bootstrap/dist/css/bootstrap.min.css">
	
    <!-- Google Fonts for custom typography -->
    <link href="/assets/fonts/custom_Font.css" rel="stylesheet">

    <!-- Internal CSS for custom styling -->
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #f3ec78, #af4261);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .card {
            background-color: white;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            max-width: 600px;
            width: 100%;
        }

        .card h2 {
            text-align: center;
            margin-bottom: 1.5rem;
            font-weight: 600;
            color: #af4261;
        }

        .label-column {
            text-align: right;
            font-weight: 500;
            color: #333;
        }

        .btn-group .btn {
            transition: background-color 0.3s ease;
        }

        .btn-group .btn:hover {
            background-color: #af4261;
            color: white;
        }

        .form-control {
            box-shadow: none;
            border: 1px solid #ddd;
            transition: box-shadow 0.3s ease;
        }

        .form-control:focus {
            box-shadow: 0 0 10px rgba(175, 66, 97, 0.5);
            border-color: #af4261;
        }

        .btn-primary {
            background-color: #af4261;
            border: none;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #f3ec78;
            color: #af4261;
        }

        .btn-primary:focus {
            box-shadow: 0 0 10px rgba(243, 236, 120, 0.7);
        }

        .mt-2 {
            margin-top: 1rem;
        }
    </style>
</head>

<body>
	<img alt="java_quiz_img" src="/assets/img/java_quiz_icon.jpg">
    <!-- Main container for the quiz form -->
    <div class="card">
        <h2>Quiz Settings</h2>

        <!-- Quiz form to be submitted via POST -->
        <form action="/processQuizSetup" method="post">

            <!-- Input for user's name -->
            <div class="form-group row">
                <label class="col-sm-4 col-form-label label-column">Your Name</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" placeholder="Enter your name" name="username">
                </div>
            </div>

            <!-- Radio buttons for selecting the number of questions -->
            <div class="form-group row">
                <label class="col-sm-4 col-form-label label-column">Number of Questions</label>
                <div class="col-sm-8">
                    <div class="btn-group btn-group-toggle" data-toggle="buttons">
                        <label class="btn btn-outline-primary">
                            <input type="radio" name="questions" value="5" onclick="toggleCustomInput('questions-custom', false)"> 5
                        </label>
                        <label class="btn btn-outline-primary">
                            <input type="radio" name="questions" value="10" onclick="toggleCustomInput('questions-custom', false)"> 10
                        </label>
                        <label class="btn btn-outline-primary">
                            <input type="radio" name="questions" value="15" onclick="toggleCustomInput('questions-custom', false)"> 15
                        </label>
                        <label class="btn btn-outline-primary">
                            <input type="radio" name="questions" value="20" onclick="toggleCustomInput('questions-custom', false)"> 20
                        </label>
                        <label class="btn btn-outline-primary">
                            <input id="custom-questions" type="radio" value="custom" onclick="toggleCustomInput('questions-custom', true)"> Custom
                        </label>
                    </div>
                    <input type="number" min="1" id="questions-custom" class="form-control mt-2" name="questions" placeholder="Enter custom number" style="display:none;">
                </div>
            </div>

            <!-- Radio buttons for selecting the quiz difficulty level -->
            <div class="form-group row level-group ">
                <label class="col-sm-4 col-form-label label-column">Level</label>
                <div class="col-sm-8">
                    <div class="btn-group btn-group-toggle level-lebel" data-toggle="buttons">
                        <label class="btn btn-outline-success">
                            <input type="radio" name="level" value="easy"> Easy
                        </label>
                        <label class="btn btn-outline-success">
                            <input type="radio" name="level" value="medium"> Medium
                        </label>
                        <label class="btn btn-outline-success">
                            <input type="radio" name="level" value="hard"> Hard
                        </label>
                        <label class="btn btn-outline-success">
                            <input type="radio" name="level" value="expert"> Expert
                        </label>
                    </div>
                </div>
            </div>

            <!-- Dropdown for selecting a quiz category -->
            <%
                List<String> allCategorys = (List<String>) request.getAttribute("allCategorys");
                List<String> allTypes = (List<String>) request.getAttribute("allTypes");
            %>
            <div class="form-group row">
                <label class="col-sm-4 col-form-label label-column">Category</label>
                <div class="col-sm-8">
                    <select class="form-control" name="category">
                        <option value="">All Categories</option>
                        <%
                            for (String category : allCategorys) {
                                out.print("<option value=\"" + category + "\">" + category + "</option>");
                            }
                        %>
                    </select>
                </div>
            </div>

            <!-- Dropdown for selecting the content type -->
            <div class="form-group row">
                <label class="col-sm-4 col-form-label label-column">Content Type</label>
                <div class="col-sm-8">
                    <select class="form-control" name="type">
                        <option value="">All Types</option>
                        <%
                            for (String type : allTypes) {
                                out.print("<option value=\"" + type + "\">" + type + "</option>");
                            }
                        %>
                    </select>
                </div>
            </div>

            <!-- Radio buttons for selecting total quiz time -->
            <div class="form-group row time-group">
                <label class="col-sm-4 col-form-label label-column">Total Time (minutes)</label>
                <div class="col-sm-8">
                    <div class="btn-group btn-group-toggle time-lebel" data-toggle="buttons">
                        <label class="btn btn-outline-info">
                            <input type="radio" name="time" value="5" onclick="toggleCustomInput('time-custom', false)"> 5 min
                        </label>
                        <label class="btn btn-outline-info">
                            <input type="radio" name="time" value="10" onclick="toggleCustomInput('time-custom', false)"> 10 min
                        </label>
                        <label class="btn btn-outline-info">
                            <input type="radio" name="time" value="15" onclick="toggleCustomInput('time-custom', false)"> 15 min
                        </label>
                        <label class="btn btn-outline-info">
                            <input type="radio" id="custom-time" value="custom" onclick="toggleCustomInput('time-custom', true)"> Custom
                        </label>
                    </div>
                    <input type="number" id="time-custom" class="form-control mt-2" name="time" placeholder="Enter custom time" style="display:none;">
                </div>
            </div>

            <!-- Submit button to send the form data -->
            <input type="submit" class="btn btn-primary btn-block" value="Submit">
        </form>
    </div>

	<!-- jQuery and Bootstrap JS for proper functioning of buttons and form -->
    <script src="/assets/jquery/jquery.min.js"></script>
    <script src="/assets/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- JavaScript to toggle custom input fields for number of questions and total time -->
    <script>
    window.addEventListener('pageshow', function(event) {
        if (event.persisted || performance.getEntriesByType("navigation")[0].type === "back_forward") {
            console.log("hello");
            window.location.reload();
        }
    });
    
    
    function toggleCustomInput(id, isCustom) {
        const customInput = document.getElementById(id);
        
        // Show custom input field if "Custom" is selected, hide otherwise
        customInput.style.display = isCustom ? 'block' : 'none';
        
        if (!isCustom) {
            customInput.value = ''; // Clear custom input value when switching to predefined options
        }
    }

 
  
  </script>

</body>

</html>