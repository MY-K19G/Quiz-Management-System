<%@ include file="../loader.jsp" %>
<%@page import="k19g.quiz.DTO.QuizDTO"%>
<%@page import="java.util.List"%>
<%@ page isELIgnored="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Quiz Score Page</title>
	
	<link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">
	
	<!-- Prism.js styles for syntax highlighting -->
	<link rel="stylesheet" href="/assets/prism/prism.min.css">
	<link rel="stylesheet" href="/assets/prism/prism-okaidia.min.css">
	
	<style>
		/* General body styling */
		body {
			font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
			background-color: #f0f2f5;
			margin: 0;
			display: flex;
			flex-direction: column;
			height: 100vh;
			scroll-behavior: smooth;
			/* Enables smooth scrolling */
		}

		/* Main content area styling */
		.main-content {
			padding: 20px;
			flex-grow: 1;
			overflow-y: auto;
			position: relative;
			/* Necessary for the scroll buttons to move with content */
		}

		/* Header styling for alignment */
		.header {
			display: flex;
			justify-content: flex-end;
			align-items: center;
			margin-bottom: 20px;
		}

		/* Container for the quiz content */
		.quiz-container {
			background: white;
			border-radius: 12px;
			box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
			padding: 30px;
			max-width: 1000px;
			margin: 0 auto;
		}

		/* User information display styling */
		.user-info {
			background: #f8f9fa;
			padding: 20px;
			border-radius: 10px;
			box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
			margin-bottom: 20px;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;
		}

		/* Individual user detail styling */
		.user-detail {
			width: 45%;
			margin-bottom: 10px;
		}

		/* Header for user info section */
		.user-info h2 {
			margin-top: 0;
			font-size: 24px;
			text-align: center;
			width: 100%;
		}

		/* Paragraph styling for user info */
		.user-info p {
			margin: 5px 0;
			font-size: 16px;
		}

		/* Question card styling */
		.question {
			margin-bottom: 30px;
			background: #f8f9fa;
			padding: 20px;
			border-radius: 10px;
			box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
		}

		/* Tint question card green for correct answers */
		.question.correct {
			/* background-color: #d4edda; */
			color: #155724;
			border: 4px dashed #c3e6cb;
		}

		/* Tint question card red for incorrect answers */
		.question.incorrect {
			/*  background-color: #f8d7da; */
			color: #721c24;
			border: 4px dashed #f5c6cb;
		}

		/* Add label for not attempted questions */
		.question.not-attempted {
			/* background-color: #fff3cd; */
			color: #856404;
			border: 4px dashed #ffeeba;
		}

		/* Question title styling */
		.question h3 {
			margin-top: 0;
			font-size: 20px;
		}

		/* Styling for answer options */
		.options {
			margin-top: 15px;
		}

		/* Individual answer option styling */
		.option {
			margin: 8px 0;
			padding: 12px;
			border-radius: 8px;
			display: flex;
			justify-content: space-between;
			/* Aligns option text to the left and tick/cross to the right */
			border: 2px solid #e0e0e0;
			font-size: 16px;
			font-weight: bold;
			align-items: center;
			/* Center items vertically */
		}

		/* Styles for correctly selected options */
		.option.correct {
			background-color: #d4edda;
			color: #155724;
			border-color: #c3e6cb;
		}

		/* Styles for incorrectly selected options */
		.option.incorrect {
			background-color: #f8d7da;
			color: #721c24;
			border-color: #f5c6cb;
		}

		/* Styling for tick and cross indicators */
		.tick-cross {
			margin-left: 10px;
			/* Space between option text and symbol */
			font-size: 20px;
			display: flex;
			/* Flex display to allow centering if needed */
			align-items: center;
			/* Center the symbols vertically */
		}

		/* Explanation section styling */
		.explanation {
			margin-top: 15px;
			font-size: 15px;
			color: #495057;
			background: #f1f3f5;
			padding: 10px;
			border-radius: 8px;
		}
		
		/* Explanation text section styling */
		.explanation pre {
      		white-space: pre-wrap;
        	word-wrap: break-word;
        	overflow-x: auto;
        	max-width: 100%;
    	}

		/* Code block styling for programming questions */
		.code-block {
			background-color: #272822;
			padding: 15px;
			border-radius: 8px;
			margin-bottom: 10px;
			max-height: 350px;
			overflow-y: auto;
		}

		/* Score display styling */
		.score {
			font-size: 30px;
			font-weight: bold;
			margin-bottom: 30px;
			text-align: center;
			color: #28a745;
		}

		/* Marquee style for notifications */
		.marquee {
			background-color: #007bff;
			color: white;
			padding: 10px;
			text-align: center;
			font-weight: bold;
			margin-bottom: 20px;
			position: relative;
			border-radius: 10px;
		}

		/* Close button styling for marquee */
		.close-btn {
			position: absolute;
			right: 10px;
			top: 50%;
			transform: translateY(-50%);
			background: none;
			border: none;
			color: white;
			font-size: 20px;
			cursor: pointer;
		}

		/* General button styling */
		.button {
			background-color: #007bff;
			color: white;
			border: none;
			border-radius: 5px;
			padding: 10px 15px;
			cursor: pointer;
			font-size: 16px;
			margin: 5px;
		}

		/* Button hover effect */
		.button:hover {
			background-color: #0056b3;
		}

		/* Scroll button container styling */
		.scroll-buttons {
			position: fixed;
			/* Fixed positioning for visibility */
			bottom: 20px;
			right: 20px;
			display: flex;
			flex-direction: column;
			gap: 10px;
			/* Adds space between the buttons */
		}

		/* Color styles for correct, wrong, attempted, and time taken */
		.correct {
			color: #06bd30;
		}

		.wrong {
			color: #df0000;
		}

		.attempt {
			color: #8d0e9b;
		}

		.timeTaken {
			color: #e1850f;
		}
		
		
	</style>
</head>

<body>

	<div class="main-content">
		<div class="header">
			<button class="button" onclick="login()">Login</button>
		</div>

		<div class="marquee" id="marquee">
			<marquee behavior="scroll" direction="left">Login to see
				your score on the leaderboard!</marquee>
			<button class="close-btn" onclick="closeMarquee()">✖</button>
		</div>

		<div id="quiz-container" class="quiz-container">
			<h1>Quiz Score Page</h1>

			<div class="user-info" id="user-details"></div>

			<div class="score" id="score-display">Score:</div>

			<div id="quiz-content"></div>

			<button class="button" onclick="printPage()">Print Results</button>
		</div>
	</div>

	<div class="scroll-buttons">
		<button class="button" onclick="scrollToTop()">↑</button>
		<button class="button" onclick="scrollToBottom()">↓</button>
	</div>

	<script src="/assets/prism/prism.min.js"></script>
	<script src="/assets/prism/prism-java.min.js"></script>
	<script src="/assets/confetti/confetti.browser.min.js"></script>
	
	<script>
			<%
			String name = (String)request.getAttribute("username");
			Integer noOfQuestion = (Integer)request.getAttribute("noOfQuestion");
			String level = (String)request.getAttribute("level");
			String category = (String)request.getAttribute("category");
			String type = (String)request.getAttribute("type");
			Integer totalAttemptQuestion = (Integer)request.getAttribute("totalAttemptQuestion");
			Integer totalCorretAns = (Integer)request.getAttribute("totalCorrectAns");
			Integer totalWrongAns = (Integer)request.getAttribute("totalWrongAns");
			String selectedTime = (String)request.getAttribute("selectedTime");
			String submitTime = (String)session.getAttribute("submissionTime");
			List < QuizDTO > generatedQuiz=(List < QuizDTO >)session.getAttribute("generatedQuiz");
			List < List < Integer >> correctAnsInt=(List < List < Integer >>)session.getAttribute("correctAnsInt"); //correct ans int
			List < List < Integer >> attemptQuizAnswer=(List < List < Integer >>)session.getAttribute("attemptQuizAnswer"); //user answer
			%>

    // dynamically render for user and quiz
    const quizMetaData = {
			userName: "<%=name%>",
			noOfQuestions: <%=noOfQuestion%>,
			level: "<%=level%>",
			category: "<%=category%>",
			type: "<%=type%>",
			time: "<%=selectedTime%>",
			totalAttempt: <%=totalAttemptQuestion%>,
			totalCorrect: <%=totalCorretAns %>,
			totalWrong: <%=totalWrongAns %>,
			timeTaken: "<%=submitTime%>"
    };

		// Array to hold the quiz questions and their details
		const quizData = [
    <%
        for (int i = 0; i < generatedQuiz.size(); i++) {
        	QuizDTO quiz = generatedQuiz.get(i);
            String questionCode = quiz.getType().equalsIgnoreCase("Programming") ? "code: `" + quiz.getQuestionCode() + "`, " : "";
            String userAnswer = attemptQuizAnswer.get(i) == null ? "[]" : attemptQuizAnswer.get(i).toString();
    %>
				{
        <%= questionCode %>
				question: `<%= quiz.getQuestionTitle() %>`,
					type: "<%= quiz.getType() %>",
						options: [
							`<%= quiz.getOptions().get(0) %>`,
							`<%= quiz.getOptions().get(1) %>`,
							`<%= quiz.getOptions().get(2) %>`,
							`<%= quiz.getOptions().get(3) %>`
						],
							correctAnswer: <%= correctAnsInt.get(i) %>,
								userAnswer: <%= userAnswer %>,
									explanation: `<%= quiz.getExplanation() %>`
		}<% if (i < generatedQuiz.size() - 1) { %>,<% } %>
    <%
        }
    %>
]

		// Display user information on the page
		const userDetailsDiv = document.getElementById("user-details");
		userDetailsDiv.innerHTML = `
        <h2>User Information</h2>
        <div class="user-detail">
            <p><strong>Name:</strong> ${quizMetaData.userName}</p>
            <p><strong>Number of Questions:</strong> ${quizMetaData.noOfQuestions}</p>
            <p><strong>Level:</strong> ${quizMetaData.level}</p>
            <p><strong>Category:</strong> ${quizMetaData.category}</p>
            <p><strong>Type:</strong> ${quizMetaData.type}</p>
            <p><strong>Time:</strong> ${quizMetaData.time} minutes</p>
        </div>
        <div class="user-detail">
			<p class="attempt"><strong>Total Question Attempted:</strong> ${quizMetaData.totalAttempt}</p>
            <p class="correct"><strong>Total Correct Answers:</strong> ${quizMetaData.totalCorrect}</p>
            <p class="wrong"><strong>Total Wrong Answers:</strong> ${quizMetaData.totalWrong}</p>
            <p class="timeTaken"><strong>Time Taken:</strong> ${quizMetaData.timeTaken}</p>
        </div>
    `;

		const quizContentDiv = document.getElementById("quiz-content");
		// Generate option labels A, B, C, D
		const optionLabels = ['A', 'B', 'C', 'D'];

		// Function to render the quiz questions on the page
		function renderQuiz(quizData) {
			const quizContent = document.getElementById("quiz-content");
			let score = <%=totalCorretAns%>;

			quizData.forEach((item, index) => {
				// Check if the user's answer is correct
				const userAnsweredCorrectly = JSON.stringify(item.userAnswer) === JSON.stringify(item.correctAnswer);
				const userAttempted = item.userAnswer.length > 0;
				const questionClass = userAnsweredCorrectly ? "correct" : userAttempted ? "incorrect" : "not-attempted";

				// Create a div for each question
				const questionDiv = document.createElement("div");
				questionDiv.classList.add("question", questionClass);
				//const questionDiv = document.createElement("div");
				//questionDiv.className = "question";
				questionDiv.innerHTML = `<h3>Question ${index + 1}: ${item.question}</h3>`;

				// Code block for programming questions
				if (item.type === "programming") {
					const codeBlock = document.createElement("pre");
					codeBlock.className = "code-block language-java";
					codeBlock.innerHTML = Prism.highlight(item.code, Prism.languages.java, 'java');
					questionDiv.appendChild(codeBlock);
				}

				// Create div for options
				const optionsDiv = document.createElement("div");
				optionsDiv.className = "options";
				item.options.forEach((option, optIndex) => {
					const optionDiv = document.createElement("div");
					optionDiv.className = "option";
					optionDiv.innerHTML = `
                <span>${optionLabels[optIndex]}. ${option}</span>
                <span class="tick-cross">${item.correctAnswer.includes(optIndex) ? '✔' : (item.userAnswer.includes(optIndex) ? '✖' : '')}</span>
            `;// Display option and correctness indicator

					// Apply correct/incorrect classes based on user's answer
					if (item.correctAnswer.includes(optIndex)) {
						optionDiv.classList.add("correct");
					} else if (item.userAnswer.includes(optIndex)) {
						optionDiv.classList.add("incorrect");
					}

					optionsDiv.appendChild(optionDiv);
				});

				// Display user's selected options
				const selectedOptions = item.userAnswer.map(answerIndex => optionLabels[answerIndex]).join(', ');
				console.log(typeof selectedOptions)
				console.log(selectedOptions)
				const selectedOptionsDiv = document.createElement("div");
				selectedOptionsDiv.className = "selected-options";
				selectedOptionsDiv.innerHTML = `<strong>You selected:</strong> ${selectedOptions || "No options selected"}`;
				questionDiv.appendChild(selectedOptionsDiv);

				questionDiv.appendChild(optionsDiv);

				// Explanation section for the question
				const explanationDiv = document.createElement("div");
				explanationDiv.className = "explanation";
				explanationDiv.innerHTML = `<strong>Explanation:</strong> <pre>${item.explanation}</pre>`;
				questionDiv.appendChild(explanationDiv);

				quizContentDiv.appendChild(questionDiv);
			});

			// Display the user's score at the end of the quiz
			document.getElementById("score-display").innerText = `Score: ${score}/${quizData.length}`;
		}

		// Close marquee function
		function closeMarquee() {
			document.getElementById("marquee").style.display = "none";
		}

		// Scroll to top of the page
		function scrollToTop() {
			document.querySelector('.main-content').scrollTo({
				top: 0,
				behavior: 'smooth' // Smooth scroll behavior
			});
		}

		// Scroll to bottom of the page
		function scrollToBottom() {
			const mainContent = document.querySelector('.main-content');
			mainContent.scrollTo({
				top: mainContent.scrollHeight,
				behavior: 'smooth' // Smooth scroll behavior
			});
		}

		// Function to print the quiz results
		function printPage() {
			const originalBody = document.body.innerHTML;
			const printContent = document.getElementById('quiz-container').outerHTML;
			document.body.innerHTML = printContent;
			window.print();
			document.body.innerHTML = originalBody; // Restore original body after print
			location.reload(); // Reload page to restore JavaScript functionality
		}

		// Simulate rendering the quiz with the generated data
		renderQuiz(quizData);

		// Placeholder functions for login
		function login() {
			alert('Login functionality not implemented yet.');
		}
			
		 window.onload = function() {
		      const end = Date.now() + 2 * 1000;

		      const colors = ["#bb0000", "#ffffff", "#00bb00", "#0000bb", "#ffbb00", "#ff0077"];

		      (function frame() {
		        confetti({
		          particleCount: 3,
		          angle: 60,
		          spread: 55,
		          origin: { x: 0 },
		          colors: colors,
		        });

		        confetti({
		          particleCount: 2,
		          angle: 120,
		          spread: 55,
		          origin: { x: 1 },
		          colors: colors,
		        });

		        if (Date.now() < end) {
		          requestAnimationFrame(frame);
		        }
		      })();
		    }
		</script>
		 
		   
</body>

</html>