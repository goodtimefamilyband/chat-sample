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
    	doModel(model, function(msg) {
    		return msg.authorId != userId;
    	});
    },
    onMessage: function (message) {
    	showNote(message);
    }
});
