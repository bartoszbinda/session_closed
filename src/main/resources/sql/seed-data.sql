drop table if exists TIME_ZONE;
CREATE TABLE TIME_ZONE
(
    ID int NOT NULL,
    CODE VARCHAR(255) NOT NULL ,
    NAME VARCHAR(255) NOT NULL
);
ALTER TABLE TIME_ZONE
    ADD PRIMARY KEY (Id);