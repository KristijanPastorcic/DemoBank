CREATE TABLE Persons (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    oib VARCHAR(255) NOT NULL UNIQUE,
    status smallint
);

CREATE TABLE Files (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    fk_oib BIGINT, foreign key (fk_oib) references Persons(oib)
);

insert into persons (name ,last_name ,oib, status) values('Marko', 'Markić', 12345678901, 1);
insert into persons (name ,last_name ,oib, status) values('Ivo', 'Ivić', 12345678902, 1);