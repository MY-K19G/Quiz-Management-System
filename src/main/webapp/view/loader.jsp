<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
 <link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">    
 <style>
    .background-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.85); 
        z-index: 50; 
     }
     
	.loader {
	    width: auto;
	    height: auto;
	    position: fixed;
	    left: 50%;
	    top: 50%;
	    z-index: 100;
	    transform: translate(-50%, -50%);
	}

	.loader-container {
	    display: grid;
	    grid-template-columns: repeat(5, 1fr);
	    animation: rot 16s linear infinite;
	}
	
	.loader-container li {
	    width: 40px;
	    height: 40px;
	    background: #651FFF;
	    border-radius: 4px;
	    box-shadow: 0 0 1px #fff, 0 0 5px #651FFF, 0 0 10px #651FFF, 0 0 15px #651FFF, 0 0 25px #651FFF, 0 0 55px #651FFF;
	    animation: scale 0.8s linear alternate infinite;
	}

	@keyframes rot {
	    100% {
	        transform: rotate(360deg);
	    }
	}
	
	
	@keyframes scale {
	    100% {
	        transform: scale(0.1);
	        opacity: 0;
	    }
	}

	/* Z-index and animation-delay logic for each li element */
	li:nth-child(1) { z-index: 24; animation-delay: 0.1s; }
	li:nth-child(2) { z-index: 23; animation-delay: 0.2s; }
	li:nth-child(3) { z-index: 22; animation-delay: 0.3s; }
	li:nth-child(4) { z-index: 21; animation-delay: 0.4s; }
	li:nth-child(5) { z-index: 20; animation-delay: 0.5s; }
	
	li:nth-child(6) { z-index: 19; animation-delay: 0.3s; }
	li:nth-child(7) { z-index: 18; animation-delay: 0.4s; }
	li:nth-child(8) { z-index: 17; animation-delay: 0.5s; }
	li:nth-child(9) { z-index: 16; animation-delay: 0.6s; }
	li:nth-child(10) { z-index: 15; animation-delay: 0.7s; }
	
	li:nth-child(11) { z-index: 14; animation-delay: 0.5s; }
	li:nth-child(12) { z-index: 13; animation-delay: 0.6s; }
	li:nth-child(13) { z-index: 12; animation-delay: 0.7s; }
	li:nth-child(14) { z-index: 11; animation-delay: 0.8s; }
	li:nth-child(15) { z-index: 10; animation-delay: 0.9s; }
	
	li:nth-child(16) { z-index: 9; animation-delay: 0.7s; }
	li:nth-child(17) { z-index: 8; animation-delay: 0.8s; }
	li:nth-child(18) { z-index: 7; animation-delay: 0.9s; }
	li:nth-child(19) { z-index: 6; animation-delay: 1s; }
	li:nth-child(20) { z-index: 5; animation-delay: 1.1s; }
	
	li:nth-child(21) { z-index: 4; animation-delay: 1s; }
	li:nth-child(22) { z-index: 3; animation-delay: 1.1s; }
	li:nth-child(23) { z-index: 2; animation-delay: 1.2s; }
	li:nth-child(24) { z-index: 1; animation-delay: 1.3s; }
	li:nth-child(25) { z-index: 0; animation-delay: 1.4s; }

	</style>
</head>
<body>
	<div class="background-overlay"></div>
	<div class="loader">
		<ul class="loader-container">
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
		</ul>
	</div>
	<script>
    // Show the loader when the page starts loading
    window.addEventListener('load', function() {
        document.querySelector(".loader").style.display = "none";
        document.querySelector(".background-overlay").style.display = "none";
    });

 // Show the loader when the page is about to be reloaded or navigated away from
    window.addEventListener("beforeunload", function() {
        document.querySelector(".loader").style.display = "flex";
        document.querySelector(".background-overlay").style.display = "flex";
        
    });
	
    function showLoader() {
        document.querySelector(".loader").style.display = "flex";
        document.querySelector(".background-overlay").style.display = "flex";
    }

    function hideLoader() {
        document.querySelector(".loader").style.display = "none";
        document.querySelector(".background-overlay").style.display = "none";
    }
</script>
</body>
</html>