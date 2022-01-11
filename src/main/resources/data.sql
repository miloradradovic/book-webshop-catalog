insert into books (in_stock, name, recap, year_released, price) values
(3, "Book1", "Recap1", 1999, 30.0);

insert into genres (book_id, genre) values (1, "SCI_FI");
insert into genres (book_id, genre) values (1, "COMEDY");

insert into writers (biography, name, surname) values
("Biography1", "Name1", "Surname1");
insert into writers (biography, name, surname) values
("Biography2", "Name2", "Surname2");

insert into writer_book (writer_id, book_id) values
(1, 1);
insert into writer_book (writer_id, book_id) values
(2, 1);