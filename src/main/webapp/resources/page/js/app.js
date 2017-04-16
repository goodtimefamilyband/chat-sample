/**
 * 
 */


var client = new AsappClient({
	onMessage: function (message) {
		showMain(message);
    },
    onModel: function(model) {
    	console.log(model);
    	for(i in model.messages) {
    		$('#messages').append(render(model.messages[i]));
    	}
    	scrollDown();
    },
    onUserMessage: function(message) {
    	console.log("OnUserMessage", message);
    	
    	showNote(message);
    }
});
