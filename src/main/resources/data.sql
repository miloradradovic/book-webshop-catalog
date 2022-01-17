insert into books (in_stock, name, recap, year_released, price) values
(3, "Bookk", "Recapp", 1999, 30.0);
insert into books (in_stock, name, recap, year_released, price) values
(5, "Bookkk", "Recappp", 1998, 60.0);

insert into genres (book_id, genre) values (1, "SCI_FI");
insert into genres (book_id, genre) values (1, "COMEDY");
insert into genres (book_id, genre) values (2, "SCIENCE");

insert into writers (biography, name, surname) values
("Biography", "Name", "Surname");
insert into writers (biography, name, surname) values
("Biographyy", "Namee", "Surnamee");

insert into writer_book (writer_id, book_id) values
(1, 1);
insert into writer_book (writer_id, book_id) values
(2, 1);
insert into writer_book (writer_id, book_id) values
(1, 2);