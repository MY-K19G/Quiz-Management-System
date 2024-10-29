<%
if(session.getAttribute("fromUpdatePage")!=null && (boolean )session.getAttribute("fromUpdatePage") )
{	session.removeAttribute("fromUpdatePage");
%>

<%@page import="k19g.quiz.entity.Quiz"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Edit Quiz - Admin</title>
	<link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">
	<style>
		/* Reset styles for all elements */
		* {
			margin: 0;
			padding: 0;
			box-sizing: border-box;
		}

		/* Body styling */
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

		/* Title for the navbar */
		.navbar h2 {
			font-size: 24px;
			margin-bottom: 40px;
			text-align: center;
		}

		/* Link styles for navigation items */
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

		/* Change background color on hover for navigation links */
		.navbar a:hover {
			background-color: #6c5ce7;
		}

		/* Main content styles */
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

		/* Quiz creation container */
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

		.quiz-create-container h1 {
			font-size: 30px;
			color: #2d3436;
			margin-bottom: 30px;
			text-align: center;
		}

		/* Label styling */
		label {
			display: block;
			margin: 5px 0;
			font-size: 16px;
			color: #ffeaea;
		}

		/* Input and textarea styles */
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

		/* Remove default outline and change border color on focus */
		input:focus,
		textarea:focus {
			outline: none;
			border-color: #6c5ce7;
		}

		/* Code editor styling */
		.code-editor {
			background-color: #f9f9f9;
			padding: 15px;
			border-radius: 8px;
			height: 200px;
			overflow-y: auto;
			font-family: "Courier New", Courier, monospace;
			border: 1px solid #dfe6e9;
		}

		/* Change border color on focus for the code editor */
		.code-editor:focus {
			border-color: #6c5ce7;
		}

		/* Submit button styles */
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

		input[type="submit"]:hover {
			background-color: #5b51e7;
		}

		/* Toggle switch styles */
		.toggle-container {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20px;
			flex-wrap: wrap-reverse;
		}

		/* Toggle switch component */
		.toggle-switch {
			position: relative;
			display: inline-block;
			width: 60px;
			height: 34px;
		}

		/* Hide the default checkbox input for the toggle switch */
		.toggle-switch input {
			opacity: 0;
			width: 0;
			height: 0;
		}

		/* Slider styles */
		/* Slider styles for the toggle switch */
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

		/* Circle indicator for the slider */
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

		/* Checked state for slider */
		input:checked+.slider {
			background-color: #3498db;
		}

		/* Move the slider indicator to the right when checked */
		input:checked+.slider:before {
			transform: translateX(26px);
		}

		/* Slider label styles */
		.slider-label {
			font-size: 16px;
			color: #ffeaea;
			margin-right: 10px;
		}

		/* Category input styles */
		.category-input {
			flex-grow: 1;
			width: 100%;
			padding: 10px;
			border: 1px solid #dfe6e9;
			border-radius: 8px;
			transition: border-color 0.3s ease;
			box-sizing: border-box;
		}

		/* Level filter styles */
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

		/* Focus state for level filter */
		.levelFilter:focus {
			border-color: #6c5ce7;
		}

		/* Options container for quiz choices */
		.options-container {
			display: flex;
			flex-direction: column;
		}

		/* Individual option item */
		.option-item {
			display: flex;
			align-items: center;
			position: relative;
		}

		/* Option label styles */
		.option-label {
			font-weight: bold;
			margin-right: 10px;
		}

		.option-item label {
			display: flex;
			align-items: center;
			width: 100%;
		}

		/* Checkbox styles */
		.option-item input[type="checkbox"] {
			display: none;
			margin-right: 10px;
			background-color: #28a745;
			border-color: #28a745;
		}

		/* Styles for the custom checkbox appearance */
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

		/* Checkmark styles */
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

		/* Checked state for custom checkbox */
		.option-item input[type="checkbox"]:checked+.custom-checkbox {
			background-color: #28a745;
			border-color: #28a745;
		}

		/* Show the checkmark when the checkbox is checked */
		.option-item input[type="checkbox"]:checked+.custom-checkbox:after {
			opacity: 1;
		}

		/* Input styles for option text */
		.option-item input[type="text"] {
			flex-grow: 1;
			padding: 10px;
			border: 1px solid #dfe6e9;
			border-radius: 8px;
			transition: border-color 0.3s ease;
			margin: 5px;
		}

		/* Focus state for option text input */
		.option-item input[type="text"]:focus {
			border-color: #6c5ce7;
		}

		/* Tooltip styles */
		.tooltip {
			position: relative;
			cursor: pointer;
			margin-left: 10px;
		}

		/* Tooltip styling for the hidden tooltip text */
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

		/* Show tooltip text when hovering over the tooltip trigger */
		.tooltip:hover .tooltiptext {
			visibility: visible;
			opacity: 1;
		}

		/* Hamburger menu styles */
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

		/* Styling for each bar in the hamburger menu */
		.hamburger div {
			width: 30px;
			height: 3px;
			background-color: #fff;
			margin: 5px 0;
			transition: 0.3s;
		}

		/* Animation for hamburger menu */
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


		/* Default styles (for large screens) */

		/* Responsive styles for medium screens */
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

		/* Responsive styles for smaller tablets */
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

		/* Responsive styles for small screens */
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

			.navbar.hidden~.content {
				margin-left: 10px;
				width: calc(100% - 10px);
			}
		}

		/* Responsive styles for very small screens */
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
	
				<form id="quizForm" action="/update_quiz" method="post">
					<div class="toggle-container">
						<% Quiz question=(Quiz) request.getAttribute("QuestionEntity");
						List<Integer> answers = (List<Integer>) request.getAttribute("answers");
						%>
	
						<input type="hidden" name="id" value="<%= question.getId() %>">
						<input type="text" id="category" class="category-input" placeholder="Enter category" name="category" value="<%= question.getCategory() %>">
	
						<select id="level" class="levelFilter" name="level">
							<option value="EASY" <%=question.getLevel().toString().equalsIgnoreCase("EASY")
										? "selected" : "" %>>EASY</option>
							<option value="MEDIUM" <%=question.getLevel().toString().equalsIgnoreCase("MEDIUM")
										? "selected" : "" %>>MEDIUM</option>
							<option value="HARD" <%=question.getLevel().toString().equalsIgnoreCase("HARD")
										? "selected" : "" %>>HARD</option>
							<option value="EXPERT" <%=question.getLevel().toString().equalsIgnoreCase("EXPERT")
										? "selected" : "" %>>EXPERT</option>
						</select>
	
						<label class="slider-label" id="questionTypeLabel">Theory</label>
						<label class="toggle-switch">
							<input type="checkbox" id="questionTypeToggle" name="type" value="<%= question.getType() %>" <%=question.getType().equalsIgnoreCase("programming") ? "checked" : "" %>>
							<span class="slider"></span>
						</label>
					</div>
	
					<div class="programming-title" id="programmingTitleDiv">
						<label for="programmingTitle">Programming Question Title</label>
						<input type="text" id="programmingTitle" placeholder="Enter programming question title" name="questionTitle" value="<%= question.getQuestionTitle() %>">
					</div>
	
					<label for="question">Question</label>
					<% if (question.getType().equalsIgnoreCase("theory")) { %>
					<textarea id="question" rows="4" placeholder="Enter your question here..." name="theory_question"><%= question.getQuestionTitle() %></textarea>
					<textarea id="codeEditor" class="code-editor" placeholder="// Write your code here..." style="display: none;" name="programming_question"></textarea>
					<% } else { %>
					<textarea id="question" rows="4" placeholder="Enter your question here..." name="theory_question"></textarea>
					<textarea id="codeEditor" class="code-editor" placeholder="// Write your code here..." style="display: none;" name="programming_question"><%= question.getQuestionCode() %></textarea>
					<% } %>
	
					<label>Options</label>
					<div class="options-container">
						<% List<String> options = question.getOptions();
										for (int i = 0; i < options.size(); i++) { char optionLabel=(char) ('A' + i); String
											optionValue=options.get(i); boolean isChecked=answers.contains(i); %>
						<div class="option-item">
							<span class="option-label">
								<%= optionLabel %>.
							</span>
							<label for="option<%= i %>">
								<input type="checkbox" id="option<%= i %>" name="answers" value="<%= optionValue %>" <%=isChecked ? "checked" : "" %>>
								<span class="custom-checkbox"></span>
								<input type="text" id="optionText<%= i %>" placeholder="Option <%= optionLabel %>" name="options" value="<%= optionValue %>">
							</label>
							<div class="tooltip">?
								<span class="tooltiptext">Check this box to mark this option as
									correct</span>
							</div>
						</div>
						<% } %>
					</div>
	
					<label for="explanation">Explanation</label>
					<textarea id="explanation" rows="3" placeholder="Provide explanation for the correct answer..." name="explanation"><%= question.getExplanation() %></textarea>
	
					<input type="submit" value="Update Question">
				</form>
			</div>
		</div>


	<script>
		// Get DOM elements for question type toggle and related UI components
		const questionTypeToggle = document.getElementById('questionTypeToggle');
		const questionTextArea = document.getElementById('question');
		const codeEditor = document.getElementById('codeEditor');
		const questionTypeLabel = document.getElementById('questionTypeLabel');
		const programmingTitleDiv = document.getElementById("programmingTitleDiv");

		// Toggle the UI based on the question type when the checkbox is changed
		questionTypeToggle.addEventListener('change', function () {
			if (this.checked) {
				this.value = 'Programming'; // Set value to 'Programming'
				programmingTitleDiv.style.display = 'block'; // Show programming title input
				questionTypeLabel.textContent = 'Programming'; // Update label to 'Programming'
				questionTextArea.style.display = 'none'; // Hide theory question text area
				codeEditor.style.display = 'block'; // Show code editor
			} else {
				this.value = 'theory'; // Set value to 'theory'
				programmingTitleDiv.style.display = 'none'; // Hide programming title input
				questionTypeLabel.textContent = 'Theory'; // Update label to 'Theory'
				questionTextArea.style.display = 'block'; // Show theory question text area
				codeEditor.style.display = 'none'; // Hide code editor
			}
		});

		// Function to toggle the visibility of the navbar
		function toggleNavbar() {
			const navbar = document.querySelector('.navbar');
			navbar.classList.toggle('hidden'); // Toggle the hidden class for navbar visibility

			const hamburger = document.querySelector('.hamburger');
			hamburger.classList.toggle('open'); // Add animation to hamburger icon
		}

		// Simulate receiving question type from the backend
		const questionTypeFromBackend = '<%= question.getType() %>'; // Example backend value: can be 'programming' or 'theory'

		// Function to set the initial state based on the question type received from backend
		function toggleQuestionType(type) {
			if (type.toLowerCase() === 'programming') { // Ensure case-insensitive check
				questionTypeToggle.setAttribute('value', questionTypeFromBackend); // Set toggle value
				questionTypeToggle.checked = true; // Check the toggle
				questionTypeLabel.textContent = 'Programming'; // Update label to 'Programming'
				questionTextArea.style.display = 'none'; // Hide theory question text area
				programmingTitleDiv.style.display = 'block'; // Show programming title input
				codeEditor.style.display = 'block'; // Show code editor
			} else {
				questionTypeToggle.checked = false; // Uncheck the toggle
				questionTypeLabel.textContent = 'Theory'; // Update label to 'Theory'
				programmingTitleDiv.style.display = 'none'; // Hide programming title input
				questionTextArea.style.display = 'block'; // Show theory question text area
				codeEditor.style.display = 'none'; // Hide code editor
			}
		}

		// Check the initial question type from backend and apply the toggle
		toggleQuestionType(questionTypeFromBackend);

		// Function to check a specific option by its number
		function checkOption(optionNumber) {
			const optionId = 'option' + optionNumber; // Construct the option ID
			const checkbox = document.getElementById(optionId);
			if (checkbox) {
				checkbox.checked = true; // Check the checkbox
			}
		}

		// Function to check all specified options
		function checkAllOptions(optionArray) {
			const checkboxes = document.querySelectorAll('input[type="checkbox"]');
			checkboxes.forEach((checkbox) => {
				// checkbox.checked = false; // Uncomment to uncheck all first (if needed)
			});

			optionArray.forEach((optionNumber) => {
				checkOption(optionNumber); // Check each specified option
			});
		}

		//  Receiving checked options from the backend
		const input = <%= answers %>;
		checkAllOptions(input); // Check the specified options

		//Validation form inputs
		document.getElementById("quizForm").addEventListener("submit", function (event) {
			const category = document.getElementById("category").value.trim();
			const level = document.getElementById("level").value;
			const questionType = document.getElementById("questionTypeToggle").checked;
			const programmingTitle = document.getElementById("programmingTitle").value.trim();
			const question = document.getElementById("question").value.trim();
			const codeEditor = document.getElementById("codeEditor").value.trim();
			const explanation = document.getElementById("explanation").value.trim();

			const optionTexts = [
				document.getElementById("optionText0").value,
				document.getElementById("optionText1").value,
				document.getElementById("optionText2").value,
				document.getElementById("optionText3").value
			];
			const checkboxes = [
				document.getElementById("option0").checked,
				document.getElementById("option1").checked,
				document.getElementById("option2").checked,
				document.getElementById("option3").checked
			];


			let isValid = true;
			let errorMessage = "";

			console.log(category);
			// Check category
			if (!category) {
				isValid = false;
				errorMessage += "Category is required.\n";

			}
			console.log("0000 " + level);
			// Check level
			if (!level) {
				isValid = false;
				console.log(level);
				errorMessage += "Please select a level.\n";
			}

			// Check question type specific fields
			if (questionType && !programmingTitle) {
				errorMessage += "Programming question title is required.\n";
				if (!codeEditor) {
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
				errorMessage += "Please fill in all options.\n";
			}

			// Check if at least one checkbox is selected as correct
			const isAnswerSelected = checkboxes.some(checked => checked === true);
			if (!isAnswerSelected) {
				isValid = false;
				errorMessage += "Please select at least one correct answer.\n";
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

		// Log a message when the form is submitted
		document.getElementById("quizForm").addEventListener("submit", function (e) {
			console.log("Quiz updated!"); // Log submission event for debugging
		});

	</script>
</body>

</html>
<%} else{ response.sendRedirect("/403"); } %>