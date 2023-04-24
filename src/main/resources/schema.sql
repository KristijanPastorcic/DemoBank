CREATE TABLE Persons
(
    id        INTEGER AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    oib       VARCHAR(11) NOT NULL UNIQUE,
    status    VARCHAR(20)
);

CREATE TABLE Files
(
    id        INTEGER AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    fk_oib    VARCHAR(11),
    foreign key (fk_oib) references Persons (oib)
);

insert into persons (name, last_name, oib, status)
values ('Marko', 'Markić', 12345678901, 'INACTIVE');
insert into persons (name, last_name, oib, status)
values ('Ivo', 'Ivić', 12345678902, 'INACTIVE');