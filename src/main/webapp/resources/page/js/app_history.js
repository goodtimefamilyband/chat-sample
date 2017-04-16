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
    	for(i in model.messages) {
    		$('#messages').append(render(model.messages[i]));
    	}
    	scrollDown();
    }
});
