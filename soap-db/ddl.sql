create table logging (
	id int not null auto_increment,
	description char(255) not null,
	ip char(16) not null,
	endpoint char(255) not null,
	requested_at timestamp not null,
	PRIMARY KEY (id)
);

create table subscription (
	creator_id int not null,
	subscriber_id int not null,
	status enum ('PENDING', 'ACCEPTED', 'REJECTED') default 'PENDING',
	primary key (creator_id, subscriber_id)
);

create table apikey (
	uid int not null,
	api_key char(255) not null,
	primary key (uid)
);