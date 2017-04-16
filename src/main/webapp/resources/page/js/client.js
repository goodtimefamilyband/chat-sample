/**
 * 
 */

var stompClient = null;

var AsappClient = function(clientopts) {
	this.connected = false;
	
	var opts = {
		onMessage: function (message) {},
		onUserMessage: function (message) {},
		onModel: function (model) {}
	}
	
	for(var k in clientopts) {
		opts[k] = clientopts[k];
	}
	
	$.ajax("messages", {dataType: "json"})
		.done(function(data, status, xhr) {
			opts.onModel(data);
		})
		.fail(function(xhr, status) {
			console.log("fail");
			console.log(status);
		});
	
	var connectedFxn = function(frame) {
		this.connected = true;
		console.log('Connected: ' + frame);
        this.stompClient.subscribe('/app/messaging', function (data) {
        	console.log("Subscribe", data, typeof data.body);
        	opts.onMessage(JSON.parse(data.body));
        });
        
        this.stompClient.subscribe("/user/app/messaging", function (data) {
        	console.log("User message: " + data);
        	opts.onUserMessage(JSON.parse(data.body));
        });
        
        
	};
	
	connectedFxn = connectedFxn.bind(this);
	
	this.socket = new SockJS("/messaging");
	this.stompClient = new Stomp.over(this.socket);
	this.stompClient.connect({}, connectedFxn);
}
