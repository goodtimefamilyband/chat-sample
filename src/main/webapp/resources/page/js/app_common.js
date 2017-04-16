/**
 * 
 */

var msgDiv = document.getElementById('messages');
var scrolled = false;

var render = function(message) {
	console.log("Render", message);
	var msgDiv = $(document.createElement('div'));
	msgDiv.attr('class', 'message');
	
	var msgTime = new Date(message.posted*1000);
	
	var timeSpan = $(document.createElement('span'));
	timeSpan.html(msgTime.toLocaleString());
	msgDiv.append(timeSpan);
	
	var authorSpan = $(document.createElement('span'));
	authorSpan.html(message.authorName);
	msgDiv.append(authorSpan);
	
	var txtSpan = $(document.createElement('span'));
	txtSpan.html(message.html);
	msgDiv.append(txtSpan);
	
	return msgDiv;
}

var renderUserMessage = function(message) {
	
	if(message.recipient == null) {
		message.authorName = "Main chat";
	}
	
	var userDiv = $(document.createElement('div'));
	userDiv.attr('class', 'note');
	userDiv.attr('id', 'note' + message.authorId);
	
	var userA = $(document.createElement('a'));
	userA.attr('href', '/app/' + message.authorId + '/');
	userA.html(message.authorName);
	userDiv.append(userA);
	
	return userDiv;
}


$(msgDiv).on('scroll', function() {
	console.log('scroll', scrolled, msgDiv.scrollHeight, msgDiv.scrollTop);
	if(msgDiv.scrollHeight - msgDiv.scrollTop != $(msgDiv).height()) {
		scrolled = true;
	}
	else {
		scrolled = false;
	}
});

function scrollDown() {
	console.log('scrollDown', scrolled, msgDiv.scrollHeight, msgDiv.scrollTop);
	if(!scrolled) {
		console.log(scrolled, msgDiv.scrollTop, msgDiv.scrollHeight);
		msgDiv.scrollTop = msgDiv.scrollHeight;
	}
}

function showMain (message) {
	$('#messages').append(render(message));
	scrollDown();
}

function showNote (message) {
	if(message.recipient == null) {
		message.authorId = "main";
	}
	
	$('#note' + message.authorId).remove();
	
	$('#notes').prepend(renderUserMessage(message));
	$('#btnDm').removeClass('btn-primary')
		.addClass('btn-danger')
		.html('New Messages');
}


$('#msgForm').submit(function() {
	$(this).ajaxSubmit();
	return false;
});

$('#btnDm').click(function() {
	$('#notes').slideToggle();
});