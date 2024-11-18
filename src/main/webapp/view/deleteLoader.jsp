<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
	.delete-background-overlay {
	    background: rgba(0, 0, 0, 0.85); 
	    position: fixed; 
	    top: 0;
	    left: 0;
	    width: 100%;
	    height: 100vh; 
	    z-index: 1; 
	    display: none;
	}

	.delete-loader {
	    position: fixed;
	    top: 50%;
	    left: 50%;
	    transform: translate(-50%, -50%);
	    background: #ff3d00;
	    width: 80px;
	    height: 30px;
	    line-height: 18px;
	    text-align: center;
	    color: #931010;
	    font-weight: 700;
	    letter-spacing: 0.5px;
	    font-size: 14px;
	    box-sizing: border-box;
	    border: 5px groove #de3500;
	    border-radius: 0 0 4px 4px;
	    box-shadow: 0 5px 7px #0002;
	    z-index: 2; 
	    display: none;
	}
	
	.delete-loader:before {
	    content: "QUIZ";
	    width: 70px;
	    height: 80px;
	    background: #fff;
	    box-shadow: 0 0 10px #0003;
	    position: absolute;
	    left: 50%;
	    bottom: calc(100% + 6px);
	    transform: translateX(-50%);
	    text-align: center;
	    line-height: 80px; 
	    font-size: 16px;
	    opacity: 1; 
	    animation: loadPaper 4s ease-in infinite;
	}
	
	.delete-loader:after {
	    content: "";
	    width: 70px;
	    height: 80px;
	    background: linear-gradient(to right, #fff 50%, #0000 51%);
	    background-size: 9px 80px;
	    position: absolute;
	    left: 50%;
	    transform: translateX(-50%);
	    top: calc(100% + 6px);
	    animation: disposePaper 4s ease-in infinite;
	}

/* When the loader is in position, hide the QUIZ text */
@keyframes loadPaper {
    0%, 10% {
        height: 80px;
        bottom: calc(100% + 40px);
        opacity: 1; 
    }
    50% {
        height: 80px;
        bottom: calc(100% + 6px);
        opacity: 1; 
    }
    75%, 100% {
        height: 0px; 
        bottom: calc(100% + 6px);
        opacity: 0; 
    }
}

@keyframes disposePaper {
    0%, 50% {
        height: 0px;
        top: calc(100% + 6px);
    }
    75% {
        height: 80px;
        top: calc(100% + 6px);
        opacity: 1;
    }
    100% {
        height: 80px;
        top: calc(100% + 40px);
        opacity: 0;
    }
}

</style>
</head>
<body>
	<div class="delete-background-overlay"></div>
    <span class="delete-loader">Deleting</span>

	<script>
    function showDeleteLoader() {
    	console.log("fdfd");
        document.querySelector(".delete-loader").style.display = "flex";
        document.querySelector(".delete-background-overlay").style.display = "flex";
    }

    function hideDeleteLoader() {
    	console.log("21");
        document.querySelector(".delete-loader").style.display = "none";
        document.querySelector(".delete-background-overlay").style.display = "none";
    }
	</script>

</body>
</html>