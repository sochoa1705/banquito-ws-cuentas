DROP TABLE BANK_ENTITY;
CREATE TABLE BANK_ENTITY(
BANK_ENTITY_ID INTEGER NOT NULL,
NAME VARCHAR(100) NOT NULL,
INTERNATIONAL_CODE	VARCHAR(20)	NOT NULL,
VERSION	INT4	NOT NULL DEFAULT 0,
CONSTRAINT PK_BANK_ENTITY PRIMARY KEY (BANK_ENTITY_ID)
);

INSERT INTO BANK_ENTITY (BANK_ENTITY_ID,NAME,INTERNATIONAL_CODE) VALUES ('1','Banco BanQuito','BANQUITOXXX');