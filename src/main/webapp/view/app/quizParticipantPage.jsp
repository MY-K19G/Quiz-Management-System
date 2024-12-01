<%
boolean quiz_access=(boolean)session.getAttribute("quiz_access");
if(quiz_access){ 
quiz_access=false;%>
<%@ include file="../loader.jsp" %>
<%@page import="k19g.quiz.DTO.QuizDTO"%>
<%@page import="java.util.List"%>
<%@ page isELIgnored="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!DOCTYPE html>
<html lang="en">

<head>
	<!-- Meta tags for character encoding and responsive layout -->
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Programming Quiz</title>
		
	<link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">
		
	<!-- Prism.js styles for syntax highlighting -->
	<link rel="stylesheet" href="/assets/prism/prism.min.css">
	<link rel="stylesheet" href="/assets/prism/prism-okaidia.min.css">

	<style>
		/* General body styling */
		body {
			font-family: Arial, sans-serif;
			background-color: #f4f4f4;
			padding: 20px;
			display: flex;
			flex-direction: row-reverse;
		    overflow: hidden;
		}

		.quiz-container {
			background-color: rgba(255, 255, 255, 0.8);
			padding: 20px;
			border-radius: 5px;
			box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
			max-width: 600px;
			margin: 0 auto;
			position: relative;
			z-index: 1;
			flex: 1;
			height: 600px;
			overflow-y: auto;
		}

		.background {
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			z-index: -1;
			background-size: cover;
			background-position: center;
		}

		/* Code block styling */
		pre {
			padding: 10px;
			border-radius: 5px;
			background-color: #2d2d2d;
			color: #f8f8f2;
			transition: transform 0.2s;
		}

		/* Hover effect for code blocks */
		pre:hover {
			transform: scale(1.05);
		}

		/* Theory question styling */
		.theory-question {
			background-color: #e8f0fe;
			border-left: 5px solid #4285f4;
			padding: 10px;
			margin: 20px 0;
			border-radius: 5px;
		}

		/* Options container styling */
		.options {
			margin: 20px 0;
			display: flex;
			flex-direction: column;
		}

		/* Individual option button styling */
		.option {
			background-color: #eaeaea;
			border: none;
			padding: 10px;
			margin: 5px 0;
			border-radius: 5px;
			cursor: pointer;
			display: flex;
			align-items: center;
			transition: background-color 0.3s;
		}

		/* Selected option styling */
		.option.selected {
			color: black;
		}

		/* Option label styling */
		.option-label {
			font-weight: bold;
			margin-right: 10px;
		}

		/* General button styling */
		.button {
			padding: 10px 15px;
			background-color: #007bff;
			color: white;
			border: none;
			border-radius: 5px;
			cursor: pointer;
			margin: 5px;
		}

		/* Hover effect for buttons */
		.button:hover {
			background-color: #0056b3;
		}

		/* Submit button styling */
		.submit-button {
			padding: 10px 15px;
			background-color: #6f42c1;
			color: white;
			border: none;
			border-radius: 5px;
			cursor: pointer;
			margin-left: auto;
		}

		/* Hover effect for submit button */
		.submit-button:hover {
			background-color: #5a32a3;
		}

		/* Button container styling */
		.button-container {
			display: flex;
			justify-content: space-between;
			margin-top: 20px;
		}

		/* Timer Styles */
		#timer {
			text-align: center;
			margin-bottom: 20px;
		}

		svg {
			transform: rotate(-90deg);
		}

		circle {
			fill: none;
			stroke-width: 10;
		}

		.bg-circle {
			stroke: #eaeaea;
			/* Background Circle Color */
		}

		.progress-circle {
			stroke: #f0ad4e;
			transition: stroke-dashoffset 1s linear;
		}

		/* Sidebar Styles */
		.sidebar {
			width: 200px;
			margin-left: 20px;
			background-color: rgba(255, 255, 255, 0.9);
			border-radius: 5px;
			padding: 15px;
			box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
			position: relative;
			z-index: 2;
		}

		.sidebar h3 {
			margin-top: 0;
		}

		.answered {
			color: green;
		}

		.unanswered {
			color: red;
		}

		/* Status Box Styling */
		#question-status {
			display: flex;
			flex-wrap: wrap;
			gap: 10px;
			padding: 10px;
			justify-content: center;
			max-height: calc(50px * 3 + 20px);
			overflow-y: auto;
		}

		.status-box {
			width: 50px;
			height: 50px;
			display: flex;
			justify-content: center;
			align-items: center;
			background-color: #eaeaea;
			border-radius: 50%;
			text-align: center;
			font-weight: bold;
			color: white;
			position: relative;
		}

		.status-box.answered {
			background-color: green;
		}

		.status-box.unanswered {
			background-color: red;
		}

		/* Add numbers in the middle of circles */
		.status-box::after {
			content: attr(data-number);
			position: absolute;
			font-size: 1.2rem;
		}

		/* Add scrollbar styles for more than 9 circles */
		#question-status::-webkit-scrollbar {
			width: 8px;
		}

		#question-status::-webkit-scrollbar-thumb {
			background-color: #888;
			border-radius: 5px;
		}

		#question-status::-webkit-scrollbar-thumb:hover {
			background-color: #555;
		}
		
		.navbar{
			display: none;
		}
		
		.disable{
			display: none;
		}
			

		@media screen and (max-width: 768px) {
			body, html {
            margin: 0;
            padding: 0;
            overflow-x: hidden;
            font-family: Arial, sans-serif;
            flex-direction: column;
        	}

			 /* Sidebar styling */
	        .sidebar-container {
	        	display: flex;
				flex-direction: column;
				align-items: center;
	            position: fixed;
	            top: 0;
	            left: -250px; 
	            width: 250px;
	            height: 100%;
	            background-color: #ff7c00;
	            border-radius: 10px;
	            overflow-y: auto;
	            transition: left 0.3s ease;
	            padding-top: 15px;
	            z-index: 10;
	        }
	        
	        .sidebar-container.active {
	            left: 0; 
	        }
	        
			.sidebar {
   				width: 75%;
			    display: flex;
			    flex-direction: column;
			    justify-content: center;
			    align-items: center;
			    margin: 10px auto;
			}
			
			/* Sidebar links */
	        .sidebar-container a {
	            display: block;
	            padding: 15px 20px;
	            color: white;
	            text-decoration: none;
	            font-size: 18px;
	            transition: background 0.2s;
	        }

	        .sidebar-container a:hover {
	            background-color: #555;
	        }
	        
	        /* Main content styling */
	        .quiz-container {
	            padding: 20px;
	            transition: margin-left 0.3s ease;
	            width: 80%;
	            overflow: hidden; 
	        }
	        
	         .quiz-container.shift {
	            margin-left: 250px; 
	        }
	        
			.button-container {
				flex-direction: column;
				align-items: center;
			}

			.button,
			.submit-button {
				width: 100%;
				margin: 10px 0;
			}

			.option {
				font-size: 14px;
			}
				
			/* Navbar styling */
        	.navbar {
	            display: flex;
	            align-items: center;
	            justify-content:center;
	            padding: 10px 20px;
	            color: blue;
       		 }

        	/* Hamburger button styling */
	        .hamburger {
	            font-size: 24px;
	            cursor: pointer;
	        }

	        /* Overlay styling */
	        .overlay {
	            position: fixed;
	            top: 0;
	            left: 0;
	            width: 100%;
	            height: 100%;
	            background: rgba(0, 0, 0, 0.5);
	            z-index: 5;
	            display: none;
	            transition: opacity 0.3s ease;
	        }
	
	        .overlay.active {
	            display: block;
	        }
		}
		
		/* Styling for each bar */
        .ani, .ani-theory, pre {
   			 opacity: 0;
   			 transform: translateX(100%);
			}



	        /* Animation for sliding in from right to left */
	    @keyframes slideToLeft {
	    	0% { transform: translate3d(100%, 0, 0); opacity: 0; }
	    	100% { transform: translate3d(0, 0, 0); opacity: 1; }
			}


		.ani-theory{
        	animation: slideToLeft 1s ease-out forwards;
        }
        pre {
            animation: slideToLeft 1.5s ease-in-out forwards; 
            animation-delay: 0.2s;
        }
        
        /* Assigning different speeds and smoothness to each bar */
        .ani:nth-child(1) {
            animation: slideToLeft 1s ease-out forwards; 
            animation-delay: 0.3s;
        }

        .ani:nth-child(2) {
            animation: slideToLeft 1.5s ease-in-out forwards; 
            animation-delay: 0.4s;
        }
        
        .ani:nth-child(3) {
            animation: slideToLeft 1.5s ease-in-out forwards; 
            animation-delay: 0.5s;
        }
        
        .ani:nth-child(4) {
            animation: slideToLeft 1.5s ease-in-out forwards; 
            animation-delay: 0.6s;
        }
        
        
	/* Container for each option */
	.option {
	    border: 1px solid #ddd;
	    border-radius: 5px;
	    cursor: pointer;
	    transition: background-color 0.3s, border-color 0.3s;
	    display: flex;
	    align-items: center;
	}
	
	/* Animation effect */
	 
	/* Container for each option */
	.option {
	    position: relative; 
	    border: 1px solid #8f7070b5;
	    border-radius: 5px;
	    cursor: pointer;
	    transition: background-color 0.3s, border-color 0.3s;
	    display: flex;
	    align-items: center;
	    overflow: hidden; 
	}
	
	/* Animation effect */
	.option::after {
	    content: "";
	    position: absolute;
	    top: 0;
	    left: 100%;
	    bottom: 0;
	    right: 0;
	    background-color: #ffcc00;
	    transition: left 1s ease;
	    z-index: 1;
	}
	
	/* When selected, slide background into view */
	.option.selected::after {
		background-color: #ffcc00;
	    left: 0;
	}
	
	/* Checkbox style */
	.option-checkbox {
	    margin-right: 10px;
	    width: 18px;
	    height: 18px;
	    transition: all 0.3s ease;
	    position: relative;
	    z-index: 2;
	}
	
	/* Label for the option, e.g., "A)" */
	.option-label {
	    font-weight: bold;
	    margin-right: 8px;
	    position: relative;
	    z-index: 2;
	}
	
	/* Text for the option */
	.option span {
	    font-size: 16px;
	    position: relative;
	    z-index: 2;
	}
	
	/* Highlight selected option */
	.option.selected {
	    border-color: #ff7c00;
	}
	
	/* Customize checkbox when option is selected */
	.option.selected .option-checkbox {
	    accent-color: #00796b;
	    transform: translateX(5px);
	}
	
	/* Checkbox checked state */
	.option-checkbox:checked {
	    transition: background-color 0.3s ease, transform 0.3s ease;
	    background-color: #00796b;
	}
	
	/* Option text color when selected */
	.option.selected span {
	    color: #00796b;
	}



	</style>

</head>

<body>

	<!-- Background image div -->
	<div class="background" id="background"></div>
	
	 <!-- Navbar with hamburger button -->
    <div class="navbar">
        <span class="hamburger" onclick="toggleSidebar()">☰</span>
    </div>
	
	<!-- Sidebar -->
	<div class="sidebar-container">
		<div id="timer" class="sidebar">
			<svg width="100" height="100">
				<circle class="bg-circle" cx="50" cy="50" r="45"></circle>
				<circle class="progress-circle" cx="50" cy="50" r="45" stroke-dasharray="283" stroke-dashoffset="0">
				</circle>
			</svg>
			<div>
				Time Remaining: <br><span id="time"></span>
			</div>
		</div>

		<div class="sidebar">
			<h3>Quiz Status</h3>
			<p>
				Total Questions: <span id="totalQuestions">0</span>
			</p>
			<p>
				Answered: <span id="answeredQuestions" class="answered">0</span>
			</p>
			<p>
				Unanswered: <span id="unansweredQuestions" class="unanswered">0</span>
			</p>
			<h4>Question Status:</h4>
			<ul id="question-status"></ul>
		</div>
	</div>

	<!-- Main quiz container -->
	<div class="quiz-container">

		<!-- Question container -->
		<div id="question-container"></div>

		<!-- Options container -->
		<div class="options" id="options-container"></div>

		<!-- Button container for Prev, Next, and Submit buttons -->
		<div class="button-container">
			<!-- Previous and Next buttons -->
			<div>
				<button class="button" id="prevBtn" onclick="prevQuestion()" style="display: none;">Previous</button>
				<button class="button" id="nextBtn" onclick="nextQuestion()">Next</button>
			</div>
			<!-- Submit button -->
			<button class="submit-button" id="submitBtn" onclick="submitQuiz()" style="display: none;">Submit</button>
		</div>
	</div>

	<!-- Overlay for background when sidebar is open -->
    <div class="overlay" id="overlay" onclick="toggleSidebar()"></div>
	
	<script src="/assets/prism/prism.min.js"></script>
	<script src="/assets/prism/prism-java.min.js"></script>
	<script>
	let endTime;
	// Handle page load and reload to clear the endTime when needed
	window.addEventListener('load', function () {
		let perfEntries = performance.getEntriesByType("navigation");
		if (perfEntries[0].type === "navigate") { // Check if the page is being loaded for the first time
			localStorage.removeItem('endTime');
			endTime = localStorage.getItem('endTime') ? parseInt(localStorage.getItem('endTime')) : Date.now() + totalSeconds * 1000;
			console.log("Page loaded First time");

		} else if (perfEntries[0].type === "reload") { // Check if the page is being reloaded
			endTime = localStorage.getItem('endTime') ? parseInt(localStorage.getItem('endTime')) : Date.now() + totalSeconds * 1000;
			console.log("Page reloaded");

		}

	});

	// Array of quiz questions, dynamically populated from backend
	let questions = [
    <%
		List < QuizDTO > Quiz_questions = (List < QuizDTO >) request.getAttribute("Quiz_questions");
		
    	Integer total_time = (Integer) request.getAttribute("total_time");
		
    	for (int i = 0; i < Quiz_questions.size(); i++) {
		
    		QuizDTO quiz = Quiz_questions.get(i);
			
    		String type = quiz.getType();
			
    		String[] options = quiz.getOptions().toArray(new String[0]); // Convert list of options to array

		if ("Programming".equalsIgnoreCase(type)) {%>
		{
			id: <%=quiz.getId() %>,  
			code: `<%=quiz.getQuestionCode()%>`,  
			codeTitle: `<%=quiz.getQuestionTitle()%>`,  
			options: [
				`<%=options[0]%>`,
				`<%=options[1]%>`,
				`<%=options[2]%>`,
				`<%=options[3]%>`
			]
		} <%=(i != Quiz_questions.size() - 1) ? ',' : "" %> 
    <%} else { %>
			{
				id: <%=quiz.getId() %>,  
					theory: `<%=quiz.getQuestionTitle()%>`,  
						options: [
							`<%=options[0]%>`,
							`<%=options[1]%>`,
							`<%=options[2]%>`,
							`<%=options[3]%>`
						]
			}<%=(i != Quiz_questions.size() - 1) ? ',' : "" %> 
    <%}
	}%>
];

	let selectedAnswers = {};  // Object to store selected answers for each question
	let currentQuestion = 0;  // Track the current question

	// Calculate total quiz time in seconds (total_time is passed from backend)
	let totalSeconds = <%=total_time%> * 60;

	// Reference elements in the UI
	const totalQuestionsElement = document.getElementById("totalQuestions");
	const answeredQuestionsElement = document.getElementById("answeredQuestions");
	const unansweredQuestionsElement = document.getElementById("unansweredQuestions");
	const questionStatusElement = document.getElementById("question-status");
	// Set up circular progress bar animation
	const progressCircle = document.querySelector('.progress-circle');
	const radius = progressCircle.r.baseVal.value;
	const circumference = 2 * Math.PI * radius;
	progressCircle.style.strokeDasharray = `${circumference} ${circumference}`;
	progressCircle.style.strokeDashoffset = `${circumference}`;

	// Initialize total questions and unanswered questions
	totalQuestionsElement.textContent = questions.length;
	unansweredQuestionsElement.textContent = questions.length;




	// Timer element for displaying the remaining time
	const timerElement = document.getElementById("time");

	// Reference to the countdown interval
	let countdown;

	// Function to format time into mm:ss format
	function formatTime(seconds) {
		const minutes = Math.floor(seconds / 60);
		const secs = seconds % 60;
		return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
	}

	// Update the displayed time in the UI
	function updateTimerDisplay(remainingTime) {
		timerElement.textContent = formatTime(remainingTime);
	}

	// Function to fetch a random background image from Unsplash
	function fetchBackgroundImage() {
   	 fetch('/api/get-background-image')
        .then(response => response.json())
        .then(data => {
            const backgroundElement = document.getElementById("background");
            backgroundElement.style.backgroundImage = `url(${data.imageUrl})`;
        })
        .catch(error => {
            console.error('Error fetching background image:', error);
        });
	}




	// Function to calculate remaining time in seconds
	function getRemainingTime() {
		const currentTime = Date.now();
		const remaining = Math.floor((endTime - currentTime) / 1000);
		return remaining >= 0 ? remaining : 0;
	}


	// Function to start the countdown
	function startCountdown() {
		let timeCounter = getRemainingTime();
		countdown = setInterval(() => {
			const remainingTime = getRemainingTime();

			if (timeCounter >= 0) {
				timeCounter--;
				timerElement.textContent = formatTime(remainingTime);

				// Calculate the offset for the progress circle
				const offset = circumference - (remainingTime / totalSeconds) * circumference;
				progressCircle.style.strokeDashoffset = offset;

			} else {
				clearInterval(countdown); // Stop the countdown
				alert("Time's up! Submitting your quiz.");
				submitQuiz();
			}
		}, 1000);

		// Save the end time in localStorage so that the countdown can persist across reloads
		localStorage.setItem('endTime', endTime);
	}

	
	// Handle answer selection (multiple selection allowed)
	function selectAnswer(optionIndex) {
	    const optionDivs = document.querySelectorAll('.option');
	    const selected = selectedAnswers[currentQuestion] || [];

	    if (selected.includes(optionIndex)) {
	        // Deselect the option if already selected
	        selected.splice(selected.indexOf(optionIndex), 1);
	        optionDivs[optionIndex].classList.remove('selected');
	    } else {
	        // Add selected option
	        selected.push(optionIndex);
	        optionDivs[optionIndex].classList.add('selected');
	    }

	    // Update selected answers for the current question
	    selectedAnswers[currentQuestion] = selected.length > 0 ? selected : null;
	    //console.log("Selected answers for question", currentQuestion, selectedAnswers[currentQuestion]);

	    // Update question status colors
	    updateQuestionStatus();
	}

	function updateQuestionStatus() {
	    // Clear the current content of the question status element
	    questionStatusElement.innerHTML = '';

	    let answeredCount = 0;
	    let unansweredCount = 0;

	    // Iterate through each question to check if it has been answered
	    questions.forEach((_, index) => {
	        const status = document.createElement("div");
	        status.classList.add("status-box");
	        status.setAttribute("data-number", index + 1);

	        // Check if there’s a selected answer; add "answered" class if true, else "unanswered"
	        if (selectedAnswers[index] && selectedAnswers[index].length > 0) {
	            status.classList.add("answered"); // Apply answered style (e.g., green background)
	            answeredCount++;
	        } else {
	            status.classList.add("unanswered"); // Apply unanswered style (e.g., no color)
	            unansweredCount++;
	        }

	        questionStatusElement.appendChild(status);
	    });

	    answeredQuestionsElement.textContent = answeredCount;
	    unansweredQuestionsElement.textContent = unansweredCount;
	}

	function updateQuestion() {
	    const questionContainer = document.getElementById("question-container");
	    const optionsContainer = document.getElementById("options-container");

	    // Clear previous question and options
	    questionContainer.innerHTML = '';
	    optionsContainer.innerHTML = '';

	    // Create a label for the question number
	    const questionNumber = document.createElement('div');
	    questionNumber.className = 'question-number';
	    questionNumber.innerHTML = '<h2>Question ' + (currentQuestion + 1) + ':</h2>';
	    questionContainer.appendChild(questionNumber);

	    // Check if it's a programming question
	    if (questions[currentQuestion].code) {
	        const theoryElement = document.createElement('div');
	        theoryElement.className = 'theory-question';
	        theoryElement.textContent = questions[currentQuestion].codeTitle;
	        questionContainer.appendChild(theoryElement);

	        const codeEditor = document.createElement('pre');
	        const codeElement = document.createElement('code');
	        codeElement.className = 'language-java';
	        codeElement.textContent = questions[currentQuestion].code;
	        codeEditor.appendChild(codeElement);
	        questionContainer.appendChild(codeEditor);
	        Prism.highlightElement(codeElement);
	    } else {
	        const theoryElement = document.createElement('div');
	        theoryElement.className = 'theory-question';
	        theoryElement.textContent = questions[currentQuestion].theory;
	        questionContainer.appendChild(theoryElement);
	    }

	    questions[currentQuestion].options.forEach((option, index) => {
	        const optionDiv = document.createElement('div');
	        optionDiv.className = 'option';

	        const label = document.createElement('span');
	        label.className = 'option-label';
	        label.textContent = String.fromCharCode(65 + index) + ')';

	        const button = document.createElement('span');
	        button.textContent = option;
	        optionDiv.onclick = () => selectAnswer(index);

	        optionDiv.appendChild(label);
	        optionDiv.appendChild(button);
	        optionsContainer.appendChild(optionDiv);

	        // Highlight selected options, if any
	        if (selectedAnswers[currentQuestion] && selectedAnswers[currentQuestion].includes(index)) {
	            optionDiv.classList.add('selected');
	        }
	    });

	    updateQuestionStatus();

	    document.getElementById("prevBtn").style.display = currentQuestion === 0 ? "none" : "inline";
	    document.getElementById("nextBtn").style.display = currentQuestion === questions.length - 1 ? "none" : "inline";
	    document.getElementById("submitBtn").style.display = currentQuestion === questions.length - 1 ? "inline" : "none";
	}


	// Display the previous question
	function prevQuestion() {
		if (currentQuestion > 0) {
			currentQuestion--;
			updateQuestion();
		}
	}

	// Display the next question or show the submit button at the end
	function nextQuestion() {
		if (currentQuestion < questions.length - 1) {
			currentQuestion++;
			updateQuestion();
		} else {
			document.getElementById("nextBtn").style.display = "none";
			document.getElementById("submitBtn").style.display = "inline";
		}
	}

	//handles the submission of a quiz               
	function submitQuiz() {

		clearInterval(countdown); // Stop the countdown timer
		localStorage.removeItem('endTime'); // Clear the 'endTime' from localStorage to avoid timer issues on reload

		// Map over the questions array and collect the selected answers
		const selectedAnswerArray = questions.map((question, index) => {
			const selectedAnswer = selectedAnswers[index] || []; // Use an empty array if no answer is found

			return {
				questionId: question.id,  // Use question's id
				selectedOptions: selectedAnswer  // Return the selected answer or an empty array
			};
		});

		// Function to extract selected options based on user input and match them with question options
		function getOptionsFromObject(questions, inputObj) {
			// If inputObj is null, create an empty object to avoid null pointer issues
			if (!inputObj) {
				inputObj = {};
			}

			// Iterate over the questions array and create a result for each question
			return questions.map((question, index) => {
				const selectedIndices = inputObj[index]; // Get the array of selected option indices for the current question

				// If selectedIndices is null, undefined, or empty, use an empty array
				const selectedOptions = selectedIndices != null && selectedIndices.length > 0
					? selectedIndices.map(i => question.options[i]) // Map the indices to the actual option text
					: []; // Use an empty array if no answer is selected

				return {
					questionId: question.id, // Get the question's ID
					selectedOptions: selectedOptions // Assign selected options or empty array
				};
			});
		}

		// Create quiz results by mapping the selected answers to the corresponding questions
		let quizResults = getOptionsFromObject(questions, selectedAnswers);

		// Construct the payload that includes submission time and both arrays of answers
		let payload = {
			submissionTime: getRemainingTime(),  // Attach the remaining time at the point of submission
			AnswerArray: selectedAnswerArray,    // Array of selected answers for each question
			answers: quizResults                 // Array with question ID and selected options for each question
		};

		//console.log(payload); // Log the payload for debugging purposes

		// Send the quiz results to the backend via POST request
		fetch('api/submit_quiz', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json', // Specify that the payload is in JSON format
			},
			body: JSON.stringify(payload), // Convert the payload to a JSON string and send it
		})
			.then(response => {
				if (!response.ok) { // Check if the response is not successful
					throw new Error('Network response was not ok'); // Throw an error if something went wrong
				}

				console.log("Quiz submitted successfully");
				alert("Quiz submitted successfully!"); // Notify the user of success
				window.location.href = "/result"; // Redirect to a result page after successful submission

			})
			.catch(error => {
				console.error("Error submitting quiz:", error);
				alert("There was an error submitting your quiz."); // Notify the user if the submission failed
			});
	}

	// Start the countdown and set up the page when loaded
	window.onload = function () {
		fetchBackgroundImage();
		updateQuestion();
		const remainingTime = getRemainingTime();

		// If the remaining time is zero or negative, reset the timer (useful in case of page reloads)
		if (remainingTime <= 0) {
			// Reset the timer if it's already over
			endTime = Date.now() + totalSeconds * 1000;
			localStorage.setItem('endTime', endTime);
		}

		updateTimerDisplay(remainingTime);
		startCountdown();
	};

	// Handle visibility changes (e.g., when the user switches tabs or minimizes the window)
	document.addEventListener('visibilitychange', () => {
		if (document.visibilityState === 'visible') { // If the page becomes visible again
			clearInterval(countdown);  // Clear the previous timer interval to prevent overlapping timers
			startCountdown();           // Restart the countdown when the tab becomes visible
		}
	});
			

	</script>
	
	<script>
        function toggleSidebar() {
            const sidebar = document.querySelector(".sidebar-container");
            const content = document.querySelector(".quiz-container");
            const overlay = document.getElementById("overlay");
            const hamburger = document.querySelector(".hamburger");

            // Toggle sidebar visibility
            sidebar.classList.toggle("active");
            content.classList.toggle("shift");
            overlay.classList.toggle("active");
            hamburger.classList.toggle("disable");
        }
    </script>
</body>

</html><%}else{
response.sendRedirect("/403");
}%>