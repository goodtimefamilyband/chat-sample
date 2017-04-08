/**
 * 
 */

var stompClient = null;

var AsappClient = function() {
	this.connected = false;
	
	var url = window.location.pathname + 'messaging';
	console.log(url);
	var connectedFxn = function(frame) {
		this.connected = true;
		console.log('Connected: ' + frame);
        this.stompClient.subscribe(url, function (message) {
        	console.log(message.body);
            //showGreeting(message.body);
        });
	};
	
	connectedFxn = connectedFxn.bind(this);
	
	this.socket = new SockJS("/messaging");
	this.stompClient = new Stomp.over(this.socket);
	this.stompClient.connect({}, connectedFxn);
}
