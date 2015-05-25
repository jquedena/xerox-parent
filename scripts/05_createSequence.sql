spool 05_createSequence.log

CREATE SEQUENCE FATCA.SQ_FATCA            START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_ACCIONISTAS      START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_ACCOUNT_REPORT   START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_CUENTAS          START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_FATCA_OECD       START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_INTERMEDIARY     START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_LOG              START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_ORGANISATION     START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_PARAMETRO        START WITH 1000 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_PAYMENT          START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_PERIODO          START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_PERSON           START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_POOL_REPORT      START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_REPORTING_FI     START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_REPORTING_GROUP  START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;
CREATE SEQUENCE FATCA.SQ_SUSTANTIAL_OWNER START WITH    1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE NOORDER NOCYCLE ;

spool off
