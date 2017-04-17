/**
 * 
 */


var client = new AsappClient({
	onMessage: function (message) {
		showMain(message);
    },
    onModel: function(model) {
    	console.log(model);
    	doModel(model, function(msg) {
    		
    		return msg.recipient != null;
    	});
    },
    onUserMessage: function(message) {
    	console.log("OnUserMessage", message);
    	
    	showNote(message);
    }
});
