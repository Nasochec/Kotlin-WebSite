CREATE TABLE READ_HISTORY(
    USER_LOGIN VARCHAR(40) NOT NULL,
    CONSTRAINT FK_READ_HISTORY_AUTHOR FOREIGN KEY (USER_LOGIN) REFERENCES AUTHOR(LOGIN)
                ON DELETE CASCADE ON UPDATE CASCADE,
    CHAPTER_BOOK_ID INTEGER NOT NULL,
    CHAPTER_NUMBER INTEGER NOT NULL,
    CONSTRAINT FK_READ_HISTORY_CHAPTER FOREIGN KEY (CHAPTER_BOOK_ID,CHAPTER_NUMBER) REFERENCES CHAPTER(BOOK_ID,NUMBER)
                ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY(
         USER_LOGIN,
         CHAPTER_BOOK_ID,
         CHAPTER_NUMBER
    )
);