/**
 * 
 */


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
