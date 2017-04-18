/**
 * 
 */


var client = new AsappClient({
	onMessage: function (message) {
		showMain(message);
    },
    onModel: function(model) {
    	doModel(model, function(msg) {
    		
    		return msg.recipient != null;
    	});
    },
    onUserMessage: function(message) {
    	showNote(message);
    }
});
