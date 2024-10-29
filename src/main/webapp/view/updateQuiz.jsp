<%@page import="k19g.quiz.entity.Level"%>
<%@page import="k19g.quiz.entity.Quiz"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Update Quiz Questions</title>
	<link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">
	<style>
		/* Global reset for all elements */
		* {
			margin: 0;
			padding: 0;
			box-sizing: border-box;
		}

		/* Body styling */
		body {
			font-family: Arial, sans-serif;
			background: linear-gradient(to right, #2c3e50, #3498db);
			display: flex;
			min-height: 100vh;
		}

		/* Class to hide elements */
		a.hidden {
			display: none;
		}

		/* Navbar styling */
		.navbar {
			background-color: #2c3e50;
			transition: transform 0.3s ease;
			z-index: 5;
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

		/* Navbar title styling */
		.navbar h2 {
			font-size: 24px;
			margin-bottom: 20px;
			text-align: center;
		}

		/* Navbar link styling */
		.navbar a {
			display: block;
			color: #fff;
			text-decoration: none;
			font-size: 18px;
			margin: 10px 0 15px 0;
			padding: 10px 15px;
			width: 100%;
			border-radius: 8px;
			transition: background-color 0.3s ease;
		}

		/* Navbar link hover effect */
		.navbar a:hover {
			background-color: #6c5ce7;
		}

		/* Main content area */
		.content {
			margin-left: 220px;
			padding: 20px;
			width: calc(100% - 220px);
		}

		/* Search container */
		.search-container {
			margin-bottom: 20px;
		}

		/* Search input styling */
		.search-container input[type="text"] {
			width: 100%;
			padding: 10px;
			font-size: 16px;
			border: 1px solid #ccc;
			border-radius: 5px;
		}

		/* Card container for grid layout */
		.card-container {
			display: grid;
			grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
			gap: 20px;
			min-height: 400px;
		}

		/* Card link styling */
		.card-container a {
			text-decoration: none;
		}

		/* Card styling */
		.card {
			background-color: #fff;
			padding: 20px;
			border-radius: 10px;
			box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
			transition: box-shadow 0.3s ease, transform 0.3s ease;
			cursor: pointer;
			min-height: 200px;
		}

		/* Card hover effect */
		.card:hover {
			box-shadow: 0 10px 25px rgb(207 196 196);
			transform: translateY(-10px);
		}

		/* Class to hide cards */
		.card.hidden {
			display: none;
		}

		/* Card title styling */
		.card h3 {
			font-size: 18px;
			color: #333;
			margin-bottom: 10px;
			display: -webkit-box;
			-webkit-line-clamp: 3;
			-webkit-box-orient: vertical;
			overflow: hidden;
			text-overflow: ellipsis;
		}

		/* Card paragraph styling */
		.card p {
			color: #666;
			font-size: 14px;
			margin-bottom: 10px;
		}

		/* Button styling */
		button {
			padding: 10px 20px;
			background-color: #3498db;
			color: #fff;
			border: none;
			border-radius: 5px;
			cursor: pointer;
			font-size: 16px;
		}

		/* Button hover effect */
		button:hover {
			background-color: #2980b9;
		}

		/* No results message styling */
		#noResultsMessage {
			display: none;
			font-size: 20px;
			color: #fff;
			text-align: center;
			margin-top: 50px;
		}

		/* Styles for the main content area, ensuring proper spacing from the navbar */
		.content {
			margin-left: 220px;
			padding: 20px;
			width: calc(100% - 220px);
		}

		/* Hamburger menu styling */
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

		/* Search and filter container layout */
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

		/* Input and select styling */
		.search-container input[type="text"],
		.filter-container select {
			flex: 1;
			padding: 10px;
			font-size: 16px;
			border: 1px solid #ccc;
			border-radius: 5px;
		}

		/* Media query to hide sidebar and display hamburger on small screens */
		@media (max-width: 768px) {
			.navbar {
				transform: translateX(-100%);
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

			.card:hover {
				box-shadow: 0 10px 25px rgb(207 196 196);
				transform: translateY(-10px);
			}
		}

		/* Keyframes for modal fade-in animation */
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
		<!-- Search Container -->
		<div class="search-container">
			<input type="text" id="searchBar" placeholder="Search for a question...">
		</div>

		<!-- Filter Container -->
		<div class="filter-container">
			<!-- Category Dropdown -->
			<select id="categoryFilter">
				<option value="">All Categories</option>
				<% List<String> allCategories = (List<String>) request.getAttribute("allCategories");
						System.err.print(allCategories);
						if (allCategories != null) {
						for (String category : allCategories) {
						%>
						<option value="<%= category %>">
							<%= category %>
						</option>
						<% } } %>
			</select>

			<!-- Question Type Dropdown -->
			<select id="typeFilter">
				<option value="">All Types</option>
				<% List<String> allTypes = (List<String>) request.getAttribute("allTypes");
						if (allTypes != null) {
						for (String type : allTypes) {
						%>
						<option value="<%= type %>">
							<%= type %>
						</option>
						<% } } %>
			</select>

			<!-- Level Dropdown -->
			<select id="levelFilter">
				<option value="">All Levels</option>
				<% List<Level> allLevels = (List<Level>) request.getAttribute("allLevels");
						System.err.println(allLevels);
						if (allLevels != null) {
						for (Level level : allLevels) {

						%>
						<option value="<%= level.name() %>">
							<%= level.name() %>
						</option>
						<% } } %>
			</select>
		</div>

		<!-- Card Container for Quiz Questions -->
		<div class="card-container" id="cardContainer">
			<% List<Quiz> questionEntity = (List<Quiz>) request.getAttribute("QuestionEntity");
					if (questionEntity != null) {
					for (Quiz question : questionEntity) {
					%>
					<a href="edit?questionId=<%= question.getId() %>">
						<div class="card" data-category="<%= question.getCategory() %>"
							data-type="<%= question.getType() %>" data-level="<%= question.getLevel() %>">
							<h3>
								<%= question.getQuestionTitle() %>
							</h3>
							<p>Category: <%= question.getCategory() %>
							</p>
							<p>Type: <%= question.getType() %>
							</p>
							<p>Level: <%= question.getLevel() %>
							</p>
						</div>
					</a>
					<% } } else { %>
						<p>No questions available.</p>
						<% } %>
		</div>

		<!-- No results found message -->
		<div id="noResultsMessage" style="display: none;">No questions found.</div>
	</div>


	<script>
		const searchBar = document.getElementById('searchBar');
		const categoryFilter = document.getElementById('categoryFilter');
		const typeFilter = document.getElementById('typeFilter');
		const levelFilter = document.getElementById('levelFilter');
		const cardContainer = document.getElementById('cardContainer');
		const cards = cardContainer.getElementsByClassName('card');
		const noResultsMessage = document.getElementById('noResultsMessage'); // No results message element

		// Search function to filter quiz questions
		searchBar.addEventListener('input', function () {
			filterCards();
		});

		// Filter by category
		categoryFilter.addEventListener('change', function () {
			filterCards();
		});

		// Filter by question type (Theory/Programming)
		typeFilter.addEventListener('change', function () {
			filterCards();
		});

		// Filter by level
		levelFilter.addEventListener('change', function () {
			filterCards();
		});

		// Combined function to filter cards based on search, category, type, and level
		function filterCards() {
			const searchValue = searchBar.value.toLowerCase();
			const categoryValue = categoryFilter.value;
			const typeValue = typeFilter.value;
			const levelValue = levelFilter.value;
			let visibleCards = 0; // Count of visible cards

			for (let i = 0; i < cards.length; i++) {
				const questionTitle = cards[i].getElementsByTagName('h3')[0].textContent.toLowerCase();
				const questionCategory = cards[i].dataset.category;
				const questionType = cards[i].dataset.type;
				const questionLevel = cards[i].dataset.level;

				let matchesSearch = questionTitle.includes(searchValue);
				let matchesCategory = categoryValue === "" || questionCategory === categoryValue;
				let matchesType = typeValue === "" || questionType === typeValue;
				let matchesLevel = levelValue === "" || questionLevel === levelValue;

				const parentAnchor = cards[i].closest('a');

				if (matchesSearch && matchesCategory && matchesType && matchesLevel) {
					parentAnchor.classList.remove('hidden');  // Remove 'hidden' class from <a>
					visibleCards++;
				} else {
					parentAnchor.classList.add('hidden');    // Add 'hidden' class to <a>
				}
			}

			// Toggle the visibility of the "No questions found" message
			if (visibleCards === 0) {
				noResultsMessage.style.display = 'block';
			} else {
				noResultsMessage.style.display = 'none';
			}
		}

		// Function to toggle the sidebar visibility
		function toggleSidebar() {
			const sidebar = document.querySelector('.navbar');
			sidebar.classList.toggle('active');

			const ham = document.querySelector('.hamburger');
			ham.classList.toggle('hamburger-active');
		}
	</script>
</body>

</html>