/* Requires MySQL 5.6+ */

CREATE TABLE users (
	id INT PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255),
	created BIGINT NOT NULL,
	enabled BIT DEFAULT TRUE
);

CREATE INDEX idx_users_username_password ON users (username, password);
CREATE INDEX idx_users_username ON users (username);

CREATE TABLE roles (
	id INT PRIMARY KEY AUTO_INCREMENT,
	rolename VARCHAR(255) NOT NULL
);

CREATE TABLE role_user (
	userid INT NOT NULL,
	roleid INT NOT NULL,
	PRIMARY KEY(userid, roleid),
	FOREIGN KEY (userid) REFERENCES users (id)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
	FOREIGN KEY (roleid) REFERENCES roles (id)
		ON UPDATE CASCADE
		ON DELETE CASCADE
);

CREATE TABLE messages (
	id INT PRIMARY KEY AUTO_INCREMENT,
	author INT NOT NULL,
	text TEXT NOT NULL,
	posted BIGINT NOT NULL,
	recipient INT,
	FOREIGN KEY (author) REFERENCES users (id)
		ON UPDATE CASCADE
		ON DELETE RESTRICT
);

CREATE TABLE image_data (
	msgid INT PRIMARY KEY AUTO_INCREMENT,
	width INT NOT NULL,
	height INT NOT NULL,
	FOREIGN KEY (msgid) REFERENCES messages(id)
		ON UPDATE CASCADE
		ON DELETE CASCADE
);

CREATE TABLE video_data (
	msgid INT PRIMARY KEY AUTO_INCREMENT,
	len INT NOT NULL,
	source VARCHAR(255) NOT NULL,
	html TEXT NOT NULL,
	FOREIGN KEY (msgid) REFERENCES messages(id)
		ON UPDATE CASCADE
		ON DELETE CASCADE
);

CREATE INDEX idx_messages_posted ON messages (posted);

INSERT INTO roles (rolename) VALUES ('ROLE_USER');
