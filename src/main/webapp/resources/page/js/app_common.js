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
	timeSpan.addClass('msgtime');
	msgDiv.append(timeSpan);
	
	var authorSpan = $(document.createElement('a'));
	authorSpan.attr('href', '/app/' + message.authorId + '/');
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
	
	if(message.recipient != null) {
		userA.attr('href', '/app/' + message.authorId + '/');
	}
	else {
		userA.attr('href', '/app/');
	}
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

function postLastSeen(timestamp) {
	var csrf = $('#csrf');
	var csrf_name = csrf.attr('name');
	var csrf_val = csrf.attr('value');
	var data = {tstamp: timestamp};
	data[csrf_name] = csrf_val;
	
	$.post("lastseen", data);
	
}

function showMain (message) {
	$('#messages').append(render(message));
	postLastSeen(message.posted);
	scrollDown();
}

function showNote (message) {
	if(message.recipient == null) {
		message.authorId = "main";
	}
	
	$('.note-placeholder').remove();
	$('#note' + message.authorId).remove();
	
	$('#notes').prepend(renderUserMessage(message));
	$('#btnDm').removeClass('btn-primary')
		.addClass('btn-danger')
		.html('New Messages');
}

function doModel(model, includeNote, postLast = true) {
	for(i in model.messages) {
		$('#messages').append(render(model.messages[i]));
	}
	scrollDown();
	var lastMessage = model.messages[model.messages.length - 1];
	console.log("lastMessage", lastMessage);
	
	if(postLast) {
		postLastSeen(lastMessage.posted);
	}
	
	for(i in model.unseen) {
		if(includeNote(model.unseen[i])) {
			showNote(model.unseen[i]);
		}
	}
}

$('#msgForm').submit(function() {
	$(this).ajaxSubmit({
		success: function(resp, status, xhr, jq) {
			console.log("submitted")
			$('#txtMsg').val('').focus();
		},
		error: function() {
			console.log("There was an error");
		}
	});
	return false;
});

$('#btnDm').click(function() {
	$('#notes').slideToggle();
});
