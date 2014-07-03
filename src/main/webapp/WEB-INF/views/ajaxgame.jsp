<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

<html>
<head>
    <title>Welcome To Optimal Ghost Game</title>
    <link rel="stylesheet" href="<c:url value="/resources/blueprint/screen.css" />" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="<c:url value="/resources/blueprint/print.css" />" type="text/css" media="print">
	<link rel="stylesheet" href="<c:url value="/resources/popup.css" />" type="text/css" media="screen, projection">
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script type="text/javascript">
	function doAjaxPost() {  
		  // get the 'play' value from the form  
		  var play = $('#play').val();
		  if (play ==='') {
			  $('#play').focus();
			  return;
		  }
			  
		  $.ajax({  
		    type: "POST",  
		    url: "<c:url value="/ajaxgame"/>", 
		    dataType: 'json',
		    data: "play=" + play ,  
		    
		    success: function(response){  
		      // we have the response  
		      $('#string').val(response.string);
		      $('#message').html(response.message);
		      $('#play').val(response.play);
		      if (response.message ==='') {
			      $('#play').focus();
		      } else {
		    	  $("#regularContainer").hide();
		    	  $("#gameOverMessage").show();
		    	  $('#playAgain').focus();
		      }
		    },  
		    
		    error: function(e){  
		      alert('Error: ' + e.statusError);  
		    }  
		  });  
		}  

	$( document ).ready(function() {
		$("#gameOverMessage").hide();
		// Setting the focus to play field text...
		$('#play').focus();
		// Presing 'Enter' while in that file triggers the 'click' on the 'playButton'...  
		$('#play').keypress(function(event){
			  if(event.keyCode == 13){
			    $('#playButton').click();
			  }
			});
		});
	</script>
</head>
<body>
	<div id="regularContainer" class="container" align="center">
		<br>
		<h2>
			I defy you: play against me if you dare... ;-)
		</h2>
		<textarea id="string" disabled="disabled" readonly="readonly" rows="1" cols="25">${response.string}</textarea>
		<br>
		<input id="play" type="text" maxlength="1" size="1" value="${response.play}" />
		<input id="playButton" type="button" value="Man Against Machine" onclick="doAjaxPost();"/>
		<br>
	</div>
	<br>
	<div id="gameOverMessage" align="center">
		<h4 id="message" align="center">${response.message}</h4>
		<form id="playAgainForm">
			<input id="playAgain" type="submit" value="Play again..." />
		</form>
	</div>
</body>
</html>