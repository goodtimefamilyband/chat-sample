CREATE TABLE users (
	id INT PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255),
	created TIMESTAMP DEFAULT NOW(),
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
	posted TIMESTAMP DEFAULT NOW(),
	recipient INT,
	FOREIGN KEY (author) REFERENCES users (id)
		ON UPDATE CASCADE
		ON DELETE RESTRICT
);

CREATE INDEX idx_messages_posted ON messages (posted);

INSERT INTO roles (rolename) VALUES ('ROLE_USER');
