SET SERVEROUTPUT ON;
DECLARE
    user_name varchar2(100):= 'FATCA';
BEGIN
  FOR cur_rec IN (SELECT object_name, object_type 
                  FROM  all_objects /* user_objects */
                  WHERE object_type IN ('TABLE', 'VIEW', 'PACKAGE', 'PROCEDURE', 'FUNCTION', 'SEQUENCE', 'TYPE', 'TRIGGER') AND owner = user_name) LOOP
    BEGIN
      IF cur_rec.object_type = 'TABLE' THEN
        EXECUTE IMMEDIATE 'DROP ' || cur_rec.object_type || ' ' || user_name || '."' || cur_rec.object_name || '" CASCADE CONSTRAINTS PURGE';
      ELSE
        EXECUTE IMMEDIATE 'DROP ' || cur_rec.object_type || ' ' || user_name || '."' || cur_rec.object_name || '"';
      END IF;
      -- DBMS_OUTPUT.put_line('ELIMINANDO: ' || cur_rec.object_type || ' "' || cur_rec.object_name || '"');
    EXCEPTION
      WHEN OTHERS THEN
        DBMS_OUTPUT.put_line('FAILED: DROP ' || cur_rec.object_type || ' "' || cur_rec.object_name || '"');
    END;
  END LOOP;
END;
/
