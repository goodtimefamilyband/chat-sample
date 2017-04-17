/**
 * 
 */

var client = new AsappClient({
	onMessage: function (message) {
		console.log(message);
		showNote(message);
    },
    onUserMessage: function(message) {
    	showNote(message);
    },
    onModel: function(model) {
    	console.log(model);
    	doModel(model, function(msg){
    		return true;
    	}, false);
    }
});
