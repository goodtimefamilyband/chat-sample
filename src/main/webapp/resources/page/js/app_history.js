/**
 * 
 */

var client = new AsappClient({
	onMessage: function (message) {
		showNote(message);
    },
    onUserMessage: function(message) {
    	showNote(message);
    },
    onModel: function(model) {
    	doModel(model, function(msg){
    		return true;
    	}, false);
    }
});
