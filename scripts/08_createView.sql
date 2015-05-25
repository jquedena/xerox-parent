spool 08_createView.log

CREATE OR REPLACE VIEW FATCA.VW_PERSONA AS
SELECT DISTINCT 
      A.TIN
    , A.PAIS_BANCO
    , DECODE(B.TEXTO, NULL, '--', 'US') /* NVL(A.PAIS_RECEPTOR, 'US') */ PAIS_RECEPTOR
    , FATCA.PKG_UTIL.SF_VALIDAR_TEXTO(A.NOMBRE) NOMBRE
    , FATCA.PKG_UTIL.SF_VALIDAR_TEXTO(A.APELLIDO) APELLIDO
    , A.PAIS_RESIDENCIA
    , FATCA.PKG_UTIL.SF_VALIDAR_TEXTO(A.DIRECCION || '/' || A.CIUDAD || '/' || A.PROVINCIA /* || '/' || A.PAIS_RESIDENCIA */ || DECODE(A.CODIGO_POSTAL, NULL, '', '/' || A.CODIGO_POSTAL) ) AS DIRECCION
    , DECODE(B.TEXTO, NULL, 'J', 'N') TIPO_PERSONA
    , A.FECHA_NACIMIENTO
    , A.ESTADO
    , A.CODIGO_CLIENTE
FROM FATCA.CUENTAS A
    INNER JOIN FATCA.PERIODO C ON A.PERIODO_ID=C.ID AND C.ESTADO='1'
    LEFT JOIN FATCA.PARAMETRO B ON INSTR(B.TEXTO, '|' || CLASIFICACION || '|') > 0 AND B.ID = 248 AND B.ESTADO = 'A' -- NATURAL
WHERE ESTADO_PROCESO NOT IN('1', '3')
UNION ALL
SELECT 
      DECODE(A.ACC_PAIS_1, 'US', A.ACC_TIN_1, DECODE(A.ACC_PAIS_2, 'US', A.ACC_TIN_2, DECODE(A.ACC_PAIS_3, 'US', A.ACC_TIN_3, '---'))) TIN
    , 'US' PAIS_BANCO
    , 'US' PAIS_RECEPTOR
    , FATCA.PKG_UTIL.SF_VALIDAR_TEXTO(A.ACC_NOMBRE) NOMBRE
    , FATCA.PKG_UTIL.SF_VALIDAR_TEXTO(A.ACC_APELLIDO_PATERNO || ' ' || A.ACC_APELLIDO_MATERNO) APELLIDO
    , A.ACC_PAIS_RESIDENCIA PAIS_RESIDENCIA
    , FATCA.PKG_UTIL.SF_VALIDAR_TEXTO(A.ACC_DIRECCION || '/' || A.ACC_DISTRITO || '/' || A.ACC_DEPARTAMENTO /* || '/' || A.ACC_PAIS_RESIDENCIA */ || DECODE(A.ACC_CODIGO_POSTAL, NULL, '', '/' || A.ACC_CODIGO_POSTAL) ) AS DIRECCION
    , 'A' TIPO_PERSONA
    , '-' FECHA_NACIMIENTO
    , '-' ESTADO
    , A.EMP_CODIGO_CLIENTE CODIGO_CLIENTE 
FROM FATCA.ACCIONISTAS A
     INNER JOIN FATCA.PERIODO C ON A.PERIODO_ID=C.ID AND C.ESTADO='1'
WHERE A.ACC_PAIS_1 = 'US' OR A.ACC_PAIS_2 = 'US' OR A.ACC_PAIS_3 = 'US'
UNION ALL
SELECT
      A.CODIGO || '-' || B.CODIGO TIN
    , A.TEXTO2 PAIS_BANCO
    , '--' PAIS_RECEPTOR
    , FATCA.PKG_UTIL.SF_VALIDAR_TEXTO(B.NOMBRE) NOMBRE
    , '' APELLIDO
    , A.TEXTO2 PAIS_RESIDENCIA
    , FATCA.PKG_UTIL.SF_VALIDAR_TEXTO(A.TEXTO3) AS DIRECCION
    , 'T' TIPO_PERSONA
    , '' FECHA_NACIMIENTO
    , '1' ESTADO
    , B.CODIGO CODIGO_CLIENTE 
FROM 
   (SELECT * FROM FATCA.PARAMETRO WHERE ID = 20) A, FATCA.PARAMETRO B
WHERE B.PARAMETRO_ID = 27;

CREATE OR REPLACE VIEW FATCA.QRTZ_VW_TRIGGERS AS
SELECT X.TRIGGER_NAME
    , X.TRIGGER_GROUP
    , X.JOB_NAME
    , X.JOB_GROUP
    , X.NEXT_FIRE_TIME
    , X.TRIGGER_STATE
    , X.TRIGGER_TYPE
    , X.START_TIME
    , X.END_TIME
    , Z.EXIT_CODE
FROM FATCA.QRTZ_TRIGGERS X
LEFT JOIN(
     SELECT A.JOB_NAME, MAX(A.JOB_INSTANCE_ID) JOB_INSTANCE_ID FROM FATCA.BATCH_JOB_INSTANCE A
     GROUP BY A.JOB_NAME
) Y ON X.JOB_NAME=Y.JOB_NAME
LEFT JOIN FATCA.BATCH_JOB_EXECUTION Z ON Y.JOB_INSTANCE_ID=Z.JOB_INSTANCE_ID
ORDER BY X.TRIGGER_GROUP, X.TRIGGER_NAME, X.JOB_GROUP, X.JOB_NAME;

COMMENT ON TABLE FATCA.QRTZ_VW_TRIGGERS             IS 'VISTA DE TRABAJOS';
COMMENT ON TABLE FATCA.VW_PERSONA                   IS 'VISTA DE PERSONAS';

spool off