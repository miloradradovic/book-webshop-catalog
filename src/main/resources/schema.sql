drop table if exists genres;
drop table if exists writer_book;
drop table if exists books;
drop table if exists writers;

create table books (
	id integer not null auto_increment,
	in_stock integer not null,
	name varchar(255) not null,
	recap varchar(255) not null,
	year_released integer not null,
	price double precision not null,
	primary key (id)
);

create table genres (
	book_id integer not null,
	genre varchar(255) not null,
	primary key (book_id, genre)
);

create table writer_book (
	writer_id integer not null,
	book_id integer not null,
	primary key (writer_id, book_id)
);

create table writers (
	id integer not null auto_increment,
	biography varchar(255) not null,
	name varchar(255) not null,
	surname varchar(255) not null,
	primary key (id)
);

alter table genres
	add constraint FKqyfd9hv35djdbmoryimd7s7hs
    foreign key (book_id)
    references books (id);

alter table writer_book
	add constraint FKfekuq4iehufg09i367x51ydku
	foreign key (book_id)
	references books (id);

alter table writer_book
	add constraint FK2t4gg4xul59lngkq478coacei
	foreign key (writer_id)
	references writers (id);