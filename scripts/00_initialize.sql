--==========================================================================================================--
-- CREATE TABLESPACE
--==========================================================================================================--
CREATE TABLESPACE FATCA_DAT DATAFILE 'C:/u01/datafile/fatca/fatca_dat.ora0' SIZE 256M AUTOEXTEND OFF ONLINE;
CREATE TABLESPACE FATCA_IDX DATAFILE 'C:/u01/datafile/fatca/fatca_idx.ora0' SIZE 128M AUTOEXTEND OFF ONLINE;



--==========================================================================================================--
-- CREATE USER
--==========================================================================================================--
CREATE USER FATCA IDENTIFIED BY FATCA DEFAULT TABLESPACE FATCA_DAT TEMPORARY TABLESPACE TEMP;
GRANT SELECT_CATALOG_ROLE TO FATCA;
GRANT SELECT ANY DICTIONARY TO FATCA;
GRANT SELECT ON DBA_PENDING_TRANSACTIONS TO FATCA;
GRANT EXECUTE ON DBMS_SYSTEM TO FATCA;
GRANT SELECT ON DBA_2PC_PENDING TO FATCA;
GRANT SELECT ON DBA_PENDING_TRANSACTIONS TO FATCA;
GRANT DEBUG ANY PROCEDURE TO FATCA;
GRANT DEBUG CONNECT SESSION TO FATCA;
GRANT RESOURCE TO FATCA;
GRANT CREATE VIEW TO FATCA;
GRANT CONNECT TO FATCA;

CREATE USER APP_FATCA IDENTIFIED BY APP_FATCA;
GRANT CREATE SESSION TO APP_FATCA;



--==========================================================================================================--
-- CREATE DIRECTORY
--==========================================================================================================--
CREATE OR REPLACE DIRECTORY FATCA_LOAD_DIR AS 'C:/mnt/compartido/fatca/load';
CREATE OR REPLACE DIRECTORY FATCA_OUT_DIR AS 'C:/mnt/compartido/fatca/out';

GRANT READ ON DIRECTORY FATCA_LOAD_DIR TO FATCA;
GRANT WRITE ON DIRECTORY FATCA_LOAD_DIR TO FATCA;
GRANT READ ON DIRECTORY FATCA_OUT_DIR TO FATCA;
GRANT WRITE ON DIRECTORY FATCA_OUT_DIR TO FATCA;
