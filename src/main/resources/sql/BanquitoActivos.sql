
/*==============================================================*/
/* Table: ACCOUNT                                               */
/*==============================================================*/
CREATE TABLE ACCOUNT (
   ACCOUNT_ID           SERIAL NOT NULL,
   PRODUCT_ACCOUNT_ID   INT4                 NOT NULL,
   GROUP_ID             INT4                 NULL,
   CUSTOMER_ID          INT4                 NULL,
   BRANCH_ID            INT4                 NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   CODE_INTERNAL_ACCOUNT VARCHAR(8)           NOT NULL,
   CODE_INTERNATIONAL_ACCOUNT VARCHAR(16)          NOT NULL,
   ACCOUNT_HOLDER_TYPE  VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_ACCOUNT_HOLDER_TY_ACCOUNT CHECK (ACCOUNT_HOLDER_TYPE IN ('CUS','GRO')),
   ACCOUNT_HOLDER_CODE  VARCHAR(36)          NOT NULL,
   NAME                 VARCHAR(50)          NOT NULL,
   TOTAL_BALANCE        NUMERIC(18,2)        NOT NULL DEFAULT 0,
   AVAILABLE_BALANCE    NUMERIC(18,2)        NOT NULL DEFAULT 0,
   BLOCKED_BALANCE      NUMERIC(18,2)        NOT NULL,
   STATE                VARCHAR(3)           NOT NULL DEFAULT 'ACT'
      CONSTRAINT CKC_STATE_ACCOUNT CHECK (STATE IN ('INA','ACT','BLO','SUS') AND STATE = UPPER(STATE)),
   CREATION_DATE        TIMESTAMP            NOT NULL,
   ACTIVATION_DATE      TIMESTAMP            NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NULL,
   LAST_INTEREST_CALCULATION_DATE TIMESTAMP            NULL,
   ALLOW_OVERDRAFT      BOOL                 NOT NULL,
   MAX_OVERDRAFT        NUMERIC(18,2)        NULL,
   CLOSED_DATE          TIMESTAMP            NULL,
   INTEREST_RATE        NUMERIC(5,2)         NOT NULL,
   VERSION              INT4                 NULL DEFAULT 0,
   CONSTRAINT PK_ACCOUNT PRIMARY KEY (ACCOUNT_ID)
);

/*==============================================================*/
/* Index: IDX_ACC_CUST                                          */
/*==============================================================*/
CREATE  INDEX IDX_ACC_CUST ON ACCOUNT (
CUSTOMER_ID,
ACCOUNT_HOLDER_CODE
);

/*==============================================================*/
/* Index: IDX_ACC_COMP                                          */
/*==============================================================*/
CREATE  INDEX IDX_ACC_COMP ON ACCOUNT (
GROUP_ID,
ACCOUNT_HOLDER_CODE
);

/*==============================================================*/
/* Index: IDX_ACC                                               */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_ACC ON ACCOUNT (
UNIQUE_KEY,
NAME,
CREATION_DATE
);

/*==============================================================*/
/* Table: ACCOUNT_DOCUMENT                                      */
/*==============================================================*/
CREATE TABLE ACCOUNT_DOCUMENT (
   ACCOUNT_DOCUMENT_ID  SERIAL NOT NULL,
   DOCUMENT_TYPE_ID     VARCHAR(10)          NOT NULL,
   ACCOUNT_ID           INT4                 NOT NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   NAME                 VARCHAR(100)         NOT NULL,
   URL                  VARCHAR(250)         NOT NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_ACCOUNT_DOCUMENT PRIMARY KEY (ACCOUNT_DOCUMENT_ID)
);

/*==============================================================*/
/* Index: IDX_ACCDOC_TYPE                                       */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_ACCDOC_TYPE ON ACCOUNT_DOCUMENT (
DOCUMENT_TYPE_ID,
NAME,
ACCOUNT_ID
);

/*==============================================================*/
/* Table: ACCOUNT_INTEREST_ACCRUED                              */
/*==============================================================*/
CREATE TABLE ACCOUNT_INTEREST_ACCRUED (
   ACCOUNT_INTEREST_ACCRUED_ID SERIAL NOT NULL,
   ACCOUNT_ID           INT4                 NOT NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   EXECUTION_DATE       TIMESTAMP            NOT NULL,
   AMOUNT               NUMERIC(18,2)        NOT NULL,
   INTEREST_RATE        NUMERIC(5,2)         NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_ACCOUNT_INTEREST_ACCRUED PRIMARY KEY (ACCOUNT_INTEREST_ACCRUED_ID)
);

/*==============================================================*/
/* Table: ACCOUNT_TRANSACTION                                   */
/*==============================================================*/
CREATE TABLE ACCOUNT_TRANSACTION (
   ACCOUNT_TRANSACTION_ID SERIAL NOT NULL,
   ACCOUNT_ID           INT4                 NOT NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   TRANSACTION_TYPE     VARCHAR(12)          NOT NULL
      CONSTRAINT CKC_TRANSACTION_TYPE_ACCOUNT_ CHECK (TRANSACTION_TYPE IN ('ADJUSTMENT','WITHDRAWAL','TRANSFER','PAYMENT','FEE_APPLIED','INTEREST_AP','WITHHOLD_TA','LOAN_FUNDED','LOAN_REPAID')),
   REFERENCE            VARCHAR(50)          NOT NULL,
   AMOUNT               NUMERIC(18,2)        NOT NULL,
   CREDITOR_BANK_CODE   VARCHAR(20)          NULL,
   CREDITOR_ACCOUNT     VARCHAR(16)          NULL,
   DEBTOR_BANK_CODE     VARCHAR(20)          NULL,
   DEBTOR_ACCOUNT       VARCHAR(16)          NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   BOOKING_DATE         TIMESTAMP            NOT NULL,
   VALUE_DATE           TIMESTAMP            NOT NULL,
   APPLY_TAX            BOOL                 NOT NULL,
   PARENT_TRANSACTION_KEY VARCHAR(36)          NULL,
   STATE                VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_STATE_ACCOUNT_ CHECK (STATE IN ('POS','EXE','REV') AND STATE = UPPER(STATE)),
   NOTES                VARCHAR(200)         NULL,
   VERSION              INT4                 NOT NULL DEFAULT 00,
   CONSTRAINT PK_ACCOUNT_TRANSACTION PRIMARY KEY (ACCOUNT_TRANSACTION_ID)
);

/*==============================================================*/
/* Table: AMORTIZATION                                          */
/*==============================================================*/
CREATE TABLE AMORTIZATION (
   AMORTIZATION_ID      SERIAL NOT NULL,
   TYPE                 VARCHAR(2)           NOT NULL
      CONSTRAINT CKC_TYPE_AMORTIZA CHECK (TYPE IN ('DE','FR')),
   TERM                 INT2                 NOT NULL,
   QUOTE                NUMERIC(18,2)        NOT NULL,
   CAPITAL              NUMERIC(18,2)        NOT NULL,
   TAX                  NUMERIC(18,2)        NOT NULL,
   PENDING_BALANCE      NUMERIC(18,2)        NULL,
   PERIOD_UNIT          VARCHAR(10)          NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_AMORTIZATION PRIMARY KEY (AMORTIZATION_ID)
);

/*==============================================================*/
/* Table: ASSET                                                 */
/*==============================================================*/
CREATE TABLE ASSET (
   ASSET_ID             SERIAL               NOT NULL,
   AMOUNT               NUMERIC(18,2)        NOT NULL,
   GUARANTOR_CODE       VARCHAR(36)          NOT NULL,
   GUARANTOR_TYPE       VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_GUARANTOR_TYPE_ASSET CHECK (GUARANTOR_TYPE IN ('CUS','GCU','GCO') AND GUARANTOR_TYPE = UPPER(GUARANTOR_TYPE)),
   CURRENCY             VARCHAR(3)           NULL
      CONSTRAINT CKC_CURRENCY_ASSET CHECK (CURRENCY IS NULL OR (CURRENCY IN ('EUR','USD') AND CURRENCY = UPPER(CURRENCY))),
   VERSION              INT4                 NOT NULL,
   CONSTRAINT PK_ASSET PRIMARY KEY (ASSET_ID)
);

/*==============================================================*/
/* Table: BANK_ENTITY                                           */
/*==============================================================*/
CREATE TABLE BANK_ENTITY (
   BANK_ENTITY_ID       SERIAL NOT NULL,
   NAME                 VARCHAR(100)         NOT NULL,
   INTERNATIONAL_CODE   VARCHAR(20)          NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_BANK_ENTITY PRIMARY KEY (BANK_ENTITY_ID)
);

/*==============================================================*/
/* Index: IDX_BNKENT_INTCODE                                    */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_BNKENT_INTCODE ON BANK_ENTITY (
INTERNATIONAL_CODE,
NAME
);

/*==============================================================*/
/* Table: BRANCH                                                */
/*==============================================================*/
CREATE TABLE BRANCH (
   BRANCH_ID            SERIAL NOT NULL,
   BANK_ENTITY_ID       INT2                 NOT NULL,
   LOCATION_ID          INT4                 NOT NULL,
   CODE                 VARCHAR(10)          NOT NULL,
   NAME                 VARCHAR(100)         NOT NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   STATE                VARCHAR(3)           NOT NULL DEFAULT 'ACT'
      CONSTRAINT CKC_STATE_BRANCH CHECK (STATE IN ('ACT','INA') AND STATE = UPPER(STATE)),
   CREATION_DATE        TIMESTAMP            NOT NULL,
   EMAIL_ADDRESS        VARCHAR(100)         NOT NULL,
   PHONE_NUMBER         VARCHAR(20)          NULL,
   LINE1                VARCHAR(100)         NOT NULL,
   LINE2                VARCHAR(100)         NULL,
   LATITUDE             FLOAT4               NULL,
   LONGITUDE            FLOAT4               NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_BRANCH PRIMARY KEY (BRANCH_ID)
);

/*==============================================================*/
/* Index: IDX_BRA_CODE                                          */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_BRA_CODE ON BRANCH (
CODE,
NAME
);

/*==============================================================*/
/* Table: CUSTOMER                                              */
/*==============================================================*/
CREATE TABLE CUSTOMER (
   CUSTOMER_ID          SERIAL NOT NULL,
   BRANCH_ID            INT4                 NOT NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   TYPE_DOCUMENT_ID     VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_TYPE_DOCUMENT_ID_CUSTOMER CHECK (TYPE_DOCUMENT_ID IN ('CID','PASS','RUC') AND TYPE_DOCUMENT_ID = UPPER(TYPE_DOCUMENT_ID)),
   DOCUMENT_ID          VARCHAR(20)          NOT NULL,
   FIRST_NAME           VARCHAR(50)          NOT NULL,
   LAST_NAME            VARCHAR(50)          NOT NULL,
   GENDER               VARCHAR(3)           NOT NULL,
   BIRTH_DATE           DATE                 NOT NULL,
   EMAIL_ADDRESS        VARCHAR(100)         NOT NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   ACTIVATION_DATE      TIMESTAMP            NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   STATE                VARCHAR(3)           NOT NULL DEFAULT 'ACT'
      CONSTRAINT CKC_STATE_CUSTOMER CHECK (STATE IN ('ACT','INA','SUS','BLO','PRO') AND STATE = UPPER(STATE)),
   CLOSED_DATE          TIMESTAMP            NULL,
   COMMENTS             VARCHAR(500)         NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_CUSTOMER PRIMARY KEY (CUSTOMER_ID)
);

/*==============================================================*/
/* Index: IDX_CUST_TYP_DOC                                      */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_CUST_TYP_DOC ON CUSTOMER (
TYPE_DOCUMENT_ID,
DOCUMENT_ID
);

/*==============================================================*/
/* Table: CUSTOMER_ADDRESS                                      */
/*==============================================================*/
CREATE TABLE CUSTOMER_ADDRESS (
   CUSTOMER_ADDRESS_ID  SERIAL NOT NULL,
   CUSTOMER_ID          INT4                 NULL,
   LOCATION_ID          INT4                 NOT NULL,
   TYPE_ADDRESS         VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_TYPE_ADDRESS_CUSTOMER CHECK (TYPE_ADDRESS IN ('HOM','OFF','OTH')),
   LINE1                VARCHAR(100)         NOT NULL,
   LINE2                VARCHAR(100)         NULL,
   LATITUDE             FLOAT4               NULL,
   LONGITUDE            FLOAT4               NULL,
   IS_DEFAULT           BOOL                 NOT NULL,
   STATE                VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_STATE_CUSTOMER CHECK (STATE IN ('ACT','INA') AND STATE = UPPER(STATE)),
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_CUSTOMER_ADDRESS PRIMARY KEY (CUSTOMER_ADDRESS_ID)
);

/*==============================================================*/
/* Table: CUSTOMER_PHONE                                        */
/*==============================================================*/
CREATE TABLE CUSTOMER_PHONE (
   CUSTOMER_PHONE_ID    SERIAL NOT NULL,
   CUSTOMER_ID          INT4                 NULL,
   PHONE_TYPE           VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_PHONE_TYPE_CUSTOMER CHECK (PHONE_TYPE IN ('HOM','MOB','OFF','OTH')),
   PHONE_NUMBER         VARCHAR(20)          NOT NULL,
   IS_DEFAULT           BOOL                 NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_CUSTOMER_PHONE PRIMARY KEY (CUSTOMER_PHONE_ID)
);

/*==============================================================*/
/* Table: DOCUMENT_TYPE                                         */
/*==============================================================*/
CREATE TABLE DOCUMENT_TYPE (
   DOCUMENT_TYPE_ID     VARCHAR(10)          NOT NULL,
   NAME                 VARCHAR(100)         NOT NULL,
   APPLICABILITY        VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_APPLICABILITY_DOCUMENT CHECK (APPLICABILITY IN ('CUS','GCU','GCO')),
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_DOCUMENT_TYPE PRIMARY KEY (DOCUMENT_TYPE_ID)
);

/*==============================================================*/
/* Table: GEO_COUNTRY                                           */
/*==============================================================*/
CREATE TABLE GEO_COUNTRY (
   COUNTRY_ID           VARCHAR(3)           NOT NULL,
   PHONE_CODE           VARCHAR(4)           NOT NULL,
   NAME                 VARCHAR(50)          NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_GEO_COUNTRY PRIMARY KEY (COUNTRY_ID)
);

/*==============================================================*/
/* Table: GEO_LOCATION                                          */
/*==============================================================*/
CREATE TABLE GEO_LOCATION (
   LOCATION_ID          SERIAL NOT NULL,
   LOCATION_ID_PARENT   INT4                 NULL,
   COUNTRY_ID           VARCHAR(3)           NULL,
   LEVEL_CODE           NUMERIC(1)           NULL,
   NAME                 VARCHAR(50)          NOT NULL,
   AREA_PHONE_CODE      VARCHAR(3)           NULL,
   ZIP_CODE             VARCHAR(10)          NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_GEO_LOCATION PRIMARY KEY (LOCATION_ID)
);

/*==============================================================*/
/* Index: IDX_U_LOCATION                                        */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_U_LOCATION ON GEO_LOCATION (
LOCATION_ID
);

/*==============================================================*/
/* Table: GEO_STRUCTURE                                         */
/*==============================================================*/
CREATE TABLE GEO_STRUCTURE (
   COUNTRY_ID           VARCHAR(3)           NOT NULL,
   LEVEL_CODE           NUMERIC(1)           NOT NULL,
   NAME                 VARCHAR(50)          NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_GEO_STRUCTURE PRIMARY KEY (COUNTRY_ID, LEVEL_CODE)
);

/*==============================================================*/
/* Table: GROUP_COMPANY                                         */
/*==============================================================*/
CREATE TABLE GROUP_COMPANY (
   GROUP_COMPANY_ID     SERIAL NOT NULL,
   BRANCH_ID            INT4                 NOT NULL,
   LOCATION_ID          INT4                 NOT NULL,
   UNIQUE_KEY           VARCHAR(36)          NULL,
   GROUP_NAME           VARCHAR(200)         NOT NULL,
   EMAIL_ADDRESS        VARCHAR(100)         NOT NULL,
   PHONE_NUMBER         VARCHAR(20)          NOT NULL,
   LINE1                VARCHAR(100)         NOT NULL,
   LINE2                VARCHAR(100)         NOT NULL,
   LATITUDE             FLOAT4               NOT NULL,
   LONGITUDE            FLOAT4               NOT NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   ACTIVATION_DATE      TIMESTAMP            NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   STATE                VARCHAR(3)           NOT NULL DEFAULT 'CRE'
      CONSTRAINT CKC_STATE_GROUP_CO CHECK (STATE IN ('CRE','ACT','INA','SUS','BLO') AND STATE = UPPER(STATE)),
   CLOSED_DATE          TIMESTAMP            NULL,
   COMMENTS             VARCHAR(500)         NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_GROUP_COMPANY PRIMARY KEY (GROUP_COMPANY_ID)
);

/*==============================================================*/
/* Index: IDX_GRPCOMP_CRE_DATE                                  */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_GRPCOMP_CRE_DATE ON GROUP_COMPANY (
CREATION_DATE,
BRANCH_ID,
LOCATION_ID
);

/*==============================================================*/
/* Table: GROUP_COMPANY_MEMBER                                  */
/*==============================================================*/
CREATE TABLE GROUP_COMPANY_MEMBER (
   GROUP_COMPANY_ID     INT4                 NOT NULL,
   GROUP_ROLE_ID        VARCHAR(10)          NOT NULL,
   CUSTOMER_ID          INT4                 NOT NULL,
   STATE                VARCHAR(3)           NOT NULL DEFAULT 'ACT'
      CONSTRAINT CKC_STATE_GROUP_CO CHECK (STATE IN ('ACT','INA') AND STATE = UPPER(STATE)),
   CREATION_DATE        TIMESTAMP            NOT NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_GROUP_COMPANY_MEMBER PRIMARY KEY (GROUP_COMPANY_ID, GROUP_ROLE_ID, CUSTOMER_ID)
);

/*==============================================================*/
/* Table: GROUP_ROLE                                            */
/*==============================================================*/
CREATE TABLE GROUP_ROLE (
   GROUP_ROLE_ID        VARCHAR(10)          NOT NULL,
   GROUP_ROLE_NAME      VARCHAR(50)          NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_GROUP_ROLE PRIMARY KEY (GROUP_ROLE_ID)
);

/*==============================================================*/
/* Table: GUARANTOR                                             */
/*==============================================================*/
CREATE TABLE GUARANTOR (
   GUARANTOR_ID         SERIAL NOT NULL,
   CODE                 VARCHAR(36)          NOT NULL,
   TYPE                 VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_TYPE_GUARANTO CHECK (TYPE IN ('CUS','GCU','GCO') AND TYPE = UPPER(TYPE)),
   NAME                 VARCHAR(30)          NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_GUARANTOR PRIMARY KEY (GUARANTOR_ID)
);

/*==============================================================*/
/* Table: HOLIDAY                                               */
/*==============================================================*/
CREATE TABLE HOLIDAY (
   HOLIDAY_ID           SERIAL NOT NULL,
   HOLIDAY_DATE         DATE                 NOT NULL,
   LOCATION_ID          INT4                 NOT NULL,
   COUNTRY_ID           VARCHAR(3)           NULL,
   NAME                 VARCHAR(100)         NOT NULL,
   TYPE                 VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_TYPE_HOLIDAY CHECK (TYPE IN ('REG','NAT')),
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_HOLIDAY PRIMARY KEY (HOLIDAY_ID)
);

/*==============================================================*/
/* Index: IDXU_HOLI_DATE                                        */
/*==============================================================*/
CREATE UNIQUE INDEX IDXU_HOLI_DATE ON HOLIDAY (
HOLIDAY_DATE,
LOCATION_ID
);

/*==============================================================*/
/* Table: INTEREST_ACCRUE                                       */
/*==============================================================*/
CREATE TABLE INTEREST_ACCRUE (
   INTEREST_ACCRUE_ID   SERIAL NOT NULL,
   INTEREST_RATE        NUMERIC(18,2)        NOT NULL,
   INTEREST_TYPE        VARCHAR(10)          NOT NULL,
   SPREAD               NUMERIC(18,2)        NULL,
   CHARGE_FRECUENCY     VARCHAR(10)          NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_INTEREST_ACCRUE PRIMARY KEY (INTEREST_ACCRUE_ID)
);

/*==============================================================*/
/* Table: LOAN                                                  */
/*==============================================================*/
CREATE TABLE LOAN (
   LOAN_ID              SERIAL NOT NULL,
   GROUP_COMPANY_ID     INT4                 NULL,
   CUSTOMER_ID          INT4                 NULL,
   INTEREST_ACCRUE_ID   INT4                 NOT NULL,
   GUARANTOR_ID         INT4                 NULL,
   BRANCH_ID            INT4                 NULL,
   LOAN_PRODUCT_ID      INT4                 NOT NULL,
   ASSET_ID             INT4                 NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   LOAN_HOLDER_TYPE     VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_LOAN_HOLDER_TYPE_LOAN CHECK (LOAN_HOLDER_TYPE IN ('CUS','GRO') AND LOAN_HOLDER_TYPE = UPPER(LOAN_HOLDER_TYPE)),
   LOAN_HOLDER_CODE     VARCHAR(32)          NOT NULL,
   NAME                 VARCHAR(50)          NOT NULL,
   AMOUNT               NUMERIC(18,2)        NOT NULL,
   TERM                 INT2                 NOT NULL,
   GRACE_PERIOD         INT2                 NOT NULL,
   GRACE_PERIOD_TYPE    VARCHAR(10)          NOT NULL,
   STATUS               VARCHAR(3)           NULL
      CONSTRAINT CKC_STATUS_LOAN CHECK (STATUS IS NULL OR (STATUS IN ('APR','DEN','PEN') AND STATUS = UPPER(STATUS))),
   APPROVAL_DATE        DATE                 NOT NULL,
   DUE_DATE             DATE                 NOT NULL,
   MONTHLY_FEE          NUMERIC(18,2)        NOT NULL,
   DAYS_LATE            INT2                 NULL,
   INTEREST_RATE        NUMERIC(18,2)        NOT NULL,
   REDRAW               BOOL                 NULL,
   REDRAW_BALANCE       NUMERIC(18,2)        NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_LOAN PRIMARY KEY (LOAN_ID)
);

/*==============================================================*/
/* Index: IDX_LOAN_APPDATE                                      */
/*==============================================================*/
CREATE  INDEX IDX_LOAN_APPDATE ON LOAN (
AMOUNT,
STATUS,
APPROVAL_DATE
);

/*==============================================================*/
/* Table: LOAN_DOCUMENT                                         */
/*==============================================================*/
CREATE TABLE LOAN_DOCUMENT (
   LOAN_DOCUMENT_ID     SERIAL NOT NULL,
   LOAN_DOCUMENT_TYPE_ID VARCHAR(10)          NOT NULL,
   LOAN_ID              INT4                 NOT NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   NAME                 VARCHAR(100)         NOT NULL,
   URL                  VARCHAR(250)         NOT NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_LOAN_DOCUMENT PRIMARY KEY (LOAN_DOCUMENT_ID)
);

/*==============================================================*/
/* Index: IDX_LOAN_TYPE                                         */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_LOAN_TYPE ON LOAN_DOCUMENT (
LOAN_DOCUMENT_TYPE_ID,
LOAN_ID,
UNIQUE_KEY
);

/*==============================================================*/
/* Table: LOAN_DOCUMENT_TYPE                                    */
/*==============================================================*/
CREATE TABLE LOAN_DOCUMENT_TYPE (
   LOAN_DOCUMENT_TYPE_ID VARCHAR(10)          NOT NULL,
   NAME                 VARCHAR(100)         NOT NULL,
   AVAILABILITY         VARCHAR(3)           NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_LOAN_DOCUMENT_TYPE PRIMARY KEY (LOAN_DOCUMENT_TYPE_ID)
);

/*==============================================================*/
/* Table: LOAN_PRODUCT                                          */
/*==============================================================*/
CREATE TABLE LOAN_PRODUCT (
   LOAN_PRODUCT_ID      SERIAL NOT NULL,
   LOAN_PRODUCT_TYPE_ID VARCHAR(36)          NOT NULL,
   AMORTIZATION_ID      INT4                 NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   NAME                 VARCHAR(50)          NOT NULL,
   CURRENCY             VARCHAR(10)          NOT NULL,
   STATE                VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_STATE_LOAN_PRO CHECK (STATE IN ('ACT','INA','SUS') AND STATE = UPPER(STATE)),
   DESCRIPTION          VARCHAR(100)         NOT NULL,
   APPLICABILITY        VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_APPLICABILITY_LOAN_PRO CHECK (APPLICABILITY IN ('CUS','GCU','GCO') AND APPLICABILITY = UPPER(APPLICABILITY)),
   TRANCHES             INT2                 NOT NULL,
   GRACE_PERIOD         INT2                 NOT NULL,
   GRACE_PERIOD_TYPE    INT2                 NULL,
   FEE                  NUMERIC(18,2)        NOT NULL,
   REDRAW_BALANCE       NUMERIC(18,2)        NULL,
   MIN_INTEREST         NUMERIC(18,2)        NOT NULL,
   MAX_INTEREST         NUMERIC(18,2)        NULL,
   PENALTY_RATE         NUMERIC(18,2)        NULL,
   MIN_PENALTY_VALUE    NUMERIC(18,2)        NULL,
   MAX_PENALTY_VALUE    NUMERIC(18,2)        NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NULL,
   CLOSED_DATE          TIMESTAMP            NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_LOAN_PRODUCT PRIMARY KEY (LOAN_PRODUCT_ID)
);

/*==============================================================*/
/* Index: IDX_LOAPRD_CRE_DATE                                   */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_LOAPRD_CRE_DATE ON LOAN_PRODUCT (
LOAN_PRODUCT_TYPE_ID,
NAME,
UNIQUE_KEY
);

/*==============================================================*/
/* Table: LOAN_PRODUCT_TYPE                                     */
/*==============================================================*/
CREATE TABLE LOAN_PRODUCT_TYPE (
   LOAN_PRODUCT_TYPE_ID VARCHAR(36)          NOT NULL,
   NAME                 VARCHAR(100)         NOT NULL,
   CUSTOMER_TYPE        VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_CUSTOMER_TYPE_LOAN_PRO CHECK (CUSTOMER_TYPE IN ('NAT','BUS') AND CUSTOMER_TYPE = UPPER(CUSTOMER_TYPE)),
   SUPERTYPE            VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_SUPERTYPE_LOAN_PRO CHECK (SUPERTYPE IN ('FTE','DTE','IFR','TRA','REV') AND SUPERTYPE = UPPER(SUPERTYPE)),
   TEMPORARY_INTEREST   VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_TEMPORARY_INTERES_LOAN_PRO CHECK (TEMPORARY_INTEREST IN ('DAY','MON') AND TEMPORARY_INTEREST = UPPER(TEMPORARY_INTEREST)),
   ALLOW_BRANCH_TRANSACTIONS BOOL                 NULL,
   ALLOW_TRANCHES       BOOL                 NULL,
   ALLOW_REDRAW         BOOL                 NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_LOAN_PRODUCT_TYPE PRIMARY KEY (LOAN_PRODUCT_TYPE_ID)
);

/*==============================================================*/
/* Table: LOAN_TRANSACTION                                      */
/*==============================================================*/
CREATE TABLE LOAN_TRANSACTION (
   LOAN_TRANSACTION_ID  SERIAL NOT NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   TYPE                 VARCHAR(12)          NOT NULL,
   CREATION_DATE        TIMESTAMP            NOT NULL,
   BOOKING_DATE         TIMESTAMP            NOT NULL,
   VALUE_DATE           TIMESTAMP            NOT NULL,
   STATUS               VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_STATUS_LOAN_TRA CHECK (STATUS IN ('COM','PEN','CAN') AND STATUS = UPPER(STATUS)),
   AMOUNT               NUMERIC(18,2)        NOT NULL,
   APPLY_TAX            BOOL                 NULL,
   PARENT_LOAN_TRX_KEY  VARCHAR(36)          NOT NULL,
   NOTES                VARCHAR(200)         NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_LOAN_TRANSACTION PRIMARY KEY (LOAN_TRANSACTION_ID)
);

/*==============================================================*/
/* Index: IDX_TRX_CRE_DATE                                      */
/*==============================================================*/
CREATE  INDEX IDX_TRX_CRE_DATE ON LOAN_TRANSACTION (
TYPE,
CREATION_DATE,
STATUS,
AMOUNT
);

/*==============================================================*/
/* Index: IDX_TRX_TYPE                                          */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_TRX_TYPE ON LOAN_TRANSACTION (
UNIQUE_KEY,
TYPE,
CREATION_DATE,
BOOKING_DATE,
AMOUNT
);

/*==============================================================*/
/* Table: PAYMENT                                               */
/*==============================================================*/
CREATE TABLE PAYMENT (
   PAYMENT_ID           SERIAL NOT NULL,
   LOAN_ID              INT4                 NOT NULL,
   LOAN_TRANSACTION_ID  INT4                 NOT NULL,
   ACCOUNT_TRANSACTION_ID INT4                 NULL,
   TYPE                 VARCHAR(12)          NOT NULL,
   REFERENCE            VARCHAR(50)          NULL,
   STATUS               VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_STATUS_PAYMENT CHECK (STATUS IN ('COM','PEN','CAN') AND STATUS = UPPER(STATUS)),
   CREDITOR_BANK_CODE   VARCHAR(20)          NOT NULL,
   CREDITOR_ACCOUNT     VARCHAR(16)          NOT NULL,
   DEBTOR_ACCOUNT       VARCHAR(16)          NOT NULL,
   DEBTOR_BANK_CODE     VARCHAR(20)          NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_PAYMENT PRIMARY KEY (PAYMENT_ID)
);

/*==============================================================*/
/* Index: IDX_PMNT_TYPE                                         */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_PMNT_TYPE ON PAYMENT (
LOAN_ID,
LOAN_TRANSACTION_ID,
TYPE
);

/*==============================================================*/
/* Table: PRODUCT_ACCOUNT                                       */
/*==============================================================*/
CREATE TABLE PRODUCT_ACCOUNT (
   PRODUCT_ACCOUNT_ID   SERIAL NOT NULL,
   UNIQUE_KEY           VARCHAR(36)          NOT NULL,
   PRODUCT_ACCOUNT_TYPE_ID VARCHAR(36)          NOT NULL,
   NAME                 VARCHAR(64)          NOT NULL,
   TEMPORALITY_ACCOUNT_STATEMENT VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_TEMPORALITY_ACCOU_PRODUCT_ CHECK (TEMPORALITY_ACCOUNT_STATEMENT IN ('DLY','MON','BIM')),
   USE_CHECKBOOK        BOOL                 NOT NULL,
   ALLOW_OVERDRAFT      BOOL                 NOT NULL,
   ALLOW_TRANSFERENCES  BOOL                 NOT NULL,
   MAX_OVERDRAFT        NUMERIC(18,2)        NULL,
   CUSTOMER_TYPE        VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_CUSTOMER_TYPE_PRODUCT_ CHECK (CUSTOMER_TYPE IN ('NAT','BUS')),
   MIN_OPENING_BALANCE  NUMERIC(18,2)        NOT NULL DEFAULT 0,
   MIN_INTEREST         NUMERIC(5,2)         NOT NULL,
   MAX_INTEREST         NUMERIC(5,2)         NOT NULL,
   STATE                VARCHAR(3)           NOT NULL DEFAULT 'INA'
      CONSTRAINT CKC_STATE_PRODUCT_ CHECK (STATE IN ('INA','ACT','BLO','SUS') AND STATE = UPPER(STATE)),
   CREATION_DATE        TIMESTAMP            NOT NULL,
   ACTIVATION_DATE      TIMESTAMP            NULL,
   LAST_MODIFIED_DATE   TIMESTAMP            NOT NULL,
   CLOSED_DATE          TIMESTAMP            NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_PRODUCT_ACCOUNT PRIMARY KEY (PRODUCT_ACCOUNT_ID)
);

/*==============================================================*/
/* Index: IDX_PRDACCTYP_CRE_DATE                                */
/*==============================================================*/
CREATE UNIQUE INDEX IDX_PRDACCTYP_CRE_DATE ON PRODUCT_ACCOUNT (
UNIQUE_KEY,
PRODUCT_ACCOUNT_TYPE_ID,
CREATION_DATE
);

/*==============================================================*/
/* Table: PRODUCT_ACCOUNT_TYPE                                  */
/*==============================================================*/
CREATE TABLE PRODUCT_ACCOUNT_TYPE (
   PRODUCT_ACCOUNT_TYPE_ID VARCHAR(36)          NOT NULL,
   NAME                 VARCHAR(100)         NOT NULL,
   CUSTOMER_TYPE        VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_CUSTOMER_TYPE_PRODUCT_ CHECK (CUSTOMER_TYPE IN ('NAT','BUS')),
   SUPERTYPE            VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_SUPERTYPE_PRODUCT_ CHECK (SUPERTYPE IN ('DEP','CUR','SAV')),
   TEMPORALITY_INTEREST VARCHAR(3)           NOT NULL
      CONSTRAINT CKC_TEMPORALITY_INTER_PRODUCT_ CHECK (TEMPORALITY_INTEREST IN ('DAI','MON','YEA') AND TEMPORALITY_INTEREST = UPPER(TEMPORALITY_INTEREST)),
   ALLOW_EARN_INTEREST  BOOL                 NOT NULL,
   ALLOW_ACCOUNT_STATEMENT BOOL                 NOT NULL,
   ALLOW_BRANCH_TRANSACTIONS BOOL                 NOT NULL,
   ALLOW_WITHDRAWAL     BOOL                 NOT NULL,
   VERSION              INT4                 NOT NULL DEFAULT 0,
   CONSTRAINT PK_PRODUCT_ACCOUNT_TYPE PRIMARY KEY (PRODUCT_ACCOUNT_TYPE_ID)
);

ALTER TABLE ACCOUNT
   ADD CONSTRAINT FK_ACCOUNT_TO_BRANCH FOREIGN KEY (BRANCH_ID)
      REFERENCES BRANCH (BRANCH_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE ACCOUNT
   ADD CONSTRAINT "FK_ACCOUNT_TO CUSTOMER" FOREIGN KEY (CUSTOMER_ID)
      REFERENCES CUSTOMER (CUSTOMER_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE ACCOUNT
   ADD CONSTRAINT FK_ACC_TO_GRPCOMP FOREIGN KEY (GROUP_ID)
      REFERENCES GROUP_COMPANY (GROUP_COMPANY_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE ACCOUNT
   ADD CONSTRAINT FK_ACC_TO_PRDACC FOREIGN KEY (PRODUCT_ACCOUNT_ID)
      REFERENCES PRODUCT_ACCOUNT (PRODUCT_ACCOUNT_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE ACCOUNT_DOCUMENT
   ADD CONSTRAINT FK_ACCDOC_TO_ACC FOREIGN KEY (ACCOUNT_ID)
      REFERENCES ACCOUNT (ACCOUNT_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE ACCOUNT_DOCUMENT
   ADD CONSTRAINT FK_ACCDOC_TO_DOCTYPE FOREIGN KEY (DOCUMENT_TYPE_ID)
      REFERENCES DOCUMENT_TYPE (DOCUMENT_TYPE_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE ACCOUNT_INTEREST_ACCRUED
   ADD CONSTRAINT FK_ACCINTACCR_TO_ACC FOREIGN KEY (ACCOUNT_ID)
      REFERENCES ACCOUNT (ACCOUNT_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE ACCOUNT_TRANSACTION
   ADD CONSTRAINT FK_ACCTRX_TO_ACC FOREIGN KEY (ACCOUNT_ID)
      REFERENCES ACCOUNT (ACCOUNT_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE BRANCH
   ADD CONSTRAINT FK_BRANCH_TO_GEOLOC FOREIGN KEY (LOCATION_ID)
      REFERENCES GEO_LOCATION (LOCATION_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE BRANCH
   ADD CONSTRAINT FK_BRANCH_REFERENCE_BANK_ENT FOREIGN KEY (BANK_ENTITY_ID)
      REFERENCES BANK_ENTITY (BANK_ENTITY_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE CUSTOMER
   ADD CONSTRAINT FK_CUSTOMER_TO_BRANCH FOREIGN KEY (BRANCH_ID)
      REFERENCES BRANCH (BRANCH_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE CUSTOMER_ADDRESS
   ADD CONSTRAINT FK_CUSADD_TO_CUSTOMER FOREIGN KEY (CUSTOMER_ID)
      REFERENCES CUSTOMER (CUSTOMER_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE CUSTOMER_ADDRESS
   ADD CONSTRAINT FK_CUSADD_TO_GEOLOC FOREIGN KEY (LOCATION_ID)
      REFERENCES GEO_LOCATION (LOCATION_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE CUSTOMER_PHONE
   ADD CONSTRAINT FK_CUSPHONE_TO_CUSTOMER FOREIGN KEY (CUSTOMER_ID)
      REFERENCES CUSTOMER (CUSTOMER_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GEO_LOCATION
   ADD CONSTRAINT FK_GEOLOC_TO_GEOLOC FOREIGN KEY (LOCATION_ID_PARENT)
      REFERENCES GEO_LOCATION (LOCATION_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GEO_LOCATION
   ADD CONSTRAINT FK_GEOLOC_TO_GEOSTR FOREIGN KEY (COUNTRY_ID, LEVEL_CODE)
      REFERENCES GEO_STRUCTURE (COUNTRY_ID, LEVEL_CODE)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GROUP_COMPANY
   ADD CONSTRAINT FK_GRPCOMP_TO_BRANCH FOREIGN KEY (BRANCH_ID)
      REFERENCES BRANCH (BRANCH_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GROUP_COMPANY
   ADD CONSTRAINT FK_GRPCOMP_TO_GEOLOC FOREIGN KEY (LOCATION_ID)
      REFERENCES GEO_LOCATION (LOCATION_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GROUP_COMPANY_MEMBER
   ADD CONSTRAINT FK_GRPCOMPMEM_TO_CUST FOREIGN KEY (CUSTOMER_ID)
      REFERENCES CUSTOMER (CUSTOMER_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GROUP_COMPANY_MEMBER
   ADD CONSTRAINT FK_GRPCOMPMEM_TO_GRPCOMP FOREIGN KEY (GROUP_COMPANY_ID)
      REFERENCES GROUP_COMPANY (GROUP_COMPANY_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GROUP_COMPANY_MEMBER
   ADD CONSTRAINT FK_GRPCOMPMEM_TO_GRPROLE FOREIGN KEY (GROUP_ROLE_ID)
      REFERENCES GROUP_ROLE (GROUP_ROLE_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE HOLIDAY
   ADD CONSTRAINT FK_HOLIDAY_TO_GEOCTR FOREIGN KEY (COUNTRY_ID)
      REFERENCES GEO_COUNTRY (COUNTRY_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE HOLIDAY
   ADD CONSTRAINT FK_HOLIDAY_TO_GEOLOC FOREIGN KEY (LOCATION_ID)
      REFERENCES GEO_LOCATION (LOCATION_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN
   ADD CONSTRAINT FK_LOAN_TO_ASSET FOREIGN KEY (ASSET_ID)
      REFERENCES ASSET (ASSET_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN
   ADD CONSTRAINT FK_LOAN_TO_BRANCH FOREIGN KEY (BRANCH_ID)
      REFERENCES BRANCH (BRANCH_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN
   ADD CONSTRAINT FK_LOAN_TO_CUST FOREIGN KEY (CUSTOMER_ID)
      REFERENCES CUSTOMER (CUSTOMER_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN
   ADD CONSTRAINT FK_LOAN_TO_GRPCOMP FOREIGN KEY (GROUP_COMPANY_ID)
      REFERENCES GROUP_COMPANY (GROUP_COMPANY_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN
   ADD CONSTRAINT FK_LOAN_TO_GUARANTOR FOREIGN KEY (GUARANTOR_ID)
      REFERENCES GUARANTOR (GUARANTOR_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN
   ADD CONSTRAINT FK_LOAN_TO_INTACCR FOREIGN KEY (INTEREST_ACCRUE_ID)
      REFERENCES INTEREST_ACCRUE (INTEREST_ACCRUE_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN
   ADD CONSTRAINT FK_LOAN_TO_LOANPROD FOREIGN KEY (LOAN_PRODUCT_ID)
      REFERENCES LOAN_PRODUCT (LOAN_PRODUCT_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN_DOCUMENT
   ADD CONSTRAINT FK_LOANDOC_TO_LOADOCTYPE FOREIGN KEY (LOAN_DOCUMENT_TYPE_ID)
      REFERENCES LOAN_DOCUMENT_TYPE (LOAN_DOCUMENT_TYPE_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN_DOCUMENT
   ADD CONSTRAINT FK_LOANDOC_TO_LOAN FOREIGN KEY (LOAN_ID)
      REFERENCES LOAN (LOAN_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN_PRODUCT
   ADD CONSTRAINT FK_LOANPRD_AMORTIZATION FOREIGN KEY (AMORTIZATION_ID)
      REFERENCES AMORTIZATION (AMORTIZATION_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE LOAN_PRODUCT
   ADD CONSTRAINT FK_LOAPROD_TO_LOAPROTYPE FOREIGN KEY (LOAN_PRODUCT_TYPE_ID)
      REFERENCES LOAN_PRODUCT_TYPE (LOAN_PRODUCT_TYPE_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE PAYMENT
   ADD CONSTRAINT FK_PAYMENT_TO_ACCTRAN FOREIGN KEY (ACCOUNT_TRANSACTION_ID)
      REFERENCES ACCOUNT_TRANSACTION (ACCOUNT_TRANSACTION_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE PAYMENT
   ADD CONSTRAINT FK_PAYMENT_TO_LOAN FOREIGN KEY (LOAN_ID)
      REFERENCES LOAN (LOAN_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE PAYMENT
   ADD CONSTRAINT FK_PAYMENT_TO_LOANTRANSACTION FOREIGN KEY (LOAN_TRANSACTION_ID)
      REFERENCES LOAN_TRANSACTION (LOAN_TRANSACTION_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE PRODUCT_ACCOUNT
   ADD CONSTRAINT FK_PRDACC_TO_PRDACCTYPE FOREIGN KEY (PRODUCT_ACCOUNT_TYPE_ID)
      REFERENCES PRODUCT_ACCOUNT_TYPE (PRODUCT_ACCOUNT_TYPE_ID)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

