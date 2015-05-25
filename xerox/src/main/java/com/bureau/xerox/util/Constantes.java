package com.bureau.xerox.util;

public class Constantes {

    public static final String SELECT_EMPTY = "-1";
    public static final String MESSAGE_TYPE = "FATCA";
    public static final String BOOLEANO_YES = "S";
    public static final String BOOLEANO_NO = "N";
    public static final String TIPO_INDICADOR_TEST = "1";
    public static final String TIPO_INDICADOR_PRODUCCION = "11";
    public static final String CODIGO_POOL_REPORT = "R";
    public static final String CLASIFICACION_PERSONA = "USP";
    public static final String RES_COUNTRY_CODE_PERSONA_NATURAL = "US";
    public static final String SI = "SI";
    public static final String NO = "NO";
    public static final String PARAMETRO_FONDOS = "Fondos";
    public static final String COUNTRY_CODE_DEFAULT = "PE";
    public static final String TIPO_DOCUMENTO_IND = "FATCA2";
    public static final String TIPO_DOCUMENTO_DNI = "DNI";

    public static class Parametro {
        public static final Long ID_PARAMETRO_PADRE_TIPO_MENSAJE_FATCA = 12L;
        public static final Long ID_PARAMETRO_PADRE_TIPO_ENTIDADES = 17L;
        public static final Long ID_PARAMETRO_PADRE_NOMBRE_FONDOS = 27L;
        public static final Long LISTA_ARCHIVOS = 24L;
        public static final Long TAREAS_PROGRAMADAS = 496L;
        public static final Long PAISES = 257L;
        public static final Long TIPO_PERSONA = 247L;
        public static final Long TIPO_PAYMENT = 235L;
        public static final Long POOL_REPORT = 240L;
    }
    
    public static class TipoPersona {
    	public static final String JURIDICA = "J";
    	public static final String NATURAL = "N";
    }
    
    public static class TipoPayment {
    	public static final String DIVIDENDOS = "Dividends";
    	public static final String INTERESES = "Interest";
    	public static final String GROSS_PROCCEDS = "Gross Proceeds/Redemptions";
    	public static final String OTHERS = "Other - FATCA";
    }
    
    public static class EstadoProcesoProcedure {
        public static final String COMPLETED = "COMPLETED";
        public static final String PARTITIAL = "PARTITIAL";
        public static final String ERROR = "ERROR";
        public static final String NOT_FOUND = "NOT_FOUND";
    }
    
    public static class EstadoProceso {
        public static final String INFORMADO = "1";            // Informado
        public static final String MODIFICADO = "2";           // Modificado
        public static final String CON_ERROR = "3";            // Con Error
        public static final String CORRECTO = "4";             // OK
        public static final String INFORMADO_CONFLICTO = "5";  // Informado con Conflicto
        public static final String MODIFICADO_CONFLICTO = "6"; // Modificado con Conflicto
        public static final String INFORMADO_MODIFICADO = "7"; // Informado Modificado
        public static final String INFORMADO_MODIFICADO_CONFLICTO = "8"; // Informado Modificado con Conflicto
    }
    
    public static class TipoOperacion {
        public static final String FIRMAR = "firmar";
        public static final String VERIFICAR = "verificar";
        public static final String CIFRAR = "cifrar";
        public static final String DESCIFRAR = "descifrar";
        public static final String COMPRIMIR = "comprimir";
        public static final String DESCOMPRIMIR = "descomprimir";
        public static final String DESCOMPRIMIR_FINAL = "descomprimirFinal";
    }
}
