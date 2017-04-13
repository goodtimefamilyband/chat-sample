/**
 * 
 */

var stompClient = null;

var AsappClient = function(opts) {
	this.connected = false;
	
	var render = function(message) {
		console.log("Render", message);
		var msgDiv = $(document.createElement('div'));
		msgDiv.attr('class', 'message');
		
		var msgTime = new Date(message.posted*1000);
		
		var timeSpan = $(document.createElement('span'));
		timeSpan.html(msgTime.toLocaleString());
		msgDiv.append(timeSpan);
		
		var authorSpan = $(document.createElement('span'));
		authorSpan.html(message.authorName);
		msgDiv.append(authorSpan);
		
		var txtSpan = $(document.createElement('span'));
		txtSpan.html(message.html);
		msgDiv.append(txtSpan);
		
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
	
	var connectedFxn = function(frame) {
		this.connected = true;
		console.log('Connected: ' + frame);
        this.stompClient.subscribe('/app/messaging', function (data) {
        	console.log("Subscribe", data, typeof data.body);
        	opts.onMessage(render(JSON.parse(data.body)));
        });
        
        this.stompClient.subscribe("/user/app/messaging", function (data) {
        	console.log("User message: " + data);
        	
        });
	};
	
	connectedFxn = connectedFxn.bind(this);
	
	this.socket = new SockJS("/messaging");
	this.stompClient = new Stomp.over(this.socket);
	this.stompClient.connect({}, connectedFxn);
}
