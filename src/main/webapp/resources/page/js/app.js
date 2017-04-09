/**
 * 
 */

var msgDiv = document.getElementById('messages');
var scrolled = false;

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

var client = new AsappClient({
	onMessage: function (message) {
		console.log(message);
    	$('#messages').append(message);
    	scrollDown();
    },
    onModel: function(model) {
    	console.log(model);
    	for(i in model.messages) {
    		$('#messages').append(model.messages[i]);
    	}
    	scrollDown();
    }
});

/*
$('#msgForm').ajaxForm(function() {
	console.log('Message submitted');
});
*/

$('#msgForm').submit(function() {
	$(this).ajaxSubmit();
	return false;
});