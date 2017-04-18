# Chat #

This application provides a server which allows for messaging in a public chat room, as well as private direct messaging.

## Running ##

The server is initialized with an empty database.  Create an account to login.  The methods below should be executed on a computer with an internet connection.

### Method 1 ###
With Vagrant (https://www.vagrantup.com/) installed, run `vagrant up` from a command line.  This will launch the application in an Ubuntu VM.  After several minutes, you should see something along the lines of "Started Main in xxxx seconds."  At this point, you may access the application at http://localhost:8081 .  Type Ctrl+C and `vagrant halt` to stop the server.  You may run `vagrant destroy` to start from scratch, as well as to delete the remnants of the VM when you are done.

### Method 2 ###
The application uses a MySQL database.  Connection information can be supplied in src/main/resources/Beans.xml; under the "dataSource" bean element, specify the database URL, username, and password for your database server in the "value" attribute property elements that have the corresponding "name" attributes.  Once this is configured, create the database on your server using the definition in asapp.sql. Run `./gradlew bootRun` from the project root, and look for the message specified in method 1.  The server can then be accessed at http://localhost:8080 .  

Note that the virtual machine may also function as a database server, and without modification to Beans.xml, the application is configured to use this database.  It may be run natively and use the VM as a database server; if you want to do this, comment out line 28 of the Vagrantfile.

URLs determined to point to images will be displayed in a chat as images with a width and height of 300px.  Links to YouTube videos are converted to embedded iframes.

## Specifications ##

This application was written in Java using Spring Boot.  It uses Gradle for dependency management and as a build tool.  The client front end was written in JavaScript, with jQuery, jQuery Forms, SockJS, and STOMP, the latter two being used for websocket support; custom client scripts can be found at src/main/webapp/resources/page/js.  The application does some server-side templating via JSP and JSTL.  Some features of Bootstrap were used for layout and element styling, along with some custom CSS, which can be found at src/main/webapp/resources/page/css/style.css.

### Endpoints ###
All paths under /app require the user to log in.

#### /login ####
Shows the login page.

#### /create ####
Create a new account.

#### /app/logout ####
Log out of the application.

#### /app/ ####
Shows the main chat page.  Messages can be sent using the form at the bottom.  Notifications about messages from individual users appear in the section that expands upon clicking "Direct Messages"; this button changes to "New Messages" if a direct message is received.

#### /app/{userid}/ ####
Displays messages for the user with ID {userid}, and allows sending private messages to that user via the form at the bottom.  Notifications about messages from individual users other than the one specified by {userid} appear in the "Direct Messages" section, as well as messages sent to the main chat.

#### /app/page/{pageNum}/ ####
Displays a limited number of messages with a pager, for page {pageNum}.  An optional "count" parameter may be supplied which specifies the number of messages per page; the default is 10.

#### /app/page/{userid}/{pageNum}/ ####
Same as above, but displays the paged message history for the user with ID {userid}.

#### {Any of the above}/messages ####
Returns serialized JSON object, subject to corresponding constraints, with the following keys:
* messages: An array of message objects to be displayed in the main area. Message objects possess the following keys:
** authorId: The ID of the user who sent this message.
** authorName: The username of the user who sent this message.
** html: The text or HTML that should be displayed in a chat window.
** posted: A Unix timestamp indicating when this message was saved to the database.
** recipient: The ID of the user for whom this message was intended, or null if it was sent to the main chat.
** text: The original text sent by the author of the message to the server.
* unseen: An array of unseen messages.  These are used to show notifications on page load.  Another client making use of these endpoints may display the messages individually.

#### /app/messages (POST) ####
Sends a message to the main chat. The request must contain a "msg" parameter specifying the content of the message.  Other logged in users will see or be made aware of this message via a websocket connection.

#### /app/{userid}/messages (POST) ####
Same as above, but sends a message to a particular user.
  
#### /app/lastseen ####
Indicate a timestamp such that all messages in the main chat bearing a timestamp thereafter are unseen.  The request must contain a "tstamp" parameter specifying a Unix timestamp.
  
#### /app/{userid}/lastseen ####
Same as above, but for messages in a private converstation with a user bearing userid {userid}.

### Websocket Client ###
The client's constructor should be passed an object with the following properties:
* onMessage: function (message) {} : Called when a message arrives in main chat.  Message is an unserialized message object, the same as those found in the */messages endpoints.
* onUserMessage: function (message) {} : Called when a direct message arrives.  
* onModel: function (model) {} : Called when the client first connects, and makes a request to a corresponding /messages endpoint.

## TODO ##
It would be nice if the front end leveraged websocket events to indicate connection status to the user.
