<%@ include file="loader.jsp" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Create Quiz - Admin</title>
        
		<link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">
        <style type="text/css">
            /* Reset some basic styles for consistency */
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            /* Body styles for background and overall page layout */
            body {
                font-family: "Poppins", sans-serif;
                background: linear-gradient(to right, #2c3e50, #3498db);
                background-size: cover;
                background-repeat: no-repeat;
                overflow-x: hidden;
                display: flex;
                min-height: 100vh;
            }

            /* Navbar Styles */
            /* A fixed, full-height sidebar for navigation with hover effects */
            .navbar {
                background-color: #2d3436;
                color: #fff;
                width: 220px;
                padding: 30px 20px;
                height: 100vh;
                position: fixed;
                top: 0;
                left: 0;
                display: flex;
                flex-direction: column;
                justify-content: flex-start;
                align-items: flex-start;
                box-shadow: 2px 0 10px rgba(0, 0, 0, 0.2);
            }

            /* Styling the header inside the navbar */
            .navbar h2 {
                font-size: 24px;
                margin-bottom: 40px;
                text-align: center;
            }
            
            /* Links inside the navbar with hover effect */
            .navbar a {
                display: block;
                color: #fff;
                text-decoration: none;
                font-size: 18px;
                margin-bottom: 15px;
                padding: 10px 15px;
                width: 100%;
                border-radius: 8px;
                transition: background-color 0.3s ease;
            }

            .navbar a:hover {
                background-color: #6c5ce7;
            }

            /* Main Content Styles */
            /* The main content will sit next to the navbar and take up the remaining width of the screen. It is centered vertically and horizontally. */
            .content {
                margin-left: 240px;
                padding: 40px;
                width: calc(100% - 240px);
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
                color: #ffeaea;
            }

            /* Container for creating quizzes, styled with padding and shadows */
            .quiz-create-container {
                background-color: #706591;
                padding: 40px 50px;
                border-radius: 12px;
                box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
                width: 100%;
                max-width: 800px;
                text-align: left;
                position: relative;
            }

            /* Title for the quiz form */
            .quiz-create-container h1 {
                font-size: 30px;
                color: #2d3436;
                margin-bottom: 30px;
                text-align: center;
            }

            /* Form labels, placed above inputs */
            label {
                display: block;
                margin: 5px 0;
                font-size: 16px;
                color: #ffeaea;
            }

            /* Basic styles for text inputs and textarea elements */
            input,
            textarea {
                width: 100%;
                padding: 12px;
                margin: 10px 0 20px;
                border: 1px solid #dfe6e9;
                border-radius: 8px;
                font-size: 16px;
                transition: border-color 0.3s ease;
            }

            /* Change border color when input or textarea is focused */
            input:focus,
            textarea:focus {
                outline: none;
                border-color: #6c5ce7;
            }

            /* Container to hold options for questions */
            .options-container {
                display: flex;
                flex-direction: column;
            }

            /* Code editor style area for programming questions */
            .code-editor {
                background-color: #f9f9f9;
                padding: 15px;
                border-radius: 8px;
                height: 200px;
                overflow-y: auto;
                font-family: "Courier New", Courier, monospace;
                border: 1px solid #dfe6e9;
            }

            /* Focus state for code editor (changes border color) */
            .code-editor:focus {
                border-color: #6c5ce7;
            }

            /* Hide this title by default, for future use */
            .programming-title {
                display: none;
                margin-bottom: 20px;
            }

            /* Styles for submit button */
            input[type="submit"] {
                width: 100%;
                padding: 14px;
                background-color: #6c5ce7;
                border: none;
                color: white;
                font-size: 16px;
                cursor: pointer;
                border-radius: 8px;
                transition: background-color 0.3s ease;
            }

            /* Submit button hover effect */
            input[type="submit"]:hover {
                background-color: #5b51e7;
            }

            /* Toggle switch container */
            .toggle-container {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                flex-wrap: wrap-reverse;
            }

            /* Toggle switch styles */
            .toggle-switch {
                position: relative;
                display: inline-block;
                width: 60px;
                height: 34px;
            }

            .toggle-switch input {
                opacity: 0;
                width: 0;
                height: 0;
            }

            /* Slider for toggle switch */
            .slider {
                position: absolute;
                cursor: pointer;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background-color: #ccc;
                transition: 0.4s;
                border-radius: 34px;
            }

            /* Circle inside the slider */
            .slider:before {
                position: absolute;
                content: "";
                height: 26px;
                width: 26px;
                left: 4px;
                bottom: 4px;
                background-color: white;
                transition: 0.4s;
                border-radius: 50%;
            }
            
            /* Change background and move the circle when checked */
            input:checked + .slider {
                background-color: #3498db;
            }

            input:checked + .slider:before {
                transform: translateX(26px);
            }

            /* Style for the labels in a slider component, setting font size, color, and margin */
            .slider-label {
                font-size: 16px;
                color: #ffeaea;
                margin-right: 10px;
            }

            /* Style for the category input field */
            .category-input {
                flex-grow: 1; 
                width: 100%; 
                padding: 10px; 
                border: 1px solid #dfe6e9;
                border-radius: 8px;
                transition: border-color 0.3s ease;
                box-sizing: border-box;
            }

            /* Style for the level filter dropdown */
            .levelFilter {
                padding: 12px;
                font-size: 16px;
                border: 1px solid #dfe6e9;
                border-radius: 8px;
                transition: border-color 0.3s ease;
                flex-grow: 1; 
                width: 100%;
                box-sizing: border-box; 
            }

            /* Changes border color on focus for the level filter */
            .levelFilter:focus {
                border-color: #6c5ce7;
            }

            /* Style for each option item containing checkbox and label */
            .option-item {
                display: flex;
                align-items: center;
            }

            /* Style for the option label */
            .option-label {
                font-weight: bold;
                margin-right: 10px;
            }

            /* Style for the label and checkbox container */
            .option-item label {
                display: flex;
                align-items: center;
                width: 100%;
                cursor: pointer;
            }

            /* Hides the default checkbox */
            .option-item input[type="checkbox"] {
               display: none;
            }

            /* Custom checkbox styling */
            .option-item .custom-checkbox {
                display: inline-block;
                width: 20px;
                height: 20px;
                background-color: #fff;
                border: 2px solid #dfe6e9;
                border-radius: 4px;
                margin-right: 10px;
                position: relative;
                transition: background-color 0.3s, border-color 0.3s;
                cursor: pointer;
            }

            /* Checkmark (pseudo-element) styling for custom checkbox */
            .option-item .custom-checkbox:after {
                content: "";
                position: absolute;
                width: 6px;
                height: 10px;
                border: solid #fff;
                border-width: 0 2px 2px 0;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%) rotate(45deg);
                opacity: 0;
                transition: opacity 0.3s;
            }

            /* Style for checked state of custom checkbox */
            .option-item input[type="checkbox"]:checked + .custom-checkbox {
                background-color: #28a745;
                border-color: #28a745;
            }

            /* Displays the checkmark when the checkbox is checked */
            .option-item input[type="checkbox"]:checked + .custom-checkbox:after {
                opacity: 1;
            }

            /* Style for text input within option item */
            .option-item input[type="text"] {
                flex-grow: 1;
                padding: 10px;
                border: 1px solid #dfe6e9;
                border-radius: 8px;
                transition: border-color 0.3s ease;
                margin: 5px;
            }

            /* Changes border color on focus for text input */
            .option-item input[type="text"]:focus {
                border-color: #6c5ce7;
            }

            /* Tooltip container */
            .tooltip {
                position: relative;
                cursor: pointer;
                margin-left: 10px;
            }

            /* Hidden tooltip text */
            .tooltip .tooltiptext {
                visibility: hidden;
                width: 200px;
                background-color: #6c5ce7;
                color: #fff;
                text-align: center;
                border-radius: 5px;
                padding: 8px;
                position: absolute;
                z-index: 1;
                bottom: 125%;
                left: 50%;
                transform: translateX(-50%);
                opacity: 0;
                transition: opacity 0.3s;
            }

            /* Show the tooltip text on hover */
            .tooltip:hover .tooltiptext {
                visibility: visible;
                opacity: 1;
            }

            /* Hamburger menu (hidden by default) */
            .hamburger {
                display: none; 
                background-color: transparent;
                border: none;
                cursor: pointer;
                position: absolute;
                top: 20px;
                left: 20px;
                z-index: 1000;
            }

            /* Hamburger bars (three stacked divs) */
            .hamburger div {
                width: 30px;
                height: 3px;
                background-color: #fff;
                margin: 5px 0;
                transition: 0.3s;
            }

            /* Add animation for hamburger menu when open */
              /* Rotate and move the first bar to create an "X" shape */
            .hamburger.open div:nth-child(1) {
                transform: rotate(-45deg) translate(-5px, 6px);
            }
			
			/* Hide the second bar when the menu is open */
            .hamburger.open div:nth-child(2) {
                opacity: 0;
            }
			/* Rotate and move the third bar to create an "X" shape */
            .hamburger.open div:nth-child(3) {
                transform: rotate(45deg) translate(-5px, -6px);
            }

            /* Notification styles */
            #notification {
                position: fixed;
                top: 20px;
                right: 20px;
                background-image: linear-gradient(to left, #99f449 0%, #05ebb5 100%);
                color: #0a0c61;
                padding: 16px;
                border-radius: 4px;
                display: none;
                max-width: 300px;
            }

            /* Close button for notification */
            #close-btn {
                float: right;
                cursor: pointer;
                background: none;
                border: none;
                color: red;
                font-size: 18px;
            }

            /* Progress bar inside the notification */
            #progress-bar {
                width: 100%;
                height: 5px;
                background-color: #45a049;
                position: absolute;
                bottom: 0;
                left: 0;
                transition: width 0.1s linear;
            }

            /* Default styles (for large screens) */

            /* Responsive Styles */
            @media (max-width: 1200px) {
                /* For medium screens like tablets in landscape mode */
                .content {
                    margin-left: 220px;
                    padding: 30px;
                    width: calc(100% - 220px);
                }

                .quiz-create-container {
                    padding: 30px;
                    max-width: 800px;
                }

                .navbar {
                    width: 200px;
                    padding: 20px;
                }
            }

            @media (max-width: 992px) {
                /* For smaller tablets and medium-sized devices */
                .content {
                    margin-left: 200px;
                    padding: 20px;
                    width: calc(100% - 200px);
                }

                .quiz-create-container {
                    padding: 20px;
                    max-width: 550px;
                }

                .navbar {
                    width: 180px;
                    padding: 20px;
                }

                .navbar h2 {
                    font-size: 22px;
                }

                .navbar a {
                    font-size: 16px;
                }
            }

            /* Media Queries for Responsive Design */
            @media (max-width: 768px) {
                .navbar {
                    width: 125px; 
                    padding: 10px 0;
                    transition: width 0.3s ease;
                    z-index: 1001;
                    height: -webkit-fill-available;
                }

                .navbar h2 {
                    display: none; 
                }

                .navbar a {
                    font-size: 16px;
                    padding: 8px 10px;
                }

                .content {
                    margin-left: 70px;
                    width: calc(100% - 70px);
                }

                .hamburger {
                    display: block;
                    cursor: pointer;
                    padding: 10px;
                    margin-left: 3em;
                    margin-top: -2em;
                    z-index: 1003;
                }

                .navbar.hidden {
                    width: 0; 
                    padding: 0; 
                    display: none;
                }

                .navbar.hidden ~ .content {
                    margin-left: 10px;
                    width: calc(100% - 10px); 
                }
            }

            @media (max-width: 576px) {
                .navbar a {
                    font-size: 14px; 
                }

                .quiz-create-container {
                    padding: 20px; 
                    margin-top: 17px;
                }

                input[type="submit"] {
                    padding: 10px; 
                }

                .hamburger {
                    display: block; 
                    margin-left: 13em;
                    margin-top: -2em;
                }
            }

            /* Show the hamburger menu on smaller screens */

            @media (max-width: 400px) {
                .hamburger {
                    display: block; 
                    margin-left: 9em;
                    margin-top: -2em;
                }
            }
            @media (max-width: 375px) {
                .hamburger {
                    display: block; 
                    margin-left: 11em;
                    margin-top: -2em;
                }
            }
            @media (max-width: 320px) {
                .hamburger {
                    display: block; 
                    margin-left: 10em;
                    margin-top: -2em;
                }
            }
        </style>
    </head>
    <body>
        <!-- Hamburger Menu Button -->
        <button class="hamburger" onclick="toggleNavbar()">
            <div></div>
            <div></div>
            <div></div>
        </button>

        <div class="navbar">
            <h2>Quiz Management System</h2>
            <a href="/create">Create</a>
            <a href="/update">Update</a>
            <a href="/delete">Delete</a>
        </div>

        <div class="content">
            <div class="quiz-create-container">
                <h1>Create New Quiz Question</h1>

                <form id="quizForm" action="/save_quiz" method="post">
                    <div class="toggle-container">
                        <input type="text" class="category-input" id="category" placeholder="Enter category (*required)" name="category"  />
                        <select class="levelFilter" name="level" id="level">
                            <option value="" style="display: none;" >Select level</option>
                            <option value="EASY">EASY</option>
                            <option value="MEDIUM">MEDIUM</option>
                            <option value="HARD">HARD</option>
                            <option value="EXPERT">EXPERT</option>
                        </select>
                        <label class="slider-label" id="questionTypeLabel">Theory</label>
                        <label class="toggle-switch">
                            <input type="checkbox" id="questionTypeToggle" name="type" value="Programming" />
                            <span class="slider"></span>
                        </label>
                    </div>

                    <div class="programming-title" id="programmingTitleDiv">
                        <label for="programmingTitle">Programming Question Title (*required)</label>
                        <input type="text" id="programmingTitle" placeholder="Enter programming question title" name="questionTitle" />
                    </div>

                    <label for="question">Question (*required)</label>
                    <textarea id="question" rows="4" placeholder="Enter your theory question here..." name="theory_question"></textarea>
                    <textarea id="codeEditor" class="code-editor" placeholder="// Write your code here..." style="display: none;" name="programming_question"></textarea>

                    <label>Options (*required)</label>
                    <div class="options-container">
                        <div class="option-item">
                            <span class="option-label">A.</span>
                            <label for="option1">
                                <input type="checkbox" id="option1" name="answers" value="0" />
                                <span class="custom-checkbox"></span>
                                <input type="text" placeholder="Option 1" id="optionText1" name="options"  />
                            </label>
                            <div class="tooltip">
                                ?
                                <span class="tooltiptext">Check this box to mark this option as correct</span>
                            </div>
                        </div>
                        <div class="option-item">
                            <span class="option-label">B.</span>
                            <label for="option2">
                                <input type="checkbox" id="option2" name="answers" value="1" />
                                <span class="custom-checkbox"></span>
                                <input type="text" placeholder="Option 2" id="optionText2" name="options"  />
                            </label>
                            <div class="tooltip">
                                ?
                                <span class="tooltiptext">Check this box to mark this option as correct</span>
                            </div>
                        </div>
                        <div class="option-item">
                            <span class="option-label">C.</span>
                            <label for="option3">
                                <input type="checkbox" id="option3" name="answers" value="2" />
                                <span class="custom-checkbox"></span>
                                <input type="text" placeholder="Option 3" id="optionText3" name="options"  />
                            </label>
                            <div class="tooltip">
                                ?
                                <span class="tooltiptext">Check this box to mark this option as correct</span>
                            </div>
                        </div>
                        <div class="option-item">
                            <span class="option-label">D.</span>
                            <label for="option4">
                                <input type="checkbox" id="option4" name="answers" value="3" />
                                <span class="custom-checkbox"></span>
                                <input type="text" placeholder="Option 4" id="optionText4" name="options"  />
                            </label>
                            <div class="tooltip">
                                ?
                                <span class="tooltiptext">Check this box to mark this option as correct</span>
                            </div>
                        </div>
                    </div>

                    <label for="explanation">Explanation (*required)</label>
                    <textarea id="explanation" rows="3" placeholder="Provide explanation for the correct answer..." name="explanation" ></textarea>

                    <input type="submit" value="Add Question" />
                </form>
            </div>
            <div id="notification">
                <button id="close-btn">&times;</button>
                <p>The quiz has been recorded in the database.</p>
                <div id="progress-bar"></div>
            </div>
        </div>

        <script>
        // Event listener for when the question type toggle switch changes
        document.getElementById("questionTypeToggle").addEventListener("change", function() {
            let isProgramming = this.checked;
            document.getElementById("questionTypeLabel").innerText = isProgramming ? "Programming" : "Theory";
            document.getElementById("codeEditor").style.display = isProgramming ? "block" : "none";
            document.getElementById("question").style.display = isProgramming ? "none" : "block";
            document.getElementById("programmingTitleDiv").style.display = isProgramming ? "block" : "none";
        });
	
   		// Get references to the notification element, close button, and progress bar
        const notification = document.getElementById('notification');
        const closeBtn = document.getElementById('close-btn');
        const progressBar = document.getElementById('progress-bar');

        let timeoutId;
        
	    // Function to display the notification
        function showNotification() {
            notification.style.display = 'block';
            progressBar.style.width = '100%';

            timeoutId = setTimeout(() => {
                hideNotification();
            }, 5000);

            updateProgressBar();
        }

     	// Function to hide the notification
        function hideNotification() {
            notification.style.display = 'none';
            clearTimeout(timeoutId);
        }

        // Function to decrease the width of the progress bar over time
        function updateProgressBar() {
            let width = 100;
            const interval = setInterval(() => {
                width -= 2;
                progressBar.style.width = width + '%';
                if (width <= 0) {
                    clearInterval(interval);
                }
            }, 100);
        }
        
		//Function to toggle nav bar
        function toggleNavbar() {
            const navbar = document.querySelector('.navbar');
            navbar.classList.toggle('hidden'); // Toggle the hidden class

            const hamburger = document.querySelector('.hamburger');
            hamburger.classList.toggle('open'); // Add animation to hamburger
        }

        closeBtn.addEventListener('click', hideNotification);
		
        //Validate form inputs
        document.getElementById("quizForm").addEventListener("submit", function(event) {
            const category = document.getElementById("category").value.trim();
            const level = document.getElementById("level").value;
            const questionType = document.getElementById("questionTypeToggle").checked;
            const programmingTitle = document.getElementById("programmingTitle").value.trim();
            const question = document.getElementById("question").value.trim();
            const codeEditor = document.getElementById("codeEditor").value.trim();
            const explanation = document.getElementById("explanation").value.trim();
            
            const optionTexts = [
                document.getElementById("optionText1").value,
                document.getElementById("optionText2").value,
                document.getElementById("optionText3").value,
                document.getElementById("optionText4").value
            ];
            const checkboxes = [
                document.getElementById("option1").checked,
                document.getElementById("option2").checked,
                document.getElementById("option3").checked,
                document.getElementById("option4").checked
            ];

            let isValid = true;
            let errorMessage = "";

            // Check category
            if (!category) {
                isValid = false;
                errorMessage += "Category is required.\n";
               
            }
            console.log("0000 "+level);
            // Check level
            if (!level) {
                isValid = false;
                console.log(level);
                errorMessage += "Please select a level.\n";
            }
            
            // Check question type specific fields
            if (questionType && !programmingTitle) {
            	 errorMessage += "Programming question title is required.\n";
            	if(!codeEditor){
            		errorMessage += "Programming question code is required.\n";
            		isValid = false;
            	}
            	 isValid = false;
               
            } else if (!questionType && !question) {
                isValid = false;
                errorMessage += "Theory question is required.\n";
            }
            
         // Check if at least one option text is filled
            const areAllOptionsFilled = optionTexts.every(option => option.trim() !== "");
            if (!areAllOptionsFilled) {
            	isValid = false;
            	 errorMessage +="Please fill in all options.\n";
            }

            // Check if at least one checkbox is selected as correct
            const isAnswerSelected = checkboxes.some(checked => checked === true);
            if (!isAnswerSelected) {
            	isValid = false;
            	 errorMessage +="Please select at least one correct answer.\n";
            }

            // Check explanation
            if (!explanation) {
                isValid = false;
                errorMessage += "Explanation is required.\n";
            }

            // If the form is not valid, prevent submission and show errors
            if (!isValid) {
                event.preventDefault();
                alert(errorMessage);
            }
        });

        <%
        
        Boolean isQuizInserted = (session.getAttribute("isQuizInserted") != null) ?
            (Boolean) session.getAttribute("isQuizInserted") :
            false; 
        %>
        let isQuizInserted = <%= isQuizInserted %> ;

        <% session.removeAttribute("isQuizInserted"); %>
        // Check the boolean value and show notification if true
        if (isQuizInserted) {
            showNotification();
        }
        </script>
    </body>
</html>
