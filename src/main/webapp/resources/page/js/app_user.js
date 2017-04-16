/**
 * 
 */

var pathArr = window.location.pathname.split("/");
var userId = pathArr[2];
var client = new AsappClient({
	onUserMessage: function (message) {
		console.log(message);
		
		if(message.authorId == userId || message.recipient == userId) {
	    	showMain(message);
		}
		else {
			showNote(message);
		}
		
    },
    onModel: function(model) {
    	console.log(model);
    	for(i in model.messages) {
    		$('#messages').append(render(model.messages[i]));
    	}
    	scrollDown();
    },
    onMessage: function (message) {
    	showNote(message);
    }
});
