/**
 * 
 */

var pathArr = window.location.pathname.split("/");
var userId = pathArr[2];
var client = new AsappClient({
	onUserMessage: function (message) {
		
		if(message.authorId == userId || message.recipient == userId) {
	    	showMain(message);
		}
		else {
			showNote(message);
		}
		
    },
    onModel: function(model) {
    	doModel(model, function(msg) {
    		return msg.authorId != userId;
    	});
    },
    onMessage: function (message) {
    	showNote(message);
    }
});
