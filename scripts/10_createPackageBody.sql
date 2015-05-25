spool 10_createPackageBody.log

CREATE OR REPLACE PACKAGE BODY FATCA.PKG_CONSULTAS IS

  PROCEDURE SP_CONSULTA_CLIENTE(P_REF_CURSOR OUT SYS_REFCURSOR, P_ROWS_NUM NUMBER, P_PAGESIZE NUMBER, P_PAGE NUMBER, P_PERIODO NUMBER, P_ENTIDADCONTRATO VARCHAR2, P_NROCUENTA VARCHAR2, P_NOMBRE VARCHAR2, P_ESTADO VARCHAR2, P_CODIGOCLIENTE VARCHAR2) IS
    MIN_ROW_TO_FETCH NUMBER := 0;
    MAX_ROW_TO_FETCH NUMBER := 0;
    ROWS_NUM NUMBER := 0;
  BEGIN

    MIN_ROW_TO_FETCH := (P_PAGESIZE * (P_PAGE - 1)) + 1;
    MAX_ROW_TO_FETCH := (P_PAGESIZE * P_PAGE);

    IF P_ROWS_NUM = -1 THEN
      SELECT COUNT(1) INTO ROWS_NUM
      FROM (
          SELECT MAX(A.ID) ID
          FROM FATCA.CUENTAS A
            INNER JOIN FATCA.PERIODO D ON A.PERIODO_ID=D.ID
            INNER JOIN FATCA.PARAMETRO C ON A.ESTADO=C.CODIGO AND C.PARAMETRO_ID=12
            LEFT JOIN FATCA.ACCIONISTAS B ON
                A.CODIGO_CLIENTE=B.EMP_CODIGO_CLIENTE AND
                A.TIPO_DOCUMENTO=B.EMP_TIPO_DOCUMENTO AND
                A.NRO_DOCUMENTO=B.EMP_NRO_DOCUMENTO AND
                A.PERIODO_ID=B.PERIODO_ID
          WHERE A.PERIODO_ID = P_PERIODO
              AND (A.ENTIDAD_CONTRATO = P_ENTIDADCONTRATO OR P_ENTIDADCONTRATO IS NULL)
              AND (A.NRO_CUENTA = P_NROCUENTA OR P_NROCUENTA IS NULL)
              AND (UPPER(A.NOMBRE || ' ' || A.APELLIDO) LIKE P_NOMBRE OR P_NOMBRE IS NULL)
              AND (A.ESTADO = P_ESTADO OR P_ESTADO IS NULL OR P_ESTADO = '-1')
              AND (A.CODIGO_CLIENTE = P_CODIGOCLIENTE OR P_CODIGOCLIENTE IS NULL)
          GROUP BY
                A.CODIGO_CLIENTE
              , A.TIPO_DOCUMENTO
              , A.NRO_DOCUMENTO
              , A.CLASIFICACION
              , A.NOMBRE || ' ' || A.APELLIDO
              , C.NOMBRE
              , D.ESTADO
      );
    ELSE
      ROWS_NUM := P_ROWS_NUM;
    END IF;
    
    OPEN P_REF_CURSOR FOR SELECT *
    FROM (
      SELECT /*+ FIRST_ROWS( 50 ) */ A.*, ROWNUM RNUM, ROWS_NUM "ROWS_NUM", CEIL(ROWS_NUM / P_PAGESIZE) "TOTAL_PAGE"
      FROM (
          SELECT
              MAX(A.ID) ID
            , NULL CODIGO_GIIN
            , NULL PAIS_BANCO
            , NULL PAIS_RECEPTOR
            , NULL TIPO_REPORTE
            , NULL CODIGO_REFERENCIA
            , NULL PERIODO_INFORMADO
            , NULL FECHA_COMPILACION
            , NULL ID_ENTIDAD
            , A.CODIGO_CLIENTE
            , A.TIPO_DOCUMENTO
            , A.NRO_DOCUMENTO
            , A.CLASIFICACION
            , NULL TIN
            , A.NOMBRE || ' ' || A.APELLIDO NOMBRE
            , NULL APELLIDO
            , NULL DIRECCION
            , NULL PAIS_RESIDENCIA
            , NULL CIUDAD
            , NULL PROVINCIA
            , NULL CODIGO_POSTAL
            , NULL REFERENCIA_DIRECCION
            , NULL FECHA_NACIMIENTO
            , NULL GRUPO_REPORTE
            , NULL ID_DOC_SPEC
            , NULL ID_REPORTE
            , NULL NRO_CUENTA
            , NULL TITULAR_CUENTA
            , NULL PROPIETARIOS_SUSTANCIALES
            , NULL SALDO_CUENTA
            , NULL SALDO_MONEDA
            , NULL ENTIDAD_CONTRATO
            , NULL VALOR_NOMINAL
            , NULL VALOR_NOMINAL_MONEDA
            , NULL VALOR_MERCADO
            , NULL VALOR_MERCADO_MONEDA
            , NULL TIPO_FONDO
            , NULL MONTO_501
            , NULL MONTO_502
            , NULL MONTO_503
            , NULL MONTO_504
            , NULL MONEDA_501
            , NULL MONEDA_502
            , NULL MONEDA_503
            , NULL MONEDA_504
            , NULL ORIGEN_503
            , NULL ORIGEN_502
            , NULL ORIGEN_501
            , NULL ORIGEN_504
            , NULL CODIGO_CUSTODIO
            , NULL ADMINISTRACION_MERCADO
            , NULL ESTADO
            , MIN(A.ESTADO_PROCESO) ESTADO_PROCESO
            , NULL USUARIO_CREACION
            , NULL USUARIO_MODIFICACION
            , NULL FECHA_CREACION
            , NULL FECHA_MODIFICACION
            , NULL PERIODO_ID
            , DECODE(SUM(DECODE(B.EMP_CODIGO_CLIENTE, NULL, 0, 1)), 0, 'No', 'Sí') ACCIONISTAS
            , C.NOMBRE NOMBRE_ESTADO_CUENTA
            , D.ESTADO ESTADO_PERIODO
          FROM FATCA.CUENTAS A
            INNER JOIN FATCA.PERIODO D ON A.PERIODO_ID=D.ID
            INNER JOIN FATCA.PARAMETRO C ON A.ESTADO=C.CODIGO AND C.PARAMETRO_ID=12
            LEFT JOIN FATCA.ACCIONISTAS B ON
                A.CODIGO_CLIENTE=B.EMP_CODIGO_CLIENTE AND
                A.TIPO_DOCUMENTO=B.EMP_TIPO_DOCUMENTO AND
                A.NRO_DOCUMENTO=B.EMP_NRO_DOCUMENTO AND
                A.PERIODO_ID=B.PERIODO_ID
          WHERE A.PERIODO_ID = P_PERIODO
              AND (A.ENTIDAD_CONTRATO = P_ENTIDADCONTRATO OR P_ENTIDADCONTRATO IS NULL)
              AND (A.NRO_CUENTA = P_NROCUENTA OR P_NROCUENTA IS NULL)
              AND (UPPER(A.NOMBRE || ' ' || A.APELLIDO) LIKE P_NOMBRE OR P_NOMBRE IS NULL)
              AND (A.ESTADO = P_ESTADO OR P_ESTADO IS NULL OR P_ESTADO = '-1')
              AND (A.CODIGO_CLIENTE = P_CODIGOCLIENTE OR P_CODIGOCLIENTE IS NULL)
          GROUP BY
                A.CODIGO_CLIENTE
              , A.TIPO_DOCUMENTO
              , A.NRO_DOCUMENTO
              , A.CLASIFICACION
              , A.NOMBRE || ' ' || A.APELLIDO
              , C.NOMBRE
              , D.ESTADO              
          ORDER BY A.CODIGO_CLIENTE
      ) A
      WHERE ROWNUM <= MAX_ROW_TO_FETCH
    )
    WHERE RNUM >= MIN_ROW_TO_FETCH;
  END SP_CONSULTA_CLIENTE;
END PKG_CONSULTAS;
/

CREATE OR REPLACE PACKAGE BODY FATCA.PKG_PROCESOS_ENTRADA IS

  PROCEDURE SP_COMPARAR_VARIABLES(
      P_VARIABLE_A VARCHAR2
    , P_VARIABLE_B VARCHAR2
    , P_VALOR_NUEVO OUT VARCHAR2
    , P_NOMBRE_CAMPO VARCHAR2
    , ESTADO VARCHAR2
    , MENSAJE OUT VARCHAR2) IS
    P_VARIABLE_C VARCHAR2(200) := '';
  BEGIN
    P_VALOR_NUEVO := P_VARIABLE_A;
    MENSAJE :='';

    P_VARIABLE_C := REPLACE(P_VARIABLE_B, '''', CHR(38) || 'apos;'); -- '	Apostrophe
    P_VARIABLE_C := REPLACE(P_VARIABLE_C, '- -', ''); -- - -	Double Dash	None
    P_VARIABLE_C := REPLACE(P_VARIABLE_C, '#', ''); -- #	Hash	None
    P_VARIABLE_C := REPLACE(P_VARIABLE_C, CHR(38), CHR(38) || 'amp;'); -- Ampersand
    P_VARIABLE_C := REPLACE(P_VARIABLE_C, '<', CHR(38) || 'lt;'); -- <	Less Than
    P_VARIABLE_C := REPLACE(P_VARIABLE_C, '"', CHR(38) || 'quot;'); -- "	Quotation Mark
    P_VARIABLE_C := REPLACE(P_VARIABLE_C, '>', CHR(38) || 'gt;'); -- >	Greater Than

    IF P_VARIABLE_C = '***' THEN
      P_VARIABLE_C := '';
    END IF;

    IF ESTADO = '3' OR ESTADO = '4' THEN
      P_VALOR_NUEVO :=  P_VARIABLE_C;
    ELSIF ESTADO = '1' OR ESTADO = '2' OR ESTADO = '5' OR ESTADO = '6' OR ESTADO = '7' OR ESTADO = '8' THEN
      IF NVL(P_VARIABLE_C, '---') <> NVL(P_VARIABLE_A, '---') THEN
        P_VALOR_NUEVO := P_VARIABLE_A;
        MENSAJE := P_NOMBRE_CAMPO || ' (antes: '|| P_VARIABLE_A || ', nuevo: ' || P_VARIABLE_C || '), ';
      ELSE
        P_VALOR_NUEVO :=  P_VARIABLE_C;
      END IF;
    END IF;

  END SP_COMPARAR_VARIABLES;

  PROCEDURE SP_VALIDAR_CUENTA_PROCESO(P_CUENTA T_CUENTA, MENSAJE_OUT OUT VARCHAR2) IS
    PERSONA_NATURAL NUMBER := 0;
  BEGIN
    MENSAJE_OUT :='';

    -- VALIDACION DE CAMPOS NUMERICOS
    IF P_CUENTA.NRO_CUENTA(1) IS NULL THEN
      MENSAJE_OUT := ', nro. de cuenta';
    END IF;

    IF P_CUENTA.SALDO_CUENTA(1) IS NULL THEN
      MENSAJE_OUT := MENSAJE_OUT || ', saldo de cuenta';
    END IF;

    IF P_CUENTA.SALDO_MONEDA(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', código de moneda del saldo de cuenta';
    END IF;

    IF NVL(P_CUENTA.MONTO_501(1), '0') != '0' AND P_CUENTA.MONEDA_501(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', dividendo código de moneda';
    END IF;

    IF NVL(P_CUENTA.MONTO_502(1), '0') != '0' AND P_CUENTA.MONEDA_502(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', interés código de moneda';
    END IF;

    IF NVL(P_CUENTA.MONTO_503(1), '0') != '0' AND P_CUENTA.MONEDA_503(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', amortización código de moneda';
    END IF;

    IF NVL(P_CUENTA.MONTO_504(1), '0') != '0' AND P_CUENTA.MONEDA_504(1) IS NULL THEN
       MENSAJE_OUT :=  MENSAJE_OUT || ', otros depósitos código de moneda';
    END IF;

    IF P_CUENTA.VALOR_NOMINAL_MONEDA(1) IS NULL THEN
       MENSAJE_OUT :=  MENSAJE_OUT || ', valor nominal código de moneda';
    END IF;

    IF P_CUENTA.VALOR_MERCADO_MONEDA(1) IS NULL THEN
       MENSAJE_OUT :=  MENSAJE_OUT || ', valor de mercado código de moneda';
    END IF;

    IF P_CUENTA.VALOR_NOMINAL(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', valor nominal';
    END IF;

    IF P_CUENTA.VALOR_MERCADO(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', valor del mercado';
    END IF;

    IF P_CUENTA.MONTO_501(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', dividendo valor';
    END IF;

    IF P_CUENTA.MONTO_502(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', interés valor';
    END IF;

    IF P_CUENTA.MONTO_503(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', amortización valor';
    END IF;

    IF P_CUENTA.MONTO_504(1) IS NULL THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', otros depositos valor';
    END IF;

    -- VERIFICA SEGUN EL TIPO DE PERSONA
    SELECT COUNT(1) INTO PERSONA_NATURAL FROM FATCA.PARAMETRO WHERE PARAMETRO_ID=P_ID_TABLA_TIPO_PERSONA AND TEXTO LIKE '%|' || P_CUENTA.CLASIFICACION(1) || '|%' AND ID=P_PERSONA_NATURAL;

    -- ES PERSONA NATURAL
    IF PERSONA_NATURAL = 1 THEN
      IF P_CUENTA.TIN(1) IS NULL OR ( LENGTH(P_CUENTA.TIN(1)) < 9 OR LENGTH(P_CUENTA.TIN(1)) > 11 ) THEN
        MENSAJE_OUT :=  MENSAJE_OUT || ', código TIN';
      END IF;

      -- SE VALIDARA QUE LOS CAMPOS NOMBRE, APELLIDO Y FECHA DE NACIMIENTO VENGAN INFORMADOS
      IF P_CUENTA.NOMBRE(1)               IS NULL OR LENGTH(P_CUENTA.NOMBRE(1)          )=0
          OR P_CUENTA.APELLIDO(1)         IS NULL OR LENGTH(P_CUENTA.APELLIDO(1)        )=0
          OR P_CUENTA.FECHA_NACIMIENTO(1) IS NULL OR LENGTH(P_CUENTA.FECHA_NACIMIENTO(1))=0  THEN
          MENSAJE_OUT :=  MENSAJE_OUT || ', datos personales';
      END IF;
    END IF;

    -- PARA PERSONA JURIDICA
    IF PERSONA_NATURAL = 0 THEN
      IF P_CUENTA.TIN(1) IS NULL OR NOT( LENGTH(P_CUENTA.TIN(1)) = 11 OR LENGTH(P_CUENTA.TIN(1)) = 19 ) THEN
        MENSAJE_OUT :=  MENSAJE_OUT || ', código GIIN';
      END IF;  
    
      -- SE VALIDARA QUE EL CAMPO RAZON SOCIAL VENGA INFORMADO
      IF(P_CUENTA.NOMBRE(1) IS NULL OR LENGTH(P_CUENTA.NOMBRE(1))=0)  THEN
          MENSAJE_OUT :=  MENSAJE_OUT || ', datos de la empresa';
      END IF;
    END IF;

    -- VALIDACION DE DATOS DE DOMICILIO
    IF P_CUENTA.DIRECCION(1)         IS NULL OR LENGTH(P_CUENTA.DIRECCION(1)      )=0
      OR P_CUENTA.PAIS_RESIDENCIA(1) IS NULL OR LENGTH(P_CUENTA.PAIS_RESIDENCIA(1))=0
      OR P_CUENTA.CIUDAD(1)          IS NULL OR LENGTH(P_CUENTA.CIUDAD(1)         )=0
      OR P_CUENTA.PROVINCIA(1)       IS NULL OR LENGTH(P_CUENTA.PROVINCIA(1)      )=0 THEN
      MENSAJE_OUT :=  MENSAJE_OUT || ', datos del domicilio';
    END IF;
  END SP_VALIDAR_CUENTA_PROCESO;

  PROCEDURE SP_CARGAR_CUENTAS(USUARIO VARCHAR2, ID_PROCESO NUMBER, FILAS_LEIDAS OUT NUMBER, FILAS_ESCRITAS OUT NUMBER, ESTADO OUT VARCHAR2, DESCRIPCION OUT VARCHAR2) IS
    P_CUENTA T_CUENTA;
    P_CUENTA_MERGE T_CUENTA;
    P_ID_PERIODO NUMBER := 0;
    P_IS_PERIODO NUMBER := 0;
    P_FILA_ERROR NUMBER := 0;
    P_EXISTE NUMBER := 0;
    P_CARGA_COMPLETA VARCHAR2(1) := 'N';
    V_MARCAS         VARCHAR2(500) := '';
    V_POOL_MARCAS    VARCHAR2(500) := '';
    P_CURSOR SYS_REFCURSOR;
    MSJ VARCHAR(4000):='';
    DML_ERRORS EXCEPTION;
    PRAGMA EXCEPTION_INIT (DML_ERRORS, -24381);
  BEGIN
    FILAS_LEIDAS := 0;
    FILAS_ESCRITAS := 0;
    ESTADO := 'COMPLETED';
    DESCRIPCION := 'COMPLETED';
    
    FOR P_MARCAS IN (SELECT TEXTO FROM FATCA.PARAMETRO WHERE PARAMETRO_ID=P_ID_TABLA_TIPO_PERSONA AND TEXTO IS NOT NULL AND ESTADO='A')
    LOOP
        V_MARCAS := V_MARCAS || SUBSTR(P_MARCAS.TEXTO, 1, LENGTH(P_MARCAS.TEXTO) - 1);
    END LOOP;
    V_MARCAS := V_MARCAS || '|';
    DBMS_OUTPUT.PUT_LINE('V_MARCAS: ' || V_MARCAS);
    
    -- OBTENER MARCAS DE TIPO POOL
    FOR P_POOL IN (SELECT TEXTO FROM FATCA.PARAMETRO WHERE PARAMETRO_ID=P_TIPO_POOL AND TEXTO IS NOT NULL AND ESTADO='A')
    LOOP
        V_POOL_MARCAS := V_POOL_MARCAS || SUBSTR(P_POOL.TEXTO, 1, LENGTH(P_POOL.TEXTO) - 1);
    END LOOP;
    V_POOL_MARCAS := V_POOL_MARCAS || '|';
    DBMS_OUTPUT.PUT_LINE('V_POOL_MARCAS: ' || V_POOL_MARCAS);
    
    PKG_UTIL.SP_ALTER_CHARACTER_SESSION;
    
    PKG_UTIL.SP_VERIFICAR_ARCHIVO('CUENTAS_EXT', ID_PROCESO, P_EXISTE);
    IF P_EXISTE = 0 THEN
      ESTADO := 'NOT_FOUND';
      DESCRIPCION := 'Ocurrió un error al acceder a la tabla externa, verifique que exista el archivo y/o que su estructura sea correcta';
      RETURN;
    END IF;

    SELECT NVL(MAX(BOOLEANO), 'N') INTO P_CARGA_COMPLETA FROM FATCA.PARAMETRO WHERE ID = 253 AND ESTADO='A';

    OPEN P_CURSOR FOR SELECT
      ''                                        ,
      TRIM(CODIGO_GIIN)                         ,
      TRIM(PAIS_BANCO)                          ,
      TRIM(PAIS_RECEPTOR)                       ,
      TRIM(TIPO_REPORTE)                        ,
      TRIM(CODIGO_REFERENCIA)                   ,
      TRIM(PERIODO_INFORMADO)                   ,
      TRIM(FECHA_COMPILACION)                   ,
      TRIM(ID_ENTIDAD)                          ,
      TRIM(CODIGO_CLIENTE)                      ,
      TRIM(TIPO_DOCUMENTO)                      ,
      TRIM(NRO_DOCUMENTO)                       ,
      SUBSTR(TRIM(CLASIFICACION), 1, 9)         ,
      TRIM(TIN)                                 ,
      TRIM(NOMBRE)                              ,
      TRIM(APELLIDO)                            ,
      TRIM(DIRECCION)                           ,
      TRIM(PAIS_RESIDENCIA)                     ,
      TRIM(CIUDAD)                              ,
      TRIM(PROVINCIA)                           ,
      TRIM(CODIGO_POSTAL)                       ,
      TRIM(REFERENCIA_DIRECCION)                ,
      TRIM(FECHA_NACIMIENTO)                    ,
      TRIM(GRUPO_REPORTE)                       ,
      TRIM(ID_DOC_SPEC)                         ,
      TRIM(ID_REPORTE)                          ,
      TRIM(NRO_CUENTA)                          ,
      TRIM(TITULAR_CUENTA)                      ,
      TRIM(PROPIETARIOS_SUSTANCIALES)           ,
      PKG_UTIL.SF_TO_NUMBER(TRIM(SALDO_CUENTA)) ,
      TRIM(SALDO_MONEDA)                        ,
      TRIM(ENTIDAD_CONTRATO)                    ,
      PKG_UTIL.SF_TO_NUMBER(TRIM(VALOR_NOMINAL)),
      TRIM(VALOR_NOMINAL_MONEDA)                ,
      PKG_UTIL.SF_TO_NUMBER(TRIM(VALOR_MERCADO)),
      TRIM(VALOR_MERCADO_MONEDA)                ,
      TRIM(TIPO_FONDO)                          ,
      PKG_UTIL.SF_TO_NUMBER(TRIM(MONTO_501))    ,
      PKG_UTIL.SF_TO_NUMBER(TRIM(MONTO_502))    ,
      PKG_UTIL.SF_TO_NUMBER(TRIM(MONTO_503))    ,
      PKG_UTIL.SF_TO_NUMBER(TRIM(MONTO_504))    ,
      TRIM(MONEDA_501)                          ,
      TRIM(MONEDA_502)                          ,
      TRIM(MONEDA_503)                          ,
      TRIM(MONEDA_504)                          ,
      TRIM(ORIGEN_501)                          ,
      TRIM(ORIGEN_502)                          ,
      TRIM(ORIGEN_503)                          ,
      TRIM(ORIGEN_504)                          ,
      TRIM(CODIGO_CUSTODIO)                     ,
      TRIM(ADMINISTRACION_MERCADO)              ,
      '-1' PERIODO_ID                           ,
      ROWNUM LINEA                              ,
      '1' ESTADO                                ,
      '4' ESTADO_PROCESO                        ,
      '' MENSAJE
    FROM FATCA.CUENTAS_EXT;

    LOOP
      FETCH P_CURSOR BULK COLLECT INTO P_CUENTA LIMIT 500;
      FILAS_LEIDAS := FILAS_LEIDAS + P_CUENTA.LINEA.COUNT;
      EXIT WHEN P_CUENTA.LINEA.COUNT = 0;

      BEGIN
        FOR I IN 1 .. P_CUENTA.LINEA.COUNT
        LOOP
            IF P_IS_PERIODO = 0 THEN
              -- DESACTIVAMOS LOS PERIODOS ACTIVOS
              UPDATE FATCA.PERIODO SET ESTADO = '0';
                
              SELECT NVL(MAX(ID), -1) INTO P_ID_PERIODO FROM FATCA.PERIODO WHERE PERIODO = SUBSTR(P_CUENTA.PERIODO_INFORMADO(I), 1, 4);
              IF P_ID_PERIODO = -1 THEN
                -- REGISTRAMOS EL NUEVO PERIODO CON ESTADO ACTIVO
                SELECT SQ_PERIODO.NEXTVAL INTO P_ID_PERIODO FROM SYS.DUAL;
                INSERT INTO FATCA.PERIODO(ID, PERIODO, FECHA_COMPILACION, NOMBRE_PERIODO, ESTADO, USUARIO_CREACION, FECHA_CREACION)
                VALUES (P_ID_PERIODO, SUBSTR(P_CUENTA.PERIODO_INFORMADO(I), 1, 4), P_CUENTA.FECHA_COMPILACION(I), P_CUENTA.PERIODO_INFORMADO(I), '1', USUARIO, SYSDATE);
              ELSE
                IF P_CARGA_COMPLETA = 'S' THEN
                  -- DELETE FROM FATCA.LOG WHERE PERIODO_ID = P_ID_PERIODO;
                  DELETE FROM FATCA.ACCIONISTAS WHERE PERIODO_ID = P_ID_PERIODO;
                  DELETE FROM FATCA.CUENTAS WHERE PERIODO_ID = P_ID_PERIODO;
                  COMMIT;
                END IF;

                UPDATE FATCA.PERIODO SET
                  FECHA_COMPILACION = P_CUENTA.FECHA_COMPILACION(I),
                  NOMBRE_PERIODO = P_CUENTA.PERIODO_INFORMADO(I),
                  USUARIO_MODIFICACION = USUARIO,
                  FECHA_MODIFICACION = SYSDATE,
                  ESTADO = '1'
                WHERE ID = P_ID_PERIODO;
              END IF;
              P_IS_PERIODO := 1;
            END IF;

            BEGIN
              P_CUENTA.PERIODO_ID(I) := P_ID_PERIODO;
              
              /* VERIFICA SI LA CUENTA TIENE UNA CLASIFICACION VALIDA */
              IF INSTR(V_MARCAS, '|' || P_CUENTA.CLASIFICACION(I) || '|') = 0 THEN
                MSJ := '[Nro. Cuenta: ' || P_CUENTA.NRO_CUENTA(I) || ', Código de Cliente: ' || P_CUENTA.CODIGO_CLIENTE(I) || '] - No cuenta con una clasificación válida (' || P_CUENTA.ENTIDAD_CONTRATO(I) || ':' || P_CUENTA.CLASIFICACION(I) || ')';
                PKG_UTIL.SP_REGISTRAR_INFO(MSJ, P_CUENTA.ID_CUENTA(I), NULL, ID_PROCESO, P_ID_PERIODO, USUARIO);
                CONTINUE;
              END IF;
              
              SP_VALIDAR_CUENTA(P_CUENTA, I, V_POOL_MARCAS, P_CUENTA_MERGE);
              
              IF P_CUENTA_MERGE.ID_CUENTA(1) = '-1' THEN
                P_CUENTA_MERGE.ID_CUENTA(1) := SF_OBTENER_NEXTVAL_CUENTA;
              END IF;


              MERGE INTO FATCA.CUENTAS A USING
                ( SELECT
                    P_CUENTA_MERGE.ID_CUENTA(1)                            ID_CUENTA                 ,
                    P_CUENTA_MERGE.CODIGO_GIIN(1)                          CODIGO_GIIN               ,
                    P_CUENTA_MERGE.PAIS_BANCO(1)                           PAIS_BANCO                ,
                    P_CUENTA_MERGE.PAIS_RECEPTOR(1)                        PAIS_RECEPTOR             ,
                    P_CUENTA_MERGE.TIPO_REPORTE(1)                         TIPO_REPORTE              ,
                    P_CUENTA_MERGE.CODIGO_REFERENCIA(1)                    CODIGO_REFERENCIA         ,
                    P_CUENTA_MERGE.PERIODO_INFORMADO(1)                    PERIODO_INFORMADO         ,
                    P_CUENTA_MERGE.FECHA_COMPILACION(1)                    FECHA_COMPILACION         ,
                    P_CUENTA_MERGE.ID_ENTIDAD(1)                           ID_ENTIDAD                ,
                    P_CUENTA_MERGE.CODIGO_CLIENTE(1)                       CODIGO_CLIENTE            ,
                    P_CUENTA_MERGE.TIPO_DOCUMENTO(1)                       TIPO_DOCUMENTO            ,
                    P_CUENTA_MERGE.NRO_DOCUMENTO(1)                        NRO_DOCUMENTO             ,
                    P_CUENTA_MERGE.CLASIFICACION(1)                        CLASIFICACION             ,
                    P_CUENTA_MERGE.TIN(1)                                  TIN                       ,
                    P_CUENTA_MERGE.NOMBRE(1)                               NOMBRE                    ,
                    P_CUENTA_MERGE.APELLIDO(1)                             APELLIDO                  ,
                    P_CUENTA_MERGE.DIRECCION(1)                            DIRECCION                 ,
                    P_CUENTA_MERGE.PAIS_RESIDENCIA(1)                      PAIS_RESIDENCIA           ,
                    P_CUENTA_MERGE.CIUDAD(1)                               CIUDAD                    ,
                    P_CUENTA_MERGE.PROVINCIA(1)                            PROVINCIA                 ,
                    P_CUENTA_MERGE.CODIGO_POSTAL(1)                        CODIGO_POSTAL             ,
                    P_CUENTA_MERGE.REFERENCIA_DIRECCION(1)                 REFERENCIA_DIRECCION      ,
                    P_CUENTA_MERGE.FECHA_NACIMIENTO(1)                     FECHA_NACIMIENTO          ,
                    P_CUENTA_MERGE.GRUPO_REPORTE(1)                        GRUPO_REPORTE             ,
                    P_CUENTA_MERGE.ID_DOC_SPEC(1)                          ID_DOC_SPEC               ,
                    P_CUENTA_MERGE.ID_REPORTE(1)                           ID_REPORTE                ,
                    P_CUENTA_MERGE.NRO_CUENTA(1)                           NRO_CUENTA                ,
                    P_CUENTA_MERGE.TITULAR_CUENTA(1)                       TITULAR_CUENTA            ,
                    P_CUENTA_MERGE.PROPIETARIOS_SUSTANCIALES(1)            PROPIETARIOS_SUSTANCIALES ,
                    P_CUENTA_MERGE.SALDO_CUENTA(1)                         SALDO_CUENTA              ,
                    P_CUENTA_MERGE.SALDO_MONEDA(1)                         SALDO_MONEDA              ,
                    P_CUENTA_MERGE.ENTIDAD_CONTRATO(1)                     ENTIDAD_CONTRATO          ,
                    P_CUENTA_MERGE.VALOR_NOMINAL(1)                        VALOR_NOMINAL             ,
                    P_CUENTA_MERGE.VALOR_NOMINAL_MONEDA(1)                 VALOR_NOMINAL_MONEDA      ,
                    P_CUENTA_MERGE.VALOR_MERCADO(1)                        VALOR_MERCADO             ,
                    P_CUENTA_MERGE.VALOR_MERCADO_MONEDA(1)                 VALOR_MERCADO_MONEDA      ,
                    P_CUENTA_MERGE.TIPO_FONDO(1)                           TIPO_FONDO                ,
                    P_CUENTA_MERGE.MONTO_501(1)                            MONTO_501                 ,
                    P_CUENTA_MERGE.MONTO_502(1)                            MONTO_502                 ,
                    P_CUENTA_MERGE.MONTO_503(1)                            MONTO_503                 ,
                    P_CUENTA_MERGE.MONTO_504(1)                            MONTO_504                 ,
                    P_CUENTA_MERGE.MONEDA_501(1)                           MONEDA_501                ,
                    P_CUENTA_MERGE.MONEDA_502(1)                           MONEDA_502                ,
                    P_CUENTA_MERGE.MONEDA_503(1)                           MONEDA_503                ,
                    P_CUENTA_MERGE.MONEDA_504(1)                           MONEDA_504                ,
                    P_CUENTA_MERGE.ORIGEN_501(1)                           ORIGEN_501                ,
                    P_CUENTA_MERGE.ORIGEN_502(1)                           ORIGEN_502                ,
                    P_CUENTA_MERGE.ORIGEN_503(1)                           ORIGEN_503                ,
                    P_CUENTA_MERGE.ORIGEN_504(1)                           ORIGEN_504                ,
                    P_CUENTA_MERGE.CODIGO_CUSTODIO(1)                      CODIGO_CUSTODIO           ,
                    P_CUENTA_MERGE.ADMINISTRACION_MERCADO(1)               ADMINISTRACION_MERCADO    ,
                    P_ID_PERIODO                                           PERIODO_ID                ,
                    P_CUENTA_MERGE.LINEA(1)                                LINEA                     ,
                    P_CUENTA_MERGE.ESTADO(1)                               ESTADO                    ,
                    P_CUENTA_MERGE.ESTADO_PROCESO(1)                       ESTADO_PROCESO
                  FROM SYS.DUAL ) B
              ON (A.NRO_CUENTA = B.NRO_CUENTA AND A.CODIGO_CLIENTE = B.CODIGO_CLIENTE AND A.PERIODO_ID = B.PERIODO_ID)
              WHEN MATCHED THEN
                UPDATE SET
                          A.CODIGO_GIIN               = B.CODIGO_GIIN               ,
                          A.PAIS_BANCO                = B.PAIS_BANCO                ,
                          A.PAIS_RECEPTOR             = B.PAIS_RECEPTOR             ,
                          A.TIPO_REPORTE              = B.TIPO_REPORTE              ,
                          A.CODIGO_REFERENCIA         = B.CODIGO_REFERENCIA         ,
                          A.PERIODO_INFORMADO         = B.PERIODO_INFORMADO         ,
                          A.FECHA_COMPILACION         = B.FECHA_COMPILACION         ,
                          A.ID_ENTIDAD                = B.ID_ENTIDAD                ,
                          A.TIPO_DOCUMENTO            = B.TIPO_DOCUMENTO            ,
                          A.NRO_DOCUMENTO             = B.NRO_DOCUMENTO             ,
                          A.CLASIFICACION             = B.CLASIFICACION             ,
                          A.TIN                       = B.TIN                       ,
                          A.NOMBRE                    = B.NOMBRE                    ,
                          A.APELLIDO                  = B.APELLIDO                  ,
                          A.DIRECCION                 = B.DIRECCION                 ,
                          A.PAIS_RESIDENCIA           = B.PAIS_RESIDENCIA           ,
                          A.CIUDAD                    = B.CIUDAD                    ,
                          A.PROVINCIA                 = B.PROVINCIA                 ,
                          A.CODIGO_POSTAL             = B.CODIGO_POSTAL             ,
                          A.REFERENCIA_DIRECCION      = B.REFERENCIA_DIRECCION      ,
                          A.FECHA_NACIMIENTO          = B.FECHA_NACIMIENTO          ,
                          A.GRUPO_REPORTE             = B.GRUPO_REPORTE             ,
                          A.ID_DOC_SPEC               = B.ID_DOC_SPEC               ,
                          A.ID_REPORTE                = B.ID_REPORTE                ,
                          A.TITULAR_CUENTA            = B.TITULAR_CUENTA            ,
                          A.PROPIETARIOS_SUSTANCIALES = B.PROPIETARIOS_SUSTANCIALES ,
                          A.SALDO_CUENTA              = B.SALDO_CUENTA              ,
                          A.SALDO_MONEDA              = B.SALDO_MONEDA              ,
                          A.ENTIDAD_CONTRATO          = B.ENTIDAD_CONTRATO          ,
                          A.VALOR_NOMINAL             = B.VALOR_NOMINAL             ,
                          A.VALOR_NOMINAL_MONEDA      = B.VALOR_NOMINAL_MONEDA      ,
                          A.VALOR_MERCADO             = B.VALOR_MERCADO             ,
                          A.VALOR_MERCADO_MONEDA      = B.VALOR_MERCADO_MONEDA      ,
                          A.TIPO_FONDO                = B.TIPO_FONDO                ,
                          A.MONTO_501                 = B.MONTO_501                 ,
                          A.MONTO_502                 = B.MONTO_502                 ,
                          A.MONTO_503                 = B.MONTO_503                 ,
                          A.MONTO_504                 = B.MONTO_504                 ,
                          A.MONEDA_501                = B.MONEDA_501                ,
                          A.MONEDA_502                = B.MONEDA_502                ,
                          A.MONEDA_503                = B.MONEDA_503                ,
                          A.MONEDA_504                = B.MONEDA_504                ,
                          A.ORIGEN_501                = B.ORIGEN_501                ,
                          A.ORIGEN_502                = B.ORIGEN_502                ,
                          A.ORIGEN_503                = B.ORIGEN_503                ,
                          A.ORIGEN_504                = B.ORIGEN_504                ,
                          A.CODIGO_CUSTODIO           = B.CODIGO_CUSTODIO           ,
                          A.ADMINISTRACION_MERCADO    = B.ADMINISTRACION_MERCADO    ,
                          A.ESTADO                    = B.ESTADO                    ,
                          A.ESTADO_PROCESO            = B.ESTADO_PROCESO            ,
                          A.USUARIO_MODIFICACION      = USUARIO                     ,
                          A.FECHA_MODIFICACION        = SYSDATE
              WHEN NOT MATCHED THEN
                INSERT (
                      A.ID                        , A.CODIGO_GIIN               , A.PAIS_BANCO                ,
                      A.PAIS_RECEPTOR             , A.TIPO_REPORTE              , A.CODIGO_REFERENCIA         ,
                      A.PERIODO_INFORMADO         , A.FECHA_COMPILACION         , A.ID_ENTIDAD                ,
                      A.CODIGO_CLIENTE            , A.TIPO_DOCUMENTO            , A.NRO_DOCUMENTO             ,
                      A.CLASIFICACION             , A.TIN                       , A.NOMBRE                    ,
                      A.APELLIDO                  , A.DIRECCION                 , A.PAIS_RESIDENCIA           ,
                      A.CIUDAD                    , A.PROVINCIA                 , A.CODIGO_POSTAL             ,
                      A.REFERENCIA_DIRECCION      , A.FECHA_NACIMIENTO          , A.GRUPO_REPORTE             ,
                      A.ID_DOC_SPEC               , A.ID_REPORTE                , A.NRO_CUENTA                ,
                      A.TITULAR_CUENTA            , A.PROPIETARIOS_SUSTANCIALES , A.SALDO_CUENTA              ,
                      A.SALDO_MONEDA              , A.ENTIDAD_CONTRATO          , A.VALOR_NOMINAL             ,
                      A.VALOR_NOMINAL_MONEDA      , A.VALOR_MERCADO             , A.VALOR_MERCADO_MONEDA      ,
                      A.TIPO_FONDO                , A.MONTO_501                 , A.MONTO_502                 ,
                      A.MONTO_503                 , A.MONTO_504                 , A.MONEDA_501                ,
                      A.MONEDA_502                , A.MONEDA_503                , A.MONEDA_504                ,
                      A.ORIGEN_501                , A.ORIGEN_502                , A.ORIGEN_503                ,
                      A.ORIGEN_504                , A.CODIGO_CUSTODIO           , A.ADMINISTRACION_MERCADO    ,
                      A.PERIODO_ID                , A.ESTADO                    , A.ESTADO_PROCESO            ,
                      A.USUARIO_CREACION          , A.FECHA_CREACION
                ) VALUES (
                      B.ID_CUENTA                 , B.CODIGO_GIIN               , B.PAIS_BANCO                ,
                      B.PAIS_RECEPTOR             , B.TIPO_REPORTE              , B.CODIGO_REFERENCIA         ,
                      B.PERIODO_INFORMADO         , B.FECHA_COMPILACION         , B.ID_ENTIDAD                ,
                      B.CODIGO_CLIENTE            , B.TIPO_DOCUMENTO            , B.NRO_DOCUMENTO             ,
                      B.CLASIFICACION             , B.TIN                       , B.NOMBRE                    ,
                      B.APELLIDO                  , B.DIRECCION                 , B.PAIS_RESIDENCIA           ,
                      B.CIUDAD                    , B.PROVINCIA                 , B.CODIGO_POSTAL             ,
                      B.REFERENCIA_DIRECCION      , B.FECHA_NACIMIENTO          , B.GRUPO_REPORTE             ,
                      B.ID_DOC_SPEC               , B.ID_REPORTE                , B.NRO_CUENTA                ,
                      B.TITULAR_CUENTA            , B.PROPIETARIOS_SUSTANCIALES , B.SALDO_CUENTA              ,
                      B.SALDO_MONEDA              , B.ENTIDAD_CONTRATO          , B.VALOR_NOMINAL             ,
                      B.VALOR_NOMINAL_MONEDA      , B.VALOR_MERCADO             , B.VALOR_MERCADO_MONEDA      ,
                      B.TIPO_FONDO                , B.MONTO_501                 , B.MONTO_502                 ,
                      B.MONTO_503                 , B.MONTO_504                 , B.MONEDA_501                ,
                      B.MONEDA_502                , B.MONEDA_503                , B.MONEDA_504                ,
                      B.ORIGEN_501                , B.ORIGEN_502                , B.ORIGEN_503                ,
                      B.ORIGEN_504                , B.CODIGO_CUSTODIO           , B.ADMINISTRACION_MERCADO    ,
                      B.PERIODO_ID                , B.ESTADO                    , B.ESTADO_PROCESO            ,
                      USUARIO                     , SYSDATE
                );

                IF(P_CUENTA_MERGE.ESTADO_PROCESO(1) != 4 AND LENGTH(P_CUENTA_MERGE.MENSAJE(1))>0) THEN
                  MSJ := '[Nro. Cuenta: ' || P_CUENTA_MERGE.NRO_CUENTA(1) || '(' || P_CUENTA_MERGE.ENTIDAD_CONTRATO(1) || ':' || P_CUENTA_MERGE.CLASIFICACION(1) || ') , Código de Cliente: ' || P_CUENTA_MERGE.CODIGO_CLIENTE(1) || '] - ' || P_CUENTA_MERGE.MENSAJE(1);
                  PKG_UTIL.SP_REGISTRAR_INFO(MSJ, P_CUENTA_MERGE.ID_CUENTA(1), NULL, ID_PROCESO, P_ID_PERIODO, USUARIO);
                END IF;
                FILAS_ESCRITAS := FILAS_ESCRITAS + 1;
            EXCEPTION
              WHEN OTHERS THEN
                P_FILA_ERROR := P_FILA_ERROR + 1;
                PKG_UTIL.SP_REGISTRAR_INFO('[Nro. Cuenta: ' || P_CUENTA.NRO_CUENTA(I) || '(' || P_CUENTA.ENTIDAD_CONTRATO(I) || ':' || P_CUENTA.CLASIFICACION(I) || ') , Código de Cliente: ' || P_CUENTA.CODIGO_CLIENTE(I) || '] - ' || SQLERRM || ' ' || DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, NULL, NULL, ID_PROCESO, P_ID_PERIODO, USUARIO);
            END;
        END LOOP;
      END;
      COMMIT;
    END LOOP;
  EXCEPTION
    WHEN OTHERS THEN
      PKG_UTIL.SP_REGISTRAR_INFO(SQLERRM || ' ' || DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, NULL, NULL, ID_PROCESO, NULL, USUARIO);
      COMMIT;

      ESTADO := 'ERROR';
      DESCRIPCION := 'Ocurrió un error durante la ejecución del proceso: ' || SQLERRM || ' ' || DBMS_UTILITY.FORMAT_ERROR_BACKTRACE;
  END SP_CARGAR_CUENTAS;

  PROCEDURE SP_VALIDAR_CUENTA(P_CUENTA_EXT T_CUENTA, K NUMBER, P_POOL_MARCAS VARCHAR2, P_CUENTA OUT T_CUENTA) IS
    P_CURSOR SYS_REFCURSOR;
    MENSAJE_OUT VARCHAR2(500) := '';
    MENSAJE_CONFLICTO VARCHAR2(250) := '';
    MENSAJE_CONFLICTO_TOTAL VARCHAR2(4000) := '';
    VALOR_NUEVO VARCHAR(500) := '';
  BEGIN
    OPEN P_CURSOR FOR SELECT
      NVL(MAX(ID                           ), '-1'                                         ),
      NVL(MAX(CODIGO_GIIN                  ), P_CUENTA_EXT.CODIGO_GIIN(K)                  ),
      NVL(MAX(PAIS_BANCO                   ), P_CUENTA_EXT.PAIS_BANCO(K)                   ),
      NVL(MAX(PAIS_RECEPTOR                ), P_CUENTA_EXT.PAIS_RECEPTOR(K)                ),
      NVL(MAX(TIPO_REPORTE                 ), P_CUENTA_EXT.TIPO_REPORTE(K)                 ),
      NVL(MAX(CODIGO_REFERENCIA            ), P_CUENTA_EXT.CODIGO_REFERENCIA(K)            ),
      NVL(MAX(PERIODO_INFORMADO            ), P_CUENTA_EXT.PERIODO_INFORMADO(K)            ),
      NVL(MAX(FECHA_COMPILACION            ), P_CUENTA_EXT.FECHA_COMPILACION(K)            ),
      NVL(MAX(ID_ENTIDAD                   ), P_CUENTA_EXT.ID_ENTIDAD(K)                   ),
      NVL(MAX(CODIGO_CLIENTE               ), P_CUENTA_EXT.CODIGO_CLIENTE(K)               ),
      NVL(MAX(TIPO_DOCUMENTO               ), P_CUENTA_EXT.TIPO_DOCUMENTO(K)               ),
      NVL(MAX(NRO_DOCUMENTO                ), P_CUENTA_EXT.NRO_DOCUMENTO(K)                ),
      NVL(MAX(CLASIFICACION                ), P_CUENTA_EXT.CLASIFICACION(K)                ),
      NVL(MAX(TIN                          ), P_CUENTA_EXT.TIN(K)                          ),
      NVL(MAX(NOMBRE                       ), P_CUENTA_EXT.NOMBRE(K)                       ),
      NVL(MAX(APELLIDO                     ), P_CUENTA_EXT.APELLIDO(K)                     ),
      NVL(MAX(DIRECCION                    ), P_CUENTA_EXT.DIRECCION(K)                    ),
      NVL(MAX(PAIS_RESIDENCIA              ), P_CUENTA_EXT.PAIS_RESIDENCIA(K)              ),
      NVL(MAX(CIUDAD                       ), P_CUENTA_EXT.CIUDAD(K)                       ),
      NVL(MAX(PROVINCIA                    ), P_CUENTA_EXT.PROVINCIA(K)                    ),
      NVL(MAX(CODIGO_POSTAL                ), P_CUENTA_EXT.CODIGO_POSTAL(K)                ),
      NVL(MAX(REFERENCIA_DIRECCION         ), P_CUENTA_EXT.REFERENCIA_DIRECCION(K)         ),
      NVL(MAX(FECHA_NACIMIENTO             ), P_CUENTA_EXT.FECHA_NACIMIENTO(K)             ),
      NVL(MAX(GRUPO_REPORTE                ), P_CUENTA_EXT.GRUPO_REPORTE(K)                ),
      NVL(MAX(ID_DOC_SPEC                  ), P_CUENTA_EXT.ID_DOC_SPEC(K)                  ),
      NVL(MAX(ID_REPORTE                   ), P_CUENTA_EXT.ID_REPORTE(K)                   ),
      NVL(MAX(NRO_CUENTA                   ), P_CUENTA_EXT.NRO_CUENTA(K)                   ),
      NVL(MAX(TITULAR_CUENTA               ), P_CUENTA_EXT.TITULAR_CUENTA(K)               ),
      NVL(MAX(PROPIETARIOS_SUSTANCIALES    ), P_CUENTA_EXT.PROPIETARIOS_SUSTANCIALES(K)    ),
      NVL(MAX(SALDO_CUENTA                 ), P_CUENTA_EXT.SALDO_CUENTA(K)                 ),
      NVL(MAX(SALDO_MONEDA                 ), P_CUENTA_EXT.SALDO_MONEDA(K)                 ),
      NVL(MAX(ENTIDAD_CONTRATO             ), P_CUENTA_EXT.ENTIDAD_CONTRATO(K)             ),
      NVL(MAX(VALOR_NOMINAL                ), P_CUENTA_EXT.VALOR_NOMINAL(K)                ),
      NVL(MAX(VALOR_NOMINAL_MONEDA         ), P_CUENTA_EXT.VALOR_NOMINAL_MONEDA(K)         ),
      NVL(MAX(VALOR_MERCADO                ), P_CUENTA_EXT.VALOR_MERCADO(K)                ),
      NVL(MAX(VALOR_MERCADO_MONEDA         ), P_CUENTA_EXT.VALOR_MERCADO_MONEDA(K)         ),
      NVL(MAX(TIPO_FONDO                   ), P_CUENTA_EXT.TIPO_FONDO(K)                   ),
      NVL(MAX(MONTO_501                    ), P_CUENTA_EXT.MONTO_501(K)                    ),
      NVL(MAX(MONTO_502                    ), P_CUENTA_EXT.MONTO_502(K)                    ),
      NVL(MAX(MONTO_503                    ), P_CUENTA_EXT.MONTO_503(K)                    ),
      NVL(MAX(MONTO_504                    ), P_CUENTA_EXT.MONTO_504(K)                    ),
      NVL(MAX(MONEDA_501                   ), P_CUENTA_EXT.MONEDA_501(K)                   ),
      NVL(MAX(MONEDA_502                   ), P_CUENTA_EXT.MONEDA_502(K)                   ),
      NVL(MAX(MONEDA_503                   ), P_CUENTA_EXT.MONEDA_503(K)                   ),
      NVL(MAX(MONEDA_504                   ), P_CUENTA_EXT.MONEDA_504(K)                   ),
      NVL(MAX(ORIGEN_501                   ), P_CUENTA_EXT.ORIGEN_501(K)                   ),
      NVL(MAX(ORIGEN_502                   ), P_CUENTA_EXT.ORIGEN_502(K)                   ),
      NVL(MAX(ORIGEN_503                   ), P_CUENTA_EXT.ORIGEN_503(K)                   ),
      NVL(MAX(ORIGEN_504                   ), P_CUENTA_EXT.ORIGEN_504(K)                   ),
      NVL(MAX(CODIGO_CUSTODIO              ), P_CUENTA_EXT.CODIGO_CUSTODIO(K)              ),
      NVL(MAX(ADMINISTRACION_MERCADO       ), P_CUENTA_EXT.ADMINISTRACION_MERCADO(K)       ),
      NVL(MAX(PERIODO_ID                   ), P_CUENTA_EXT.PERIODO_ID(K)                   ),
      '0' LINEA                             ,
      NVL(MAX(ESTADO                       ), P_CUENTA_EXT.ESTADO(K)                       ),
      NVL(MAX(ESTADO_PROCESO               ), P_CUENTA_EXT.ESTADO_PROCESO(K)               ),
      '' MENSAJE
    FROM FATCA.CUENTAS
    WHERE NRO_CUENTA = P_CUENTA_EXT.NRO_CUENTA(K)
      AND CODIGO_CLIENTE = P_CUENTA_EXT.CODIGO_CLIENTE(K)
      AND PERIODO_ID = P_CUENTA_EXT.PERIODO_ID(K);

    FETCH P_CURSOR BULK COLLECT INTO P_CUENTA LIMIT 1;

    /*
     * 1 Informado
     * 2 Modificado
     * 3 Con Error
     * 4 OK
     * 5 Informado con Conflicto
     * 6 Modificado con Conflicto
     * 7 Informado modificado
     * 8 Informado modificado con Conflicto
     **/

    SP_COMPARAR_VARIABLES(P_CUENTA.CODIGO_GIIN(1) , P_CUENTA_EXT.CODIGO_GIIN(K), VALOR_NUEVO, 'código GIIN', P_CUENTA_EXT.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.CODIGO_GIIN(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.PAIS_BANCO(1) , P_CUENTA_EXT.PAIS_BANCO(K), VALOR_NUEVO, 'país banco', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.PAIS_BANCO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.PAIS_RECEPTOR(1) , SF_OBTENER_PAIS_X_CODIGO_HOST(P_CUENTA_EXT.PAIS_RECEPTOR(K)), VALOR_NUEVO, 'país receptor', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.PAIS_RECEPTOR(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.TIPO_REPORTE(1) , P_CUENTA_EXT.TIPO_REPORTE(K), VALOR_NUEVO, 'tipo de reporte', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.TIPO_REPORTE(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.CODIGO_REFERENCIA(1) , P_CUENTA_EXT.CODIGO_REFERENCIA(K), VALOR_NUEVO, 'código de referencia', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.CODIGO_REFERENCIA(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.PERIODO_INFORMADO(1) , P_CUENTA_EXT.PERIODO_INFORMADO(K), VALOR_NUEVO, 'periodo informado', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.PERIODO_INFORMADO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    --LA FECHA SE SOBREESCRIBE SIN PROBLEMAS
    P_CUENTA.FECHA_COMPILACION(1) := P_CUENTA_EXT.FECHA_COMPILACION(K);

    SP_COMPARAR_VARIABLES(P_CUENTA.ID_ENTIDAD(1) , P_CUENTA_EXT.ID_ENTIDAD(K), VALOR_NUEVO, 'idEntidad', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.ID_ENTIDAD(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.TIPO_DOCUMENTO(1) , P_CUENTA_EXT.TIPO_DOCUMENTO(K), VALOR_NUEVO, 'tipo de documento', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.TIPO_DOCUMENTO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.NRO_DOCUMENTO(1) , P_CUENTA_EXT.NRO_DOCUMENTO(K), VALOR_NUEVO, 'nro. documento', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.NRO_DOCUMENTO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.CLASIFICACION(1) , P_CUENTA_EXT.CLASIFICACION(K), VALOR_NUEVO, 'clasificación', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.CLASIFICACION(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.TIN(1) , P_CUENTA_EXT.TIN(K), VALOR_NUEVO, 'TIN', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.TIN(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.NOMBRE(1) , P_CUENTA_EXT.NOMBRE(K), VALOR_NUEVO, 'nombre', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.NOMBRE(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.APELLIDO(1) , P_CUENTA_EXT.APELLIDO(K), VALOR_NUEVO, 'apellidos', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.APELLIDO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.DIRECCION(1) , P_CUENTA_EXT.DIRECCION(K), VALOR_NUEVO, 'dirección', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.DIRECCION(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.PAIS_RESIDENCIA(1) , SF_OBTENER_PAIS_X_CODIGO_HOST(P_CUENTA_EXT.PAIS_RESIDENCIA(K)), VALOR_NUEVO, 'país de residencia', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.PAIS_RESIDENCIA(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.CIUDAD(1) , P_CUENTA_EXT.CIUDAD(K), VALOR_NUEVO, 'ciudad', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.CIUDAD(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.PROVINCIA(1) , P_CUENTA_EXT.PROVINCIA(K), VALOR_NUEVO, 'provincia', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.PROVINCIA(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.CODIGO_POSTAL(1) , P_CUENTA_EXT.CODIGO_POSTAL(K), VALOR_NUEVO, 'código postal', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.CODIGO_POSTAL(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.REFERENCIA_DIRECCION(1) , P_CUENTA_EXT.REFERENCIA_DIRECCION(K), VALOR_NUEVO, 'referencia dirección', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.REFERENCIA_DIRECCION(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.FECHA_NACIMIENTO(1) , P_CUENTA_EXT.FECHA_NACIMIENTO(K), VALOR_NUEVO, 'fecha de nacimiento', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.FECHA_NACIMIENTO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.GRUPO_REPORTE(1) , P_CUENTA_EXT.GRUPO_REPORTE(K), VALOR_NUEVO, 'grupo', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.GRUPO_REPORTE(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    /*
    SP_COMPARAR_VARIABLES(P_CUENTA.ID_DOC_SPEC(1) , P_CUENTA_EXT.ID_DOC_SPEC(K), VALOR_NUEVO, 'ID_DOC_SPEC', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.ID_DOC_SPEC(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.ID_REPORTE(1) , P_CUENTA_EXT.ID_REPORTE(K), VALOR_NUEVO, 'ID_REPORTE', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.ID_REPORTE(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;
    */

    SP_COMPARAR_VARIABLES(P_CUENTA.TITULAR_CUENTA(1) , P_CUENTA_EXT.TITULAR_CUENTA(K), VALOR_NUEVO, 'titular de la cuenta', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.TITULAR_CUENTA(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    /*
    SP_COMPARAR_VARIABLES(P_CUENTA.PROPIETARIOS_SUSTANCIALES(1) , P_CUENTA_EXT.PROPIETARIOS_SUSTANCIALES(K), VALOR_NUEVO, 'PROPIETARIOS_SUSTANCIALES', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.PROPIETARIOS_SUSTANCIALES(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;
    */
    
    SP_COMPARAR_VARIABLES(P_CUENTA.SALDO_CUENTA(1) , P_CUENTA_EXT.SALDO_CUENTA(K), VALOR_NUEVO, 'saldo de cuenta', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.SALDO_CUENTA(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.SALDO_MONEDA(1) , SF_OBTENER_MONE_X_CODIGO_HOST(P_CUENTA_EXT.SALDO_MONEDA(K)), VALOR_NUEVO, 'código de moneda del saldo de cuenta', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.SALDO_MONEDA(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.ENTIDAD_CONTRATO(1) , P_CUENTA_EXT.ENTIDAD_CONTRATO(K), VALOR_NUEVO, 'entidad', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.ENTIDAD_CONTRATO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.VALOR_NOMINAL(1) , P_CUENTA_EXT.VALOR_NOMINAL(K), VALOR_NUEVO, 'valor nominal', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.VALOR_NOMINAL(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.VALOR_NOMINAL_MONEDA(1) ,SF_OBTENER_MONE_X_CODIGO_HOST(P_CUENTA_EXT.VALOR_NOMINAL_MONEDA(K)), VALOR_NUEVO, 'código de moneda del valor nominal', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.VALOR_NOMINAL_MONEDA(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.VALOR_MERCADO(1) ,P_CUENTA_EXT.VALOR_MERCADO(K), VALOR_NUEVO, 'valor de mercado', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.VALOR_MERCADO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.VALOR_MERCADO_MONEDA(1) ,SF_OBTENER_MONE_X_CODIGO_HOST(P_CUENTA_EXT.VALOR_MERCADO_MONEDA(K)), VALOR_NUEVO, 'código de moneda del valor de mercado', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.VALOR_MERCADO_MONEDA(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.TIPO_FONDO(1) ,P_CUENTA_EXT.TIPO_FONDO(K), VALOR_NUEVO, 'tipo de fondo', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.TIPO_FONDO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.MONTO_501(1) ,P_CUENTA_EXT.MONTO_501(K), VALOR_NUEVO, 'dividendo valor', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.MONTO_501(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.MONTO_502(1) ,P_CUENTA_EXT.MONTO_502(K), VALOR_NUEVO, 'interés valor', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.MONTO_502(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.MONTO_503(1) ,P_CUENTA_EXT.MONTO_503(K), VALOR_NUEVO, 'amortización valor', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.MONTO_503(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.MONTO_504(1) ,P_CUENTA_EXT.MONTO_504(K), VALOR_NUEVO, 'otros depositos valor', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.MONTO_504(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.MONEDA_501(1) ,SF_OBTENER_MONE_X_CODIGO_HOST(P_CUENTA_EXT.MONEDA_501(K)), VALOR_NUEVO, 'código de moneda del dividendo', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.MONEDA_501(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.MONEDA_502(1) ,SF_OBTENER_MONE_X_CODIGO_HOST(P_CUENTA_EXT.MONEDA_502(K)), VALOR_NUEVO, 'código de moneda del interés', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.MONEDA_502(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.MONEDA_503(1) ,SF_OBTENER_MONE_X_CODIGO_HOST(P_CUENTA_EXT.MONEDA_503(K)), VALOR_NUEVO, 'código de moneda de la amortización', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.MONEDA_503(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.MONEDA_504(1) ,SF_OBTENER_MONE_X_CODIGO_HOST(P_CUENTA_EXT.MONEDA_504(K)), VALOR_NUEVO, 'código de moneda de otros depósitos', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.MONEDA_504(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.ORIGEN_503(1) ,P_CUENTA_EXT.ORIGEN_503(K), VALOR_NUEVO, 'origen del dividendo', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.ORIGEN_503(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.ORIGEN_502(1) ,P_CUENTA_EXT.ORIGEN_502(K), VALOR_NUEVO, 'origen del interés', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.ORIGEN_502(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.ORIGEN_501(1) ,P_CUENTA_EXT.ORIGEN_501(K), VALOR_NUEVO, 'origen de la amortización', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.ORIGEN_501(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.ORIGEN_504(1) ,P_CUENTA_EXT.ORIGEN_504(K), VALOR_NUEVO, 'origen de otros depósitos', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.ORIGEN_504(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.CODIGO_CUSTODIO(1) ,P_CUENTA_EXT.CODIGO_CUSTODIO(K), VALOR_NUEVO, 'código custodio', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.CODIGO_CUSTODIO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    SP_COMPARAR_VARIABLES(P_CUENTA.ADMINISTRACION_MERCADO(1) ,P_CUENTA_EXT.ADMINISTRACION_MERCADO(K), VALOR_NUEVO, 'administración de mercado', P_CUENTA.ESTADO_PROCESO(1), MENSAJE_CONFLICTO);
    P_CUENTA.ADMINISTRACION_MERCADO(1) := VALOR_NUEVO;
    MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL || MENSAJE_CONFLICTO;

    IF LENGTH(MENSAJE_CONFLICTO_TOTAL) > 0 THEN
      MENSAJE_CONFLICTO_TOTAL := SUBSTR(MENSAJE_CONFLICTO_TOTAL, 1, LENGTH(MENSAJE_CONFLICTO_TOTAL) - 2);
      MENSAJE_CONFLICTO_TOTAL := MENSAJE_CONFLICTO_TOTAL ||'.';
      MENSAJE_CONFLICTO_TOTAL := 'Existe conflicto con los siguientes campos: ' || MENSAJE_CONFLICTO_TOTAL;
    END IF;

    SP_VALIDAR_CUENTA_PROCESO(P_CUENTA, MENSAJE_OUT);
    IF LENGTH(TRIM(SUBSTR(MENSAJE_OUT, 3))) > 0 AND LENGTH(MENSAJE_CONFLICTO_TOTAL) > 0 THEN
      P_CUENTA.MENSAJE(1) := '[' || MENSAJE_CONFLICTO_TOTAL || ' Los siguientes campos tienen errores: ' || TRIM(SUBSTR(MENSAJE_OUT, 3)) || '. ]';
    ELSIF LENGTH(TRIM(SUBSTR(MENSAJE_OUT, 3))) = 0 AND LENGTH(MENSAJE_CONFLICTO_TOTAL) > 0 THEN
      P_CUENTA.MENSAJE(1) := '[' || MENSAJE_CONFLICTO_TOTAL || ' ]';
    ELSIF LENGTH(TRIM(SUBSTR(MENSAJE_OUT, 3))) > 0 THEN
      P_CUENTA.MENSAJE(1) := '[ Los siguientes campos tienen errores: ' || TRIM(SUBSTR(MENSAJE_OUT, 3)) || '. ]';
    ELSIF LENGTH(MENSAJE_CONFLICTO_TOTAL) > 0 THEN
      P_CUENTA.MENSAJE(1) := '[' || MENSAJE_CONFLICTO_TOTAL || ' ]';      
    END IF;
    
    -- 257 -> Código de Paises ISO
    IF P_CUENTA.NRO_CUENTA.COUNT = 1 THEN
      IF INSTR(P_POOL_MARCAS, '|' || P_CUENTA.CLASIFICACION(1) || '|') > 0 THEN
        P_CUENTA.ESTADO_PROCESO(1) := '4';
      ELSE
        IF P_CUENTA.ESTADO_PROCESO(1) = '8' THEN -- Esta Informado Corregido con Conflicto
          -- Caso 5: Informado con Conflicto
          -- Marcar con 1 cuando no exista conflicto
          IF LENGTH(MENSAJE_CONFLICTO_TOTAL)=0 THEN
            P_CUENTA.ESTADO_PROCESO(1) := '7';
          END IF;
        ELSIF P_CUENTA.ESTADO_PROCESO(1) = '7' THEN -- Esta Informado Corregido
          -- Caso 7: Informado Corregido con Conflicto
          -- Marcar con 8 cuando exista conflicto
          IF LENGTH(MENSAJE_CONFLICTO_TOTAL) > 0 THEN
            P_CUENTA.ESTADO_PROCESO(1) := '8';
          END IF;
        ELSIF P_CUENTA.ESTADO_PROCESO(1) = '1' THEN -- Esta Informado
          -- Caso 1: Informado con Conflicto
          -- Marcar con 5 cuando exista conflicto
          IF LENGTH(MENSAJE_CONFLICTO_TOTAL) > 0 THEN
            P_CUENTA.ESTADO_PROCESO(1) := '5';
          END IF;
        ELSIF P_CUENTA.ESTADO_PROCESO(1) = '5' THEN -- Esta Modificado
          -- Caso 5: Informado con Conflicto
          -- Marcar con 1 cuando no exista conflicto
          IF LENGTH(MENSAJE_CONFLICTO_TOTAL)=0 THEN
            P_CUENTA.ESTADO_PROCESO(1) := '1';
          END IF;
        ELSIF P_CUENTA.ESTADO_PROCESO(1) = '2' THEN -- Esta Modificado
          -- Caso 2: Modificado con Conflicto
          -- Marcar con 6 cuando exista conflicto
          IF LENGTH(MENSAJE_CONFLICTO_TOTAL)>0 THEN
            P_CUENTA.ESTADO_PROCESO(1) := '6';
          END IF;
        ELSIF P_CUENTA.ESTADO_PROCESO(1) = '6' THEN -- Esta Modificado
          -- Caso 6: Modificado con Conflicto
          -- Marcar con 2 cuando no exista conflicto
          IF LENGTH(MENSAJE_CONFLICTO_TOTAL)=0 THEN
            P_CUENTA.ESTADO_PROCESO(1) := '2';
          END IF;
        ELSE
          -- Caso 3: Registro con error
          IF LENGTH(P_CUENTA.MENSAJE(1)) > 0 THEN
            P_CUENTA.ESTADO_PROCESO(1) := '3';
          ELSE
            P_CUENTA.ESTADO_PROCESO(1) := '4';
          END IF;
        END IF;
      END IF;
    END IF;

    -- SETEAMOS EL ID DEL PERIODO ACTIVO
    P_CUENTA.PERIODO_ID(1) := P_CUENTA_EXT.PERIODO_ID(K);
  EXCEPTION
  WHEN OTHERS THEN
    P_CUENTA.MENSAJE(1) := SQLERRM || ' ' || DBMS_UTILITY.FORMAT_ERROR_BACKTRACE;
  END SP_VALIDAR_CUENTA;

  FUNCTION SF_OBTENER_NEXTVAL_CUENTA
  RETURN NUMBER AS
    NEXT_ID NUMBER;
  BEGIN
    SELECT SQ_CUENTAS.NEXTVAL INTO NEXT_ID FROM SYS.DUAL;
    RETURN NEXT_ID;
  EXCEPTION
  WHEN OTHERS THEN
    RETURN NEXT_ID;
  END SF_OBTENER_NEXTVAL_CUENTA;

  FUNCTION SF_OBTENER_PAIS_X_CODIGO_HOST(P_CODIGO_PAIS VARCHAR2) RETURN VARCHAR2 AS
    CODIGO_PAIS VARCHAR2(3);
  BEGIN
    SELECT NVL(MAX(CODIGO), '***') INTO CODIGO_PAIS FROM FATCA.PARAMETRO WHERE PARAMETRO_ID = P_ID_TABLA_PAIS AND ESTADO='A' AND TEXTO = P_CODIGO_PAIS;
    RETURN CODIGO_PAIS;
  END SF_OBTENER_PAIS_X_CODIGO_HOST;

  FUNCTION SF_OBTENER_MONE_X_CODIGO_HOST(P_CODIGO_MONEDA VARCHAR2) RETURN VARCHAR2 AS
    CODIGO_MONEDA VARCHAR2(3);
  BEGIN
    SELECT NVL(MAX(CODIGO), '***') INTO CODIGO_MONEDA FROM FATCA.PARAMETRO WHERE PARAMETRO_ID = P_ID_TABLA_MONEDA AND ESTADO='A' AND TEXTO = P_CODIGO_MONEDA;
    RETURN CODIGO_MONEDA;
  END SF_OBTENER_MONE_X_CODIGO_HOST;

END PKG_PROCESOS_ENTRADA;
/

CREATE OR REPLACE PACKAGE BODY FATCA.PKG_PROCESOS_SALIDA AS

  PROCEDURE SP_CARGAR_FATCA(USUARIO VARCHAR2, ID_PROCESO NUMBER, FILAS_LEIDAS OUT NUMBER, FILAS_ESCRITAS OUT NUMBER, ESTADO OUT VARCHAR2, DESCRIPCION OUT VARCHAR2) IS
    P_CURSOR                SYS_REFCURSOR;
    P_CUENTA                T_CUENTA;
    P_FATCA_OECD_ID         NUMBER := 0;
    P_ID_PERIODO            NUMBER := 0;
    P_ID_OECD               NUMBER := 0;
    P_ID_REPORTING_GROUP    NUMBER := 0;
    P_PERIODO_REPORTING     VARCHAR2(10);
    P_FECHA_COMPILACION     VARCHAR2(30);
    P_TRANSMITTING_COUNTRY  VARCHAR2(2) := 'PE';
    P_RECEIVING_COUNTRY     VARCHAR2(2) := 'US';
    V_POOL_GINN_TEMP        VARCHAR(50) := '---';
    V_TIPO_FONDO_TEMP       VARCHAR(50) := '---';
    V_ENTIDAD_CONTRATO_TEMP VARCHAR(50) := '---';
    V_RFI_DOC_ID_TEMP       VARCHAR(200) := '---';
    V_CORR_MESSAGE_REF_ID   VARCHAR(200) := '---';
    V_CORR_DOC_REF_ID       VARCHAR(200) := '---';
    V_FLAG_TEST             VARCHAR(1) := '-';
    V_COUNT                 NUMBER := 0;
    V_ROW_COUNT             NUMBER := 0;
    V_POOL_MARCAS           VARCHAR(200) := '';
    V_SUSTANCIAL            NUMBER := 0;
    DML_ERRORS    EXCEPTION;
    ERROR_INTERNO EXCEPTION;
    PRAGMA EXCEPTION_INIT(DML_ERRORS    , -24381);
    PRAGMA EXCEPTION_INIT(ERROR_INTERNO , -20001);
  BEGIN
    FILAS_LEIDAS := 0;
    FILAS_ESCRITAS := 0;
    ESTADO := 'COMPLETED';
    DESCRIPCION := 'COMPLETED';

    PKG_UTIL.SP_ALTER_CHARACTER_SESSION;
    SELECT NVL(MAX(ID), -1), MAX(NOMBRE_PERIODO), TO_CHAR(MAX(TO_DATE(FECHA_COMPILACION, 'YYYY-MM-DD-HH24.MI.SS')), 'YYYY-MM-DD"T"HH24:MI:SS')
    INTO P_ID_PERIODO, P_PERIODO_REPORTING, P_FECHA_COMPILACION
    FROM FATCA.PERIODO WHERE ESTADO = '1';
    IF P_ID_PERIODO = -1 THEN
      RAISE_APPLICATION_ERROR( -20001, 'No existen periodos activos');
    END IF;

    SELECT NVL(MAX(BOOLEANO), 'N') INTO V_FLAG_TEST FROM FATCA.PARAMETRO WHERE ID = P_LIMPIAR AND ESTADO = 'A';
    IF V_FLAG_TEST = 'S' THEN
      DELETE FROM FATCA.INTERMEDIARY     WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.SUSTANTIAL_OWNER WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.PAYMENT          WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.ACCOUNT_REPORT   WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.POOL_REPORT      WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.REPORTING_GROUP  WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.FATCA            WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.REPORTING_FI     WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.FATCA_OECD       WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.ORGANISATION     WHERE PERIODO_ID=P_ID_PERIODO;
      DELETE FROM FATCA.PERSON           WHERE PERIODO_ID=P_ID_PERIODO;
      COMMIT;
    END IF;

    SELECT DECODE(NVL(MAX(BOOLEANO), 'S'), 'S', '1', '') INTO V_FLAG_TEST FROM FATCA.PARAMETRO WHERE ID = P_TEST AND ESTADO = 'A';

    SELECT COUNT(1) INTO V_COUNT FROM FATCA.CUENTAS WHERE ESTADO_PROCESO NOT IN('1', '3');
    IF V_COUNT = 0 THEN
      RAISE_APPLICATION_ERROR( -20001, 'No existen elementos para generar el archivo FATCA');
    END IF;

    SELECT NVL(MAX(ID), -1) INTO P_FATCA_OECD_ID FROM FATCA.FATCA_OECD WHERE PERIODO_ID=P_ID_PERIODO;
    V_CORR_MESSAGE_REF_ID := SF_OBTENER_CORREFID(P_ID_PERIODO, P_MESSAGE_SPEC, P_FATCA_OECD_ID, '');

    UPDATE FATCA.SUSTANTIAL_OWNER SET ESTADO=0 WHERE PERIODO_ID=P_ID_PERIODO;
    UPDATE FATCA.PAYMENT          SET ESTADO=0 WHERE PERIODO_ID=P_ID_PERIODO;
    UPDATE FATCA.ACCOUNT_REPORT   SET ESTADO=0 WHERE PERIODO_ID=P_ID_PERIODO;
    UPDATE FATCA.POOL_REPORT      SET ESTADO=0 WHERE PERIODO_ID=P_ID_PERIODO;
    UPDATE FATCA.REPORTING_GROUP  SET ESTADO=0 WHERE PERIODO_ID=P_ID_PERIODO;
    UPDATE FATCA.ORGANISATION     SET ESTADO=0 WHERE PERIODO_ID=P_ID_PERIODO;
    UPDATE FATCA.PERSON           SET ESTADO=0 WHERE PERIODO_ID=P_ID_PERIODO;
    UPDATE FATCA.FATCA_OECD       SET ESTADO=0 WHERE PERIODO_ID=P_ID_PERIODO;
    COMMIT;

    -- INSERTAMOS EN LA TABLA PERSON
    INSERT INTO FATCA.PERSON (
        ID                 , TIN                , RES_COUNTRY_CODE   , FIRST_NAME         ,
        LAST_NAME          , COUNTRY_CODE       , ADDRESS_FREE       , BIRTH_DATE         ,
        ESTADO             , USUARIO_CREACION   , FECHA_CREACION     , PERIODO_ID
    ) SELECT
        SQ_PERSON.NEXTVAL  , P.TIN              , P.PAIS_BANCO       , P.NOMBRE           ,
        P.APELLIDO         , P.PAIS_RESIDENCIA  , P.DIRECCION        , P.FECHA_NACIMIENTO ,
        '1'                , USUARIO            , SYSDATE            , P_ID_PERIODO
    FROM FATCA.VW_PERSONA P WHERE P.TIPO_PERSONA='N';

    INSERT INTO FATCA.PERSON (
        ID                 , TIN                , RES_COUNTRY_CODE   , FIRST_NAME         ,
        LAST_NAME          , COUNTRY_CODE       , ADDRESS_FREE       , BIRTH_DATE         ,
        ESTADO             , USUARIO_CREACION   , FECHA_CREACION     , PERIODO_ID
    ) SELECT
        SQ_PERSON.NEXTVAL  , P.TIN              , P.PAIS_BANCO       , P.NOMBRE           ,
        P.APELLIDO         , P.PAIS_RESIDENCIA  , P.DIRECCION        , P.FECHA_NACIMIENTO ,
        '1'                , USUARIO            , SYSDATE            , P_ID_PERIODO
    FROM (
        SELECT DISTINCT
            TIN      , PAIS_BANCO      , NOMBRE    , FECHA_NACIMIENTO ,
            APELLIDO , PAIS_RESIDENCIA , DIRECCION
        FROM FATCA.VW_PERSONA P WHERE P.TIPO_PERSONA='A'
    ) P;

    -- INSERTAMOS EN LA TABLA ORGANISATION
    INSERT INTO FATCA.ORGANISATION (
        ID                 , TIN                , RES_COUNTRY_CODE  , TIN_ISSUED_BY   ,
        NAME               , COUNTRY_CODE       , ADDRESS_FREE      , DOC_TYPE_INDIC  ,
        DOC_REF_ID         , CORR_MESSAGE_REF_ID, CORR_DOC_REF_ID   , ESTADO          ,
        USUARIO_CREACION   , FECHA_CREACION     , PERIODO_ID
    ) SELECT
        SQ_ORGANISATION.NEXTVAL , Z.TIN                , Z.RES_COUNTRY_CODE  , Z.TIN_ISSUED_BY   ,
        Z.NAME                  , Z.COUNTRY_CODE       , Z.ADDRESS_FREE      , Z.DOC_TYPE_INDIC  ,
        Z.DOC_REF_ID            , Z.CORR_MESSAGE_REF_ID, Z.CORR_DOC_REF_ID   , '1'               ,
        USUARIO                 , SYSDATE              , P_ID_PERIODO
    FROM (
        SELECT
            P.TIN
          , P.PAIS_BANCO RES_COUNTRY_CODE
          , P.PAIS_RECEPTOR TIN_ISSUED_BY
          , P.NOMBRE NAME
          , P.PAIS_RESIDENCIA COUNTRY_CODE
          , P.DIRECCION ADDRESS_FREE
          , 'FATCA' || V_FLAG_TEST || P.ESTADO DOC_TYPE_INDIC
          , SF_OBTENER_DOCREFID(NULL, NULL, NULL, P_REPORTING_FI, P.TIN) DOC_REF_ID
          , V_CORR_MESSAGE_REF_ID CORR_MESSAGE_REF_ID
          , '***' CORR_DOC_REF_ID
        FROM FATCA.VW_PERSONA P WHERE P.TIPO_PERSONA='J'
        UNION ALL
        SELECT
            P.TIN
          , P.PAIS_BANCO RES_COUNTRY_CODE
          , P.PAIS_RECEPTOR TIN_ISSUED_BY
          , P.NOMBRE NAME
          , P.PAIS_RESIDENCIA COUNTRY_CODE
          , P.DIRECCION ADDRESS_FREE
          , 'FATCA' || V_FLAG_TEST ||  P.ESTADO DOC_TYPE_INDIC
          , SF_OBTENER_DOCREFID(NULL, NULL, P.TIN, P_INTERMEDIARY, NULL) DOC_REF_ID
          , V_CORR_MESSAGE_REF_ID CORR_MESSAGE_REF_ID
          , '***' CORR_DOC_REF_ID
        FROM FATCA.VW_PERSONA P WHERE P.TIPO_PERSONA='T'
        UNION ALL
        SELECT
            B.CODIGO TIN
          , B.TEXTO2 PAIS_BANCO
          , NVL(A.PAIS_RECEPTOR, 'US') PAIS_RECEPTOR
          , B.TEXTO NOMBRE
          , B.TEXTO2 PAIS_RESIDENCIA
          , B.TEXTO3 DIRECCION
          , '***' DOC_TYPE_INDIC
          , SF_OBTENER_DOCREFID(NULL, NULL, NULL, P_REPORTING_FI, UPPER(SUBSTR(B.NOMBRE, 1, 2))) DOC_REF_ID
          , V_CORR_MESSAGE_REF_ID CORR_MESSAGE_REF_ID
          , '***' CORR_DOC_REF_ID
        FROM (
             SELECT ENTIDAD_CONTRATO, PAIS_BANCO, PAIS_RECEPTOR
             FROM FATCA.CUENTAS
             WHERE PERIODO_ID = P_ID_PERIODO AND ESTADO_PROCESO NOT IN('1', '3')
             GROUP BY ENTIDAD_CONTRATO, PAIS_BANCO, PAIS_RECEPTOR
        ) A INNER JOIN FATCA.PARAMETRO B ON UPPER(NOMBRE) = UPPER(ENTIDAD_CONTRATO) AND B.PARAMETRO_ID = 17 AND ESTADO = 'A'
    ) Z;

    UPDATE FATCA.ORGANISATION SET TIPO = CASE WHEN DOC_TYPE_INDIC = '***' THEN 'R' ELSE 'O' END WHERE ESTADO='1';
    FOR P_ORG IN (SELECT C.TIN FROM FATCA.ORGANISATION C WHERE C.ESTADO = '1' GROUP BY C.TIN)
    LOOP
      V_RFI_DOC_ID_TEMP := SF_OBTENER_CORREFID(NULL, P_REPORTING_FI, P_FATCA_OECD_ID, P_ORG.TIN);
      UPDATE FATCA.ORGANISATION
      SET CORR_MESSAGE_REF_ID = DECODE(V_RFI_DOC_ID_TEMP, NULL, '', CORR_MESSAGE_REF_ID),
          CORR_DOC_REF_ID = V_RFI_DOC_ID_TEMP,
          DOC_TYPE_INDIC = CASE WHEN DOC_TYPE_INDIC = '***' THEN 'FATCA' || V_FLAG_TEST || DECODE(NVL(V_RFI_DOC_ID_TEMP, '***'), '***', '1', '2') ELSE DOC_TYPE_INDIC END
      WHERE TIN = P_ORG.TIN AND ESTADO='1';
    END LOOP;

    -- INSERTAMOS EN LA TABLA REPORTING_FI
    INSERT INTO FATCA.REPORTING_FI (ID, ORGANISATION_ID, PERIODO_ID)
    SELECT SQ_REPORTING_FI.NEXTVAL, A.ID, P_ID_PERIODO
    FROM FATCA.ORGANISATION A
    WHERE A.ESTADO='1' AND TIPO = 'R' AND A.PERIODO_ID=P_ID_PERIODO;

    -- CONSULTAMOS EL FATCA_OECD DEL PERIODO
    SELECT SQ_FATCA_OECD.NEXTVAL INTO P_ID_OECD FROM SYS.DUAL;
    SELECT NVL(MAX(CODIGO), 'US') INTO P_RECEIVING_COUNTRY FROM FATCA.PARAMETRO WHERE ID = P_PAIS_REPORTA AND ESTADO = 'A';
    SELECT NVL(MAX(TEXTO2), 'PE') INTO P_TRANSMITTING_COUNTRY FROM FATCA.PARAMETRO WHERE ID = P_GIN_BANCO AND ESTADO = 'A';

    INSERT INTO FATCA.FATCA_OECD(ID
      , SENDING_COMPANY_IN
      , TRANSMITTING_COUNTRY
      , RECEIVING_COUNTRY
      , MESSAGE_REF_ID
      , CORRMESSAGE_REF_ID
      , REPORTING_PERIOD
      , TIMESTAMP_SEND
      , ESTADO
      , USUARIO_CREACION
      , FECHA_CREACION
      , PERIODO_ID)
    VALUES (P_ID_OECD
    , (SELECT CODIGO FROM FATCA.PARAMETRO WHERE ID = 18 AND ESTADO = 'A')
    , P_TRANSMITTING_COUNTRY
    , P_RECEIVING_COUNTRY
    , SF_OBTENER_DOCREFID(NULL, NULL, NULL, P_MESSAGE_SPEC, NULL)
    , V_CORR_MESSAGE_REF_ID
    , P_PERIODO_REPORTING
    , P_FECHA_COMPILACION
    , '1'
    , USUARIO
    , SYSDATE
    , P_ID_PERIODO);

    -- INSERTAMOS EN LA TABLA FATCA
    INSERT INTO FATCA.FATCA (ID, REPORTING_FI_ID, FATCA_OECD_ID, PERIODO_ID)
    SELECT SQ_FATCA.NEXTVAL, A.ID, P_ID_OECD, P_ID_PERIODO FROM FATCA.REPORTING_FI A
    INNER JOIN ORGANISATION B ON A.ORGANISATION_ID=B.ID AND B.ESTADO='1' AND B.PERIODO_ID=P_ID_PERIODO;

    -- OBTENER MARCAS DE TIPO POOL
    FOR P_POOL IN (SELECT TEXTO FROM FATCA.PARAMETRO WHERE PARAMETRO_ID=P_TIPO_POOL AND TEXTO IS NOT NULL AND ESTADO='A')
    LOOP
        V_POOL_MARCAS := V_POOL_MARCAS || SUBSTR(P_POOL.TEXTO, 1, LENGTH(P_POOL.TEXTO) - 1);
    END LOOP;
    V_POOL_MARCAS := V_POOL_MARCAS || '|';

    OPEN P_CURSOR FOR SELECT
      ''                        , CODIGO_GIIN               , PAIS_BANCO                , PAIS_RECEPTOR             ,
      TIPO_REPORTE              , CODIGO_REFERENCIA         , PERIODO_INFORMADO         , FECHA_COMPILACION         ,
      ID_ENTIDAD                , CODIGO_CLIENTE            , TIPO_DOCUMENTO            , NRO_DOCUMENTO             ,
      CLASIFICACION             , TIN                       , NOMBRE                    , APELLIDO                  ,
      DIRECCION                 , PAIS_RESIDENCIA           , CIUDAD                    , PROVINCIA                 ,
      CODIGO_POSTAL             , REFERENCIA_DIRECCION      , FECHA_NACIMIENTO          , GRUPO_REPORTE             ,
      ID_DOC_SPEC               , ID_REPORTE                , NRO_CUENTA                , TITULAR_CUENTA            ,
      PROPIETARIOS_SUSTANCIALES , SALDO_CUENTA              , SALDO_MONEDA              , ENTIDAD_CONTRATO          ,
      VALOR_NOMINAL             , VALOR_NOMINAL_MONEDA      , VALOR_MERCADO             , VALOR_MERCADO_MONEDA      ,
      TIPO_FONDO                , MONTO_501                 , MONTO_502                 , MONTO_503                 ,
      MONTO_504                 , MONEDA_501                , MONEDA_502                , MONEDA_503                ,
      MONEDA_504                , ORIGEN_501                , ORIGEN_502                , ORIGEN_503                ,
      ORIGEN_504                , CODIGO_CUSTODIO           , ADMINISTRACION_MERCADO    , PERIODO_ID                ,
      0 LINEA                   , ESTADO                    , ESTADO_PROCESO            , ''
    FROM FATCA.CUENTAS A
    WHERE PERIODO_ID = P_ID_PERIODO AND ESTADO_PROCESO NOT IN('1', '3') AND ENTIDAD_CONTRATO != 'FONDOS' AND INSTR(V_POOL_MARCAS, '|' || CLASIFICACION || '|') = 0
    ORDER BY ENTIDAD_CONTRATO;

    LOOP
      FETCH P_CURSOR BULK COLLECT INTO P_CUENTA LIMIT 500;
      EXIT WHEN P_CUENTA.LINEA.COUNT = 0;

      BEGIN
        FOR I IN 1..P_CUENTA.LINEA.COUNT LOOP
            V_SUSTANCIAL := SF_VERIFICAR_ACCOUNT(P_CUENTA, I, P_ID_PERIODO, USUARIO, ID_PROCESO);
            IF V_SUSTANCIAL > 0 THEN
              IF V_ENTIDAD_CONTRATO_TEMP != P_CUENTA.ENTIDAD_CONTRATO(I) THEN
                V_ENTIDAD_CONTRATO_TEMP := P_CUENTA.ENTIDAD_CONTRATO(I);

                -- INSERTAMOS EN TABLA REPORTING_GROUP
                SELECT SQ_REPORTING_GROUP.NEXTVAL INTO P_ID_REPORTING_GROUP FROM SYS.DUAL;
                INSERT INTO FATCA.REPORTING_GROUP (ID
                   , ESTADO
                   , USUARIO_CREACION
                   , FECHA_CREACION
                   , FATCA_ID
                   , PERIODO_ID)
                VALUES (
                     P_ID_REPORTING_GROUP
                   , '1'
                   , USUARIO
                   , SYSDATE
                   , (SELECT F.ID FROM FATCA.FATCA F
                      INNER JOIN FATCA.REPORTING_FI R ON F.REPORTING_FI_ID = R.ID
                      INNER JOIN FATCA.ORGANISATION O ON R.ORGANISATION_ID = O.ID
                      WHERE O.TIN = P_CUENTA.CODIGO_GIIN(I) AND O.ESTADO=1 AND O.PERIODO_ID=P_ID_PERIODO)
                   , P_ID_PERIODO
                );
                V_ROW_COUNT := SQL%ROWCOUNT;
                FILAS_LEIDAS   := FILAS_LEIDAS   + V_ROW_COUNT;
                FILAS_ESCRITAS := FILAS_ESCRITAS + V_ROW_COUNT;
              END IF;

              SP_INSERTAR_ACCOUNT(P_CUENTA, I, P_FATCA_OECD_ID, P_ID_PERIODO, USUARIO, ID_PROCESO, P_ID_REPORTING_GROUP, V_CORR_MESSAGE_REF_ID, V_FLAG_TEST, V_SUSTANCIAL);
            END IF;
        END LOOP;
      END;
    END LOOP;

    OPEN P_CURSOR FOR SELECT
      ''                        , CODIGO_GIIN               , PAIS_BANCO                , PAIS_RECEPTOR             ,
      TIPO_REPORTE              , CODIGO_REFERENCIA         , PERIODO_INFORMADO         , FECHA_COMPILACION         ,
      ID_ENTIDAD                , CODIGO_CLIENTE            , TIPO_DOCUMENTO            , NRO_DOCUMENTO             ,
      CLASIFICACION             , TIN                       , NOMBRE                    , APELLIDO                  ,
      DIRECCION                 , PAIS_RESIDENCIA           , CIUDAD                    , PROVINCIA                 ,
      CODIGO_POSTAL             , REFERENCIA_DIRECCION      , FECHA_NACIMIENTO          , GRUPO_REPORTE             ,
      ID_DOC_SPEC               , ID_REPORTE                , NRO_CUENTA                , TITULAR_CUENTA            ,
      PROPIETARIOS_SUSTANCIALES , SALDO_CUENTA              , SALDO_MONEDA              , ENTIDAD_CONTRATO          ,
      VALOR_NOMINAL             , VALOR_NOMINAL_MONEDA      , VALOR_MERCADO             , VALOR_MERCADO_MONEDA      ,
      TIPO_FONDO                , MONTO_501                 , MONTO_502                 , MONTO_503                 ,
      MONTO_504                 , MONEDA_501                , MONEDA_502                , MONEDA_503                ,
      MONEDA_504                , ORIGEN_501                , ORIGEN_502                , ORIGEN_503                ,
      ORIGEN_504                , CODIGO_CUSTODIO           , ADMINISTRACION_MERCADO    , PERIODO_ID                ,
      0 LINEA                   , ESTADO                    , ESTADO_PROCESO            , ''
    FROM FATCA.CUENTAS
    WHERE PERIODO_ID = P_ID_PERIODO AND ESTADO_PROCESO NOT IN('1', '3') AND ENTIDAD_CONTRATO = 'FONDOS' AND INSTR(V_POOL_MARCAS, '|' || CLASIFICACION || '|') = 0
    ORDER BY TIPO_FONDO;

    LOOP
      FETCH P_CURSOR BULK COLLECT INTO P_CUENTA LIMIT 500;
      EXIT WHEN P_CUENTA.LINEA.COUNT = 0;

      BEGIN
        FOR I IN 1..P_CUENTA.LINEA.COUNT LOOP
            V_SUSTANCIAL := SF_VERIFICAR_ACCOUNT(P_CUENTA, I, P_ID_PERIODO, USUARIO, ID_PROCESO);
            IF V_SUSTANCIAL > 0 THEN
              IF V_TIPO_FONDO_TEMP != P_CUENTA.TIPO_FONDO(I) THEN
                V_TIPO_FONDO_TEMP := P_CUENTA.TIPO_FONDO(I);

                -- INSERTAMOS EN TABLA REPORTING_GROUP
                SELECT SQ_REPORTING_GROUP.NEXTVAL INTO P_ID_REPORTING_GROUP FROM SYS.DUAL;
                INSERT INTO FATCA.REPORTING_GROUP (ID
                   , ESTADO
                   , USUARIO_CREACION
                   , FECHA_CREACION
                   , FATCA_ID
                   , PERIODO_ID)
                VALUES (
                     P_ID_REPORTING_GROUP
                   , '1'
                   , USUARIO
                   , SYSDATE
                   , (SELECT F.ID FROM FATCA.FATCA F
                      INNER JOIN FATCA.REPORTING_FI R ON F.REPORTING_FI_ID = R.ID
                      INNER JOIN FATCA.ORGANISATION O ON R.ORGANISATION_ID = O.ID
                      WHERE O.TIN = P_CUENTA.CODIGO_GIIN(I) AND O.ESTADO=1 AND O.PERIODO_ID=P_ID_PERIODO)
                   , P_ID_PERIODO
                );
                V_ROW_COUNT := SQL%ROWCOUNT;
                FILAS_LEIDAS   := FILAS_LEIDAS   + V_ROW_COUNT;
                FILAS_ESCRITAS := FILAS_ESCRITAS + V_ROW_COUNT;

                -- INSERTAR EL INTERMEDIARIO
                INSERT INTO FATCA.INTERMEDIARY (ID, REPORTING_GROUP_ID, ORGANISATION_ID, PERIODO_ID)
                VALUES (SQ_INTERMEDIARY.NEXTVAL, P_ID_REPORTING_GROUP, (
                  SELECT O.ID FROM FATCA.ORGANISATION O
                  INNER JOIN FATCA.VW_PERSONA P ON O.TIN=P.TIN
                  WHERE O.ESTADO='1' AND P.CODIGO_CLIENTE=P_CUENTA.TIPO_FONDO(I) AND ROWNUM=1
                ), P_ID_PERIODO);
              END IF;

              SP_INSERTAR_ACCOUNT(P_CUENTA, I, P_FATCA_OECD_ID, P_ID_PERIODO, USUARIO, ID_PROCESO, P_ID_REPORTING_GROUP, V_CORR_MESSAGE_REF_ID, V_FLAG_TEST, V_SUSTANCIAL);
            END IF;
        END LOOP;
      END;
    END LOOP;

    FOR P_POOL IN (
        SELECT
            C.CODIGO_GIIN
          , DECODE(T.TEXTO2, P_COL_SALDO, C.SALDO_MONEDA, P_COL_VALOR, C.VALOR_MERCADO_MONEDA, NULL) SALDO_MONEDA
          , P.NOMBRE
          , P.CODIGO TIPO_POOL
          , '1' ESTADO
          , SUM(TO_NUMBER(DECODE(T.TEXTO2, P_COL_SALDO, C.SALDO_CUENTA, P_COL_VALOR, C.VALOR_MERCADO, NULL))) AS SALDO
          , COUNT(1) AS CANTIDAD
        FROM FATCA.CUENTAS C
          INNER JOIN FATCA.PARAMETRO P ON P.PARAMETRO_ID = P_TIPO_POOL AND P.ESTADO = 'A'AND INSTR(P.TEXTO, '|' || C.CLASIFICACION || '|') > 0
          INNER JOIN FATCA.PARAMETRO T ON T.PARAMETRO_ID = P_REGLAS_MARCAS
                AND INSTR(T.TEXTO, '|' || C.CLASIFICACION || '|') > 0
                AND T.CODIGO = C.ENTIDAD_CONTRATO
                AND (
                    (T.ENTERO = -1) OR
                    (T.ENTERO = 0 AND LENGTH(C.NRO_CUENTA) < 20) OR
                    (T.ENTERO > 0 AND LENGTH(C.NRO_CUENTA) = T.ENTERO)
                )
        WHERE C.PERIODO_ID=P_ID_PERIODO AND C.ESTADO_PROCESO NOT IN('1', '3')
        GROUP BY
            C.CODIGO_GIIN
          , DECODE(T.TEXTO2, P_COL_SALDO, C.SALDO_MONEDA, P_COL_VALOR, C.VALOR_MERCADO_MONEDA, NULL)
          , P.NOMBRE
          , P.CODIGO
          , C.ESTADO
        ORDER BY C.CODIGO_GIIN
    )
    LOOP
        IF V_POOL_GINN_TEMP != P_POOL.CODIGO_GIIN THEN
          V_POOL_GINN_TEMP := P_POOL.CODIGO_GIIN;

          -- INSERTAMOS EN TABLA REPORTING_GROUP
          SELECT SQ_REPORTING_GROUP.NEXTVAL INTO P_ID_REPORTING_GROUP FROM SYS.DUAL;
          INSERT INTO FATCA.REPORTING_GROUP (ID
             , ESTADO
             , USUARIO_CREACION
             , FECHA_CREACION
             , FATCA_ID
             , PERIODO_ID)
          VALUES (
               P_ID_REPORTING_GROUP
             , '1'
             , USUARIO
             , SYSDATE
             , (SELECT F.ID FROM FATCA.FATCA F
                INNER JOIN FATCA.REPORTING_FI R ON F.REPORTING_FI_ID = R.ID
                INNER JOIN FATCA.ORGANISATION O ON R.ORGANISATION_ID = O.ID
                WHERE O.TIN = P_POOL.CODIGO_GIIN AND O.ESTADO=1 AND O.PERIODO_ID=P_ID_PERIODO)
             , P_ID_PERIODO
          );
          V_ROW_COUNT := SQL%ROWCOUNT;
          FILAS_LEIDAS   := FILAS_LEIDAS   + V_ROW_COUNT;
          FILAS_ESCRITAS := FILAS_ESCRITAS + V_ROW_COUNT;
        END IF;

        -- INSERTAMOS EN TABLA POOL_REPORT
        V_CORR_DOC_REF_ID := SF_OBTENER_CORREFID(NULL, P_POOL_REPORT, P_FATCA_OECD_ID, P_POOL.CODIGO_GIIN || '-' || P_POOL.TIPO_POOL || '-' || P_POOL.SALDO_MONEDA);
        INSERT INTO FATCA.POOL_REPORT (
              ID
            , ACCOUNT_COUNT
            , ACCOUNT_POOL_REPORT_TYPE
            , POOL_BALANCE_CURR_CODE
            , POOL_BALANCE_VALUE
            , DOC_TYPE_INDIC
            , DOC_REF_ID
            , CORR_MESSAGE_REF_ID
            , CORR_DOC_REF_ID
            , ESTADO
            , USUARIO_CREACION
            , FECHA_CREACION
            , REPORTING_GROUP_ID
            , PERIODO_ID
        ) VALUES (
              SQ_POOL_REPORT.NEXTVAL
            , P_POOL.CANTIDAD
            , P_POOL.TIPO_POOL
            , P_POOL.SALDO_MONEDA
            , TRIM(TO_CHAR(TO_NUMBER(P_POOL.SALDO), '999999999999999990.00'))
            , 'FATCA' || V_FLAG_TEST || DECODE(V_CORR_DOC_REF_ID, NULL, P_POOL.ESTADO, '4')
            , SF_OBTENER_DOCREFID(NULL, NULL, NULL, P_POOL_REPORT, P_POOL.CODIGO_GIIN || '-' || P_POOL.TIPO_POOL || '-' || P_POOL.SALDO_MONEDA)
            , DECODE(V_CORR_DOC_REF_ID, NULL, '', V_CORR_MESSAGE_REF_ID)
            , V_CORR_DOC_REF_ID
            , '1'
            , USUARIO
            , SYSDATE
            , P_ID_REPORTING_GROUP
            , P_ID_PERIODO);
    END LOOP;

    UPDATE FATCA.CUENTAS SET ESTADO_PROCESO='1' WHERE PERIODO_ID=P_ID_PERIODO AND  ESTADO_PROCESO NOT IN('1', '3');
    COMMIT;
    EXCEPTION
      WHEN OTHERS THEN
        ESTADO := 'ERROR';
        DESCRIPCION := 'Error: ' || SQLERRM || ' \ ' || DBMS_UTILITY.FORMAT_ERROR_BACKTRACE;
        ROLLBACK;
  END SP_CARGAR_FATCA;

  PROCEDURE SP_INSERTAR_PAYMENT(P_ID_PAYMENT NUMBER, P_SALDO VARCHAR2, P_MONEDA VARCHAR2, P_ORIGEN VARCHAR2, P_USUARIO VARCHAR2, P_ACCOUNT_REPORT_ID NUMBER, P_PARAMETRO PARAMETRO%ROWTYPE, P_ID_PERIODO NUMBER) IS
    V_TIPO_PAYMENT VARCHAR2(25) := '';
  BEGIN
    SELECT CODIGO INTO V_TIPO_PAYMENT FROM FATCA.PARAMETRO WHERE ID = P_ID_PAYMENT AND ESTADO = 'A';

    IF INSTR(P_PARAMETRO.TEXTO3, '|' || V_TIPO_PAYMENT || '|') > 0
        AND P_SALDO IS NOT NULL
        AND TO_NUMBER(P_SALDO)>0
        AND SUBSTR(UPPER(NVL(P_ORIGEN, '--')), 1, 1) = P_PARAMETRO.BOOLEANO
    THEN
      INSERT INTO FATCA.PAYMENT(ID, TYPE, CURR_CODE, VALUE, ESTADO, USUARIO_CREACION, FECHA_CREACION, ACCOUNT_REPORT_ID, PERIODO_ID)
      VALUES (SQ_PAYMENT.NEXTVAL
             , V_TIPO_PAYMENT
             , P_MONEDA
             , TRIM(TO_CHAR(TO_NUMBER(P_SALDO), '999999999999999990.00'))
             , '1'
             , P_USUARIO
             , SYSDATE
             , P_ACCOUNT_REPORT_ID
             , P_ID_PERIODO);
    END IF;
  END SP_INSERTAR_PAYMENT;

  PROCEDURE SP_INSERTAR_SUSTANCIAL(P_USUARIO VARCHAR2, P_ACCOUNT_REPORT_ID NUMBER, P_CODIGOCLIENTE VARCHAR2, P_ID_PERIODO NUMBER) IS
  BEGIN
    INSERT INTO FATCA.SUSTANTIAL_OWNER (
          ID
        , ESTADO
        , USUARIO_CREACION
        , FECHA_CREACION
        , ACCOUNT_REPORT_ID
        , PERSON_ID
        , PERIODO_ID
    )
    SELECT
          SQ_SUSTANTIAL_OWNER.NEXTVAL ID
        , P.ESTADO
        , P.USUARIO_CREACION
        , P.FECHA_CREACION
        , P.ACCOUNT_REPORT_ID
        , P.PERSON_ID
        , P.PERIODO_ID
    FROM (
        SELECT DISTINCT
              '1' ESTADO
            , P_USUARIO USUARIO_CREACION
            , SYSDATE FECHA_CREACION
            , P_ACCOUNT_REPORT_ID ACCOUNT_REPORT_ID
            , A.ID PERSON_ID
            , P_ID_PERIODO PERIODO_ID
        FROM FATCA.PERSON A INNER JOIN FATCA.VW_PERSONA B ON A.TIN=B.TIN
        WHERE B.CODIGO_CLIENTE=P_CODIGOCLIENTE AND A.ESTADO='1' AND B.TIPO_PERSONA='A' AND A.PERIODO_ID=P_ID_PERIODO
    ) P;
  END SP_INSERTAR_SUSTANCIAL;

  PROCEDURE SP_INSERTAR_ACCOUNT(P_CUENTA T_CUENTA, K NUMBER, P_FATCA_OECD_ID NUMBER, P_ID_PERIODO NUMBER, USUARIO VARCHAR2, P_ID_PROCESO NUMBER, P_REPORTING_GROUP_ID NUMBER, P_CORR_MESSAGE_REF_ID VARCHAR2, P_FLAG_TEST VARCHAR2, P_SUSTANCIAL NUMBER) IS
    P_SALDO                VARCHAR2(50);
    P_MONEDA               VARCHAR2(3);
    P_ACCOUNT_HOLDER_TYPE  VARCHAR2(20);
    P_ACCOUNT_REPORT_ID    NUMBER := 0;
    V_CORR_DOC_REF_ID      VARCHAR2(200) := '---';
    V_PARAMETRO            PARAMETRO%ROWTYPE;
  BEGIN
      -- VERIFICAMOS EL TIPO DE CASO
      BEGIN
          SELECT * INTO V_PARAMETRO FROM FATCA.PARAMETRO WHERE PARAMETRO_ID=P_REGLAS_MARCAS
            AND INSTR(TEXTO, '|' || P_CUENTA.CLASIFICACION(K) || '|') > 0
            AND CODIGO = P_CUENTA.ENTIDAD_CONTRATO(K)
            AND (
                (ENTERO = -1) OR
                (ENTERO = 0 AND LENGTH(P_CUENTA.NRO_CUENTA(K)) < 20) OR
                (ENTERO > 0 AND LENGTH(P_CUENTA.NRO_CUENTA(K)) = ENTERO)
            );
      EXCEPTION
        WHEN OTHERS THEN
          PKG_UTIL.SP_REGISTRAR_INFO('Para la cuenta ' || P_CUENTA.NRO_CUENTA(K) || ' del cliente ' || P_CUENTA.CODIGO_CLIENTE(K) || ' no cuenta con clasificación(' || P_CUENTA.ENTIDAD_CONTRATO(K) || ':' || P_CUENTA.CLASIFICACION(K) || ') válida.', NULL, NULL, P_ID_PROCESO, P_ID_PERIODO, USUARIO);
          RETURN;
      END;

      IF V_PARAMETRO.TEXTO2 = P_COL_SALDO THEN
        P_SALDO  := P_CUENTA.SALDO_CUENTA(K);
        P_MONEDA := P_CUENTA.SALDO_MONEDA(K);
      ELSIF V_PARAMETRO.TEXTO2 = P_COL_VALOR THEN
        P_SALDO  := P_CUENTA.VALOR_MERCADO(K);
        P_MONEDA := P_CUENTA.VALOR_MERCADO_MONEDA(K);
      END IF;
      P_SALDO := TRIM(TO_CHAR(TO_NUMBER(P_SALDO), '999999999999999990.00'));

      -- OBTIENE EL TIPO DE ACCOUNT HOLDER
      SELECT NVL(MAX(CODIGO), '---') INTO P_ACCOUNT_HOLDER_TYPE FROM FATCA.PARAMETRO
      WHERE PARAMETRO_ID = P_TIPO_ACCOUNT AND ESTADO = 'A' AND TEXTO LIKE '%|' || P_CUENTA.CLASIFICACION(K) || '|%';

      -- INSERTAMOS LOS ACCOUNT_REPORT, SI NO ES DE TIPO POOL
      IF P_ACCOUNT_HOLDER_TYPE != '---' THEN
        SELECT SQ_ACCOUNT_REPORT.NEXTVAL INTO P_ACCOUNT_REPORT_ID FROM SYS.DUAL;

        -- INSERTAMOS EN LA TABLA ACCOUNT_REPORT
        V_CORR_DOC_REF_ID := SF_OBTENER_CORREFID(P_CUENTA.NRO_CUENTA(K), P_ACCOUNT_REPORT, P_FATCA_OECD_ID, '');
        INSERT INTO FATCA.ACCOUNT_REPORT (
            ID                        , ACCOUNT_NUMBER        , ACCOUNT_HOLDER_TYPE
          , ACCOUNT_BALANCE_CURR_CODE , ACCOUNT_BALANCE_VALUE , DOC_TYPE_INDIC
          , DOC_REF_ID                , CORR_MESSAGE_REF_ID   , CORR_DOC_REF_ID
          , ESTADO                    , USUARIO_CREACION      , FECHA_CREACION
          , REPORTING_GROUP_ID        , PERSON_ID             , ORGANISATION_ID
          , PERIODO_ID
        ) VALUES (
            P_ACCOUNT_REPORT_ID
          , P_CUENTA.NRO_CUENTA(K)
          , P_ACCOUNT_HOLDER_TYPE
          , P_MONEDA
          , P_SALDO
          , 'FATCA' || P_FLAG_TEST || P_CUENTA.ESTADO(K)
          , SF_OBTENER_DOCREFID(P_CUENTA.NRO_CUENTA(K), P_CUENTA.CODIGO_CLIENTE(K), P_CUENTA.ENTIDAD_CONTRATO(K), P_ACCOUNT_REPORT, '')
          , DECODE(V_CORR_DOC_REF_ID, NULL, '', P_CORR_MESSAGE_REF_ID)
          , V_CORR_DOC_REF_ID
          , '1'
          , USUARIO
          , SYSDATE
          , P_REPORTING_GROUP_ID
          , (SELECT ID FROM FATCA.PERSON       A WHERE A.TIN = P_CUENTA.TIN(K) AND ESTADO='1' AND PERIODO_ID=P_ID_PERIODO AND ROWNUM=1)
          , (SELECT ID FROM FATCA.ORGANISATION A WHERE A.TIN = P_CUENTA.TIN(K) AND ESTADO='1' AND PERIODO_ID=P_ID_PERIODO AND ROWNUM=1)
          , P_ID_PERIODO
        );

        IF P_SUSTANCIAL > 0 THEN
          SP_INSERTAR_SUSTANCIAL(USUARIO, P_ACCOUNT_REPORT_ID, P_CUENTA.CODIGO_CLIENTE(K), P_ID_PERIODO);
        END IF;

        -- INSERTAMOS EN LA TABLA PAYMENT
        SP_INSERTAR_PAYMENT(P_PAYMENT_501, P_CUENTA.MONTO_501(K), P_CUENTA.MONEDA_501(K), P_CUENTA.ORIGEN_501(K), USUARIO, P_ACCOUNT_REPORT_ID, V_PARAMETRO, P_ID_PERIODO);
        SP_INSERTAR_PAYMENT(P_PAYMENT_502, P_CUENTA.MONTO_502(K), P_CUENTA.MONEDA_502(K), P_CUENTA.ORIGEN_502(K), USUARIO, P_ACCOUNT_REPORT_ID, V_PARAMETRO, P_ID_PERIODO);
        SP_INSERTAR_PAYMENT(P_PAYMENT_503, P_CUENTA.MONTO_503(K), P_CUENTA.MONEDA_503(K), P_CUENTA.ORIGEN_503(K), USUARIO, P_ACCOUNT_REPORT_ID, V_PARAMETRO, P_ID_PERIODO);
        SP_INSERTAR_PAYMENT(P_PAYMENT_504, P_CUENTA.MONTO_504(K), P_CUENTA.MONEDA_504(K), P_CUENTA.ORIGEN_504(K), USUARIO, P_ACCOUNT_REPORT_ID, V_PARAMETRO, P_ID_PERIODO);
      END IF;
  END SP_INSERTAR_ACCOUNT;

  FUNCTION SF_VERIFICAR_ACCOUNT(P_CUENTA T_CUENTA, K NUMBER, P_ID_PERIODO NUMBER, P_USUARIO VARCHAR2, P_ID_PROCESO NUMBER)
  RETURN NUMBER AS
    V_SUSTANCIAL     NUMBER := 0;
    V_CTA_SUSTANCIAL NUMBER := 0;
    V_RESULT         NUMBER := 0;
  BEGIN
      -- VERIFICAMOS SI TIENE ACCIONISTAS
      SELECT COUNT(1) INTO V_SUSTANCIAL FROM FATCA.PARAMETRO
      WHERE ID=P_ACCIONISTAS AND ESTADO = 'A' AND INSTR(TEXTO, '|' || P_CUENTA.CLASIFICACION(K) || '|') > 0;

      SELECT COUNT(1) INTO V_CTA_SUSTANCIAL FROM FATCA.PERSON A INNER JOIN FATCA.VW_PERSONA B ON A.TIN=B.TIN
      WHERE B.CODIGO_CLIENTE=P_CUENTA.CODIGO_CLIENTE(K) AND A.ESTADO='1' AND B.TIPO_PERSONA='A' AND A.PERIODO_ID=P_ID_PERIODO;

      IF V_SUSTANCIAL = 1 AND V_CTA_SUSTANCIAL = 0 THEN
        PKG_UTIL.SP_REGISTRAR_INFO('La cuenta ' || P_CUENTA.NRO_CUENTA(K) || ' del cliente ' || P_CUENTA.CODIGO_CLIENTE(K) || ' no cuenta con accionistas.', NULL, NULL, P_ID_PROCESO, P_ID_PERIODO, P_USUARIO);
        V_RESULT := 0;
      ELSIF V_SUSTANCIAL = 1 AND V_CTA_SUSTANCIAL > 0 THEN
        V_RESULT := V_CTA_SUSTANCIAL;
      ELSIF V_SUSTANCIAL = 0 THEN
        V_RESULT := 1;
      END IF;

      RETURN V_RESULT;
  END SF_VERIFICAR_ACCOUNT;

  FUNCTION SF_OBTENER_DOCREFID(P_NRO_CUENTA VARCHAR2, P_CODIGO_CLIENTE VARCHAR2, P_TIPO_FONDO VARCHAR2, P_PROCESO VARCHAR2, P_GIIN_ORGANISATION VARCHAR2)
  RETURN VARCHAR2 AS
    V_PREFIJO               VARCHAR2(30)  := 'PE';
    V_SUFIJO                VARCHAR2(20);
    V_REFERENCIA            VARCHAR2(250);
    V_PERIODO               VARCHAR2(50);
    V_FECHA_PROCESO         VARCHAR2(20);
    V_GIIN                  VARCHAR2(50);
  BEGIN

      -- OBTENEMOS EL PERIODO ACTIVO
      SELECT PERIODO, FECHA_COMPILACION INTO V_PERIODO, V_FECHA_PROCESO FROM FATCA.PERIODO WHERE ESTADO = '1';
      SELECT CODIGO INTO V_GIIN FROM FATCA.PARAMETRO WHERE ID = 18;

      V_PREFIJO := V_PREFIJO || '-' || SUBSTR(V_PERIODO, LENGTH(V_PERIODO)-1, LENGTH(V_PERIODO)) || '-' || V_GIIN ;
      V_SUFIJO  := TO_CHAR(SYSDATE,'DDMMYYHH24MISS');

      IF P_PROCESO = P_MESSAGE_SPEC THEN
        V_REFERENCIA := V_PREFIJO || '-' || V_SUFIJO;
      ELSIF P_PROCESO = P_ACCOUNT_REPORT THEN
        V_REFERENCIA := V_PREFIJO || '-' || P_NRO_CUENTA || '-' || P_CODIGO_CLIENTE || '-' || V_SUFIJO;
      ELSIF P_PROCESO = P_POOL_REPORT THEN
        V_REFERENCIA := V_PREFIJO || '-' || P_GIIN_ORGANISATION || '-' || V_SUFIJO;
      ELSIF P_PROCESO = P_REPORTING_FI THEN
        V_REFERENCIA := V_PREFIJO || '-' || P_GIIN_ORGANISATION || '-' || V_SUFIJO;
      ELSIF P_PROCESO = P_INTERMEDIARY THEN
        V_REFERENCIA := V_PREFIJO || '-' || P_TIPO_FONDO || '-' || V_SUFIJO;
      END IF;

      RETURN V_REFERENCIA;
  END SF_OBTENER_DOCREFID;

  FUNCTION SF_OBTENER_CORREFID(P_NRO_CUENTA VARCHAR2
      , P_PROCESO VARCHAR2
      , P_FATCA_OECD_ID NUMBER
      , P_TIN VARCHAR2)
  RETURN VARCHAR2 AS
    V_REFERENCIA            VARCHAR2(250);
  BEGIN
    IF P_PROCESO = P_MESSAGE_SPEC THEN
      -- TABLA FATCA_OECD
      SELECT MAX(MESSAGE_REF_ID) INTO V_REFERENCIA FROM FATCA.FATCA_OECD WHERE ID = P_FATCA_OECD_ID;
    ELSIF P_PROCESO = P_ACCOUNT_REPORT THEN
      -- TABLA ACCOUNT_REPORT
      SELECT MAX(DOC_REF_ID) INTO V_REFERENCIA FROM FATCA.ACCOUNT_REPORT AR
      LEFT JOIN FATCA.REPORTING_GROUP RG ON AR.REPORTING_GROUP_ID = RG.ID
      LEFT JOIN FATCA.FATCA FA ON RG.FATCA_ID = FA.ID
      LEFT JOIN FATCA.FATCA_OECD FO ON FA.FATCA_OECD_ID = FO.ID
      WHERE FO.ID = P_FATCA_OECD_ID AND AR.ACCOUNT_NUMBER = P_NRO_CUENTA;
    ELSIF P_PROCESO = P_POOL_REPORT THEN
      -- TABLA POOL_REPORT
      SELECT MAX(DOC_REF_ID) INTO V_REFERENCIA FROM FATCA.POOL_REPORT PR
      LEFT JOIN FATCA.REPORTING_GROUP RG ON PR.REPORTING_GROUP_ID = RG.ID
      LEFT JOIN FATCA.FATCA FA ON RG.FATCA_ID = FA.ID
      LEFT JOIN FATCA.FATCA_OECD FO ON FA.FATCA_OECD_ID = FO.ID
      WHERE FO.ID = P_FATCA_OECD_ID AND DOC_REF_ID LIKE '%' || P_TIN || '%';
    ELSIF P_PROCESO = P_REPORTING_FI OR P_PROCESO = P_INTERMEDIARY THEN
      -- TABLA ORGANISATION
      SELECT MAX(DOC_REF_ID) INTO V_REFERENCIA FROM FATCA.ORGANISATION OG
      LEFT JOIN FATCA.REPORTING_FI RI ON OG.ID = RI.ORGANISATION_ID
      LEFT JOIN FATCA.FATCA FA ON RI.ID = FA.REPORTING_FI_ID
      LEFT JOIN FATCA.FATCA_OECD FO ON FA.FATCA_OECD_ID = FO.ID
      WHERE FO.ID = P_FATCA_OECD_ID AND OG.TIN = P_TIN;
    END IF;

    RETURN V_REFERENCIA;
  END SF_OBTENER_CORREFID;

END PKG_PROCESOS_SALIDA;
/

spool off
