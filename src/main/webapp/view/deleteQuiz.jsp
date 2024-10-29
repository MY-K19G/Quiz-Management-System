<%@page import="k19g.quiz.entity.Level"%>
<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<%@page import="java.util.List"%>
<%@page import="k19g.quiz.entity.Quiz"%>

<html lang="en">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Delete Quiz Questions</title>
	<link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">
	<style>
		/* Reset margin, padding, and box-sizing for all elements */
		* {
			margin: 0;
			padding: 0;
			box-sizing: border-box;
		}

		/* Basic body styling with a gradient background and flexbox layout */
		body {
			font-family: Arial, sans-serif;
			background: linear-gradient(to right, #e74c3c, #c0392b);
			display: flex;
			min-height: 100vh;
		}

		/* Navbar styling, fixed on the left side */
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
			transition: transform 0.3s ease;
		}

		/* Navbar header styling */
		.navbar h2 {
			font-size: 24px;
			margin-bottom: 40px;
			text-align: center;
		}

		/* Navbar links styling */
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

		/* Navbar link hover effect */
		.navbar a:hover {
			background-color: #6c5ce7;
		}

		/* Main content area styling */
		.content {
			margin-left: 220px;
			padding: 20px;
			width: calc(100% - 220px);
		}

		/* Hamburger menu styling for mobile */
		.hamburger {
			display: none;
			position: fixed;
			top: 20px;
			left: 20px;
			cursor: pointer;
			font-size: 30px;
			color: white;
			z-index: 9;
		}

		/* Search and filter container styling */
		.search-container,
		.filter-container {
			display: flex;
			gap: 20px;
			margin-bottom: 15px;
		}

		/* Filter container styling */
		.filter-container {
			flex-wrap: wrap;
			margin-left: 0px;
		}

		/* Input and select styling for search and filter */
		.search-container input[type="text"],
		.filter-container select {
			flex: 1;
			padding: 10px;
			font-size: 16px;
			border: 1px solid #ccc;
			border-radius: 5px;
		}

		/* Card container layout using grid */
		.card-container {
			display: grid;
			grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
			gap: 20px;
		}

		/* Individual card styling */
		.card {
			background-color: #fff;
			padding: 20px;
			border-radius: 10px;
			box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
			transition: box-shadow 0.3s ease;
			position: relative;
			cursor: pointer;
			min-height: 120px;
		}

		/* Card title styling */
		.card h3 {
			font-size: 18px;
			color: #333;
			margin-bottom: 10px;
			overflow: hidden;
			text-overflow: ellipsis;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 3;
			line-clamp: 3;
			height: 60px;
		}

		/* Card paragraph styling */
		.card p {
			color: #666;
			font-size: 14px;
			margin-bottom: 10px;
		}

		/* No results message styling */
		.noResultsMessage {
			font-size: 20px;
			color: #fff;
			text-align: center;
			margin-top: 50px;
		}

		/* Delete button styling */
		.delete-btn {
			background-color: #e74c3c;
			color: white;
			padding: 5px 10px;
			border: none;
			border-radius: 5px;
			cursor: pointer;
			position: absolute;
			bottom: 10px;
			right: 10px;
		}

		/* Delete button hover effect */
		.delete-btn:hover {
			background-color: #c0392b;
		}

		/* Close button styling */
		.close-btn {
			background-color: #e74c3c;
			color: white;
			padding: 5px 10px;
			border: none;
			border-radius: 5px;
			cursor: pointer;
		}

		/* Close button hover effect */
		.close-btn:hover {
			background-color: #c0392b;
		}

		/* Modal styling */
		.modal {
			display: none;
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			background-color: rgba(0, 0, 0, 0.7);
			justify-content: center;
			align-items: center;
		}

		/* Modal content styling */
		.modal-content {
			background-color: white;
			padding: 30px;
			border-radius: 10px;
			width: 80%;
			max-width: 600px;
			box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
			animation: modalFadeIn 0.5s ease-out;
		}

		/* Modal header styling */
		.modal-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20px;
		}

		/* Modal header title styling */
		.modal-header h3 {
			margin: 0;
			font-size: 24px;
		}

		/* Modal body styling */
		.modal-body {
			margin-top: 10px;
		}

		/* Paragraph styling in modal body */
		.modal-body p {
			margin-bottom: 10px;
			font-size: 16px;
			color: #555;
		}

		/* List styling in modal body */
		.modal-body ul {
			list-style-type: none;
			padding: 0;
		}

		/* List item styling in modal body */
		.modal-body ul li {
			background-color: #ffc7c7;
			padding: 10px;
			margin-bottom: 5px;
			border-radius: 5px;
		}

		/* Media query to hide sidebar and display hamburger on small screens */
		@media (max-width: 768px) {
			.navbar {
				transform: translateX(-100%);
				z-index: 5;
			}

			.content {
				margin-left: 0;
				width: 100%;
			}

			.hamburger {
				display: block;
				color: #64f9ff;
			}

			.hamburger:hover {
				color: #b1b1b1;
				/* Change color on hover */
			}


			.navbar.active {
				transform: translateX(0);
			}

			.hamburger-active {
				margin-left: 135px;
				margin-top: -7px;
			}

			.search-container {
				margin-left: 45px;
			}
		}

		/* Animation for fading in the modal */

		@keyframes modalFadeIn {
			from {
				opacity: 0;
				transform: translateY(-50px);
			}

			to {
				opacity: 1;
				transform: translateY(0);
			}
		}
	</style>

</head>

<body>
	<div class="hamburger" onclick="toggleSidebar()">&#9776;</div>
<div class="navbar">
    <h2>Quiz Management System</h2>
    <a href="/create">Create</a>
    <a href="/update">Update</a>
    <a href="/delete">Delete</a>
</div>
<div class="content">
    <div class="search-container">
        <input type="text" id="searchBar" placeholder="Search for a question...">
    </div>

    <div class="filter-container">

        <% List<Quiz> allQuestion=(List<Quiz>)request.getAttribute("allQuestions");
		   List<String> allCategorys = (List<String>) request.getAttribute("allCategorys");
		   List<String> allTypes = (List<String>) request.getAttribute("allTypes");
	       List<Level> allLevels = (List<Level>) request.getAttribute("allLevels");
		%>

        <!-- Category Dropdown -->
        <select id="categoryFilter">
            <option value="all">All Categories</option>
            <% if (allCategorys !=null) { 
            	for(String category: allCategorys){ %>
            <option value="<%=category %>">
                <%=category %>
            </option>
            <% } } %>
        </select>

        <!-- Type Dropdown -->
        <select id="typeFilter">
            <option value="all">All Types</option>
            <% if (allTypes !=null) {
           		 for (String type : allTypes) { %>
            <option value="<%= type %>">
                <%= type %>
            </option>
            <% } } %>
        </select>

        <!-- Level Dropdown -->
        <select id="levelFilter">
            <option value="all">All Levels</option>
            <% if (allLevels !=null) {
            	for (Level level : allLevels) { %>
            <option value="<%= level.name() %>">
                <%= level.name() %>
            </option>
            <% } } %>
        </select>
    </div>

    <!-- Quiz Cards -->
    <div class="card-container" id="cardContainer">
        <% if (allQuestion !=null) { 
        	for (int i=0; i < allQuestion.size(); i++) {
        		Quiz quiz=allQuestion.get(i); %>
        }
        <div class="card" onclick="showQuestionDetails(<%= i + 1 %>)">
            <h3>
                <%= quiz.getQuestionTitle() %>
            </h3>
            <p>Type: <%= quiz.getType() %>
            </p>
            <p>Category: <%= quiz.getCategory() %>
            </p>
            <button class="delete-btn" onclick="deleteQuestion(<%= i + 1 %>)">Delete</button>
        </div>
        <% } } %>
    </div>
</div>

<!-- Modal Structure -->
<div id="questionModal" class="modal">
    <div id="modalContent" class="modal-content">
        <div class="modal-header">
            <h3 id="modalQuestionTitle">Question Title</h3>
            <button class="close-btn" onclick="closeModal()">Close</button>
        </div>
        <div class="modal-body">
            <p><strong>Type:</strong> <span id="modalQuestionType"></span></p>
            <p><strong>Level:</strong> <span id="modalQuestionLevel"></span></p>
            <p><strong>Category:</strong> <span id="modalQuestionCategory"></span></p>
            <p><strong>Options:</strong></p>
            <ul id="modalQuestionOptions"></ul>
            <p><strong>Correct Answer(s):</strong> <span id="modalCorrectAnswers"></span></p>
            <p><strong>Explanation:</strong> <span id="modalExplanation"></span></p>
        </div>
    </div>
</div>

	<script>
		// Getting references to DOM elements
		const searchBar = document.getElementById('searchBar');
		const categoryFilter = document.getElementById('categoryFilter');
		const typeFilter = document.getElementById('typeFilter');
		const levelFilter = document.getElementById('levelFilter');
		const cardContainer = document.getElementById('cardContainer');
		const questionModal = document.getElementById('questionModal');

		// Sample question data - populated from server-side
		const questions = [
        <% for (int i = 0; i < allQuestion.size(); i++) { %>
		{
			id: <%= allQuestion.get(i).getId() %>,
			title: `<%= allQuestion.get(i).getQuestionTitle() %>`,
			type: "<%= allQuestion.get(i).getType() %>",
			level: `<%= allQuestion.get(i).getLevel() %>`,
			category: "<%= allQuestion.get(i).getCategory() %>",
			options: [
				`<%= allQuestion.get(i).getOptions().get(0) %>`,
				`<%= allQuestion.get(i).getOptions().get(1) %>`,
				`<%= allQuestion.get(i).getOptions().get(2) %>`,
				`<%= allQuestion.get(i).getOptions().get(3) %>`
			],
			correctAnswers: [
				`<%= String.join("`, `", allQuestion.get(i).getAnswers()) %>`
			],
			explanation: `<%= allQuestion.get(i).getExplanation() %>`
		} <% if (i < allQuestion.size() - 1) { %>,<% } %>
        <% } %>
    ];

		// Function to toggle the sidebar visibility
		function toggleSidebar() {
			const sidebar = document.querySelector('.navbar');
			sidebar.classList.toggle('active'); 

			const ham = document.querySelector('.hamburger');
			ham.classList.toggle('hamburger-active'); 
		}

		// filter questions based on search input
		searchBar.addEventListener('input', function () {
			const filter = searchBar.value.toLowerCase(); 
			filterQuestions(); 
		});

		// Event listeners for filter dropdowns
		categoryFilter.addEventListener('change', filterQuestions);
		typeFilter.addEventListener('change', filterQuestions);
		levelFilter.addEventListener('change', filterQuestions);

		// Filter questions based on search, category, and type
		function filterQuestions() {
			const searchText = searchBar.value.toLowerCase(); 
			const selectedCategory = categoryFilter.value; 
			const selectedType = typeFilter.value; 
			const selectedLevel = levelFilter.value; 

			cardContainer.innerHTML = ''; 

			// Filter the questions based on the criteria
			const filteredQuestions = questions.filter(q => {
				const matchesSearch = q.title.toLowerCase().includes(searchText); 
				const matchesCategory = selectedCategory === 'all' || q.category === selectedCategory; 
				const matchesType = selectedType === 'all' || q.type === selectedType; 
				const matchesLevel = selectedLevel === 'all' || q.level === selectedLevel; 
				return matchesSearch && matchesCategory && matchesType && matchesLevel; 
			});

			// Display message if no questions are found
			if (filteredQuestions.length === 0) {
				cardContainer.innerHTML = '<p class="noResultsMessage">No questions found.</p>';
			} else {
				// Create and display card elements for filtered questions
				filteredQuestions.forEach(q => {
					const card = document.createElement('div');
					card.classList.add('card');
					card.setAttribute('data-question-id', q.id); 
					card.setAttribute('onclick', `showQuestionDetails(${q.id})`); 
					card.innerHTML = `
                    <h3>${q.title}</h3>
                    <p>Type: ${q.type}</p>
                    <p>Category: ${q.category}</p>
                    <button class="delete-btn" onclick="deleteQuestion(${q.id}, event)">Delete</button>
                `;
					cardContainer.appendChild(card); 
				});
			}
		}

		// Function to delete a quiz question
		function deleteQuestion(questionId, event) {
			event.stopPropagation();  
			if (confirm("Are you sure you want to delete this question?")) {

				console.log(questionId);
				fetch(`http://localhost:8083/api/delete-quiz/${questionId}`, {
					method: 'DELETE',
					headers: {
						'Content-Type': 'application/json',
					}
				})
					.then(response => {
						if (!response.ok) {
							throw new Error('Network response was not ok');
						}
						return response.text();
					})
					.then(data => {
						// Remove the question from the questions array
						const questionIndex = questions.findIndex(q => q.id === questionId);
						if (questionIndex > -1) {
							questions.splice(questionIndex, 1); // Remove the question from the array
							filterQuestions(); // Re-render the questions after deletion
							alert(`Question ID ${questionId} deleted successfully!`); // Success message
						}
					})
					.catch(error => {
						console.error('Error:', error);
						alert('Failed to delete the question.'); // Error message
					});
			}
		}

		// Function to show question details in the modal
		function showQuestionDetails(questionId) {
			const question = questions.find(q => q.id === questionId); // Find the question by ID
			if (question) {
				// Populate modal with question details
				document.getElementById('modalQuestionTitle').textContent = question.title;
				document.getElementById('modalQuestionType').textContent = question.type;
				document.getElementById('modalQuestionCategory').textContent = question.category;
				document.getElementById('modalQuestionLevel').textContent = question.level;

				const optionsList = document.getElementById('modalQuestionOptions');
				optionsList.innerHTML = ''; 

				// Adding options with labels A, B, C, D
				question.options.forEach((option, index) => {
					const li = document.createElement('li');
					li.textContent = String.fromCharCode(65 + index) + '. ' + option; // Convert index to A, B, C, D
					optionsList.appendChild(li); // Add option to the list
				});

				// Prepare correct answers for display
				const answerLabels = ['A', 'B', 'C', 'D']; // Corresponding labels for 0, 1, 2, 3
				let matchedAnswers = question.correctAnswers.map(answer => {
					let index = question.options.indexOf(answer); // Find the index of the correct answer in options
					if (index !== -1) {
						return `${answerLabels[index]}: ${answer}`; // Return the label (A, B, etc.) and the correct answer
					}
					return ''; // In case there's no match
				}).filter(answer => answer !== ''); // Filter out any empty values

				document.getElementById('modalCorrectAnswers').textContent = '<br>'; // Clear previous correct answers
				document.getElementById('modalCorrectAnswers').innerHTML = matchedAnswers.join(',<br>'); // Display correct answers
				document.getElementById('modalExplanation').textContent = question.explanation; // Show explanation

				// Show the modal
				questionModal.style.display = 'flex';
			}
		}

		// Function to close the modal
		function closeModal() {
			questionModal.style.display = 'none'; // Hide the modal
		}

		// Close the modal when clicking outside of it
		window.onclick = function (event) {
			const modal = document.getElementById('questionModal');
			const modalContent = document.getElementById('modalContent');
			if (event.target == modal && !modalContent.contains(event.target)) {
				closeModal();
			}
		}

		// Initial render of questions
		filterQuestions(); // Call filterQuestions to display initial set of questions

	</script>

</body>

</html>