CREATE TABLE AUTHOR (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME VARCHAR (100) NOT NULL,
    CREATION_DATE DATETIME NOT NULL
);