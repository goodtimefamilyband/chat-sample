/**
 * 
 */

var stompClient = null;

var AsappClient = function(opts) {
	this.connected = false;
	
	var render = function(message) {
		console.log("Render", message);
		var msgDiv = $(document.createElement('div'));
		msgDiv.html(message.text);
		return msgDiv;
	}
	
	this.render = render;
	
	$.ajax("messages", {dataType: "json"})
		.done(function(data, status, xhr) {
			for(i in data.messages) {
				data.messages[i] = render(data.messages[i]);
			}
			opts.onModel(data);
		})
		.fail(function(xhr, status) {
			console.log("fail");
			console.log(status);
		});
	var url = window.location.pathname + 'messaging';
	console.log(url);
	var connectedFxn = function(frame) {
		this.connected = true;
		console.log('Connected: ' + frame);
        this.stompClient.subscribe(url, function (data) {
        	console.log("Subscribe", data, typeof data.body);
        	opts.onMessage(render(JSON.parse(data.body)));
        });
	};
	
	connectedFxn = connectedFxn.bind(this);
	
	this.socket = new SockJS("/messaging");
	this.stompClient = new Stomp.over(this.socket);
	this.stompClient.connect({}, connectedFxn);
}
