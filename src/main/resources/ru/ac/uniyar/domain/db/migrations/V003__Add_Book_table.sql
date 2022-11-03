CREATE TABLE BOOK (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME       VARCHAR (100) NOT NULL,
    AUTHOR_ID  INTEGER NOT NULL,
    CONSTRAINT FK_BOOK_AUTHOR FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHOR(ID)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CREATION_DATE DATETIME NOT NULL,
    RATING     VARCHAR (10) NOT NULL CHECK (RATING in ('BABY', 'CHILD', 'TEENAGE','YOUNG','ADULT')),
    FORMAT     VARCHAR (50) NOT NULL CHECK(FORMAT in (
                                    'HARDCOVER',
                                    'PAPERBACK',
                                    'WEBBOOK',
                                    'ARTICLE',
                                    'WEBARTICKLE'
                                    )
                                ),
    ANNOTATION CHARACTER LARGE OBJECT NOT NULL,
    GENRE_ID INTEGER NOT NULL,
    CONSTRAINT FK_BOOK_GENRE FOREIGN KEY (GENRE_ID) REFERENCES GENRE(ID)
);
