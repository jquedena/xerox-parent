package com.everis.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jndi.JndiObjectFactoryBean;

import com.everis.core.exception.BussinesException;

public class DBUtilSpring {

    private static final Logger LOG = Logger.getLogger(DBUtilSpring.class);
    private static boolean envDEV = false;
    private static DBUtilSpring instance = new DBUtilSpring();
    private Map<String, DataSource> dataSources;

    public DBUtilSpring() {
        super();
        dataSources = new HashMap<String, DataSource>();
    }

    public static boolean isEnvDEV() {
        return envDEV;
    }

    public static void setEnvDEV(boolean envDEV) {
        DBUtilSpring.envDEV = envDEV;
    }

    public static DBUtilSpring getInstance() {
        return instance;
    }

    public void setDataSource(String jndiName, DataSource dataSource) {
        dataSources.put(jndiName, dataSource);
    }

    public DataSource getDataSource(String jndiName) {
        DataSource dataSource = null;

        if (!dataSources.containsKey(jndiName)) {
            JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
            jndiFactory.setJndiName(jndiName);
            jndiFactory.setResourceRef(true);
            jndiFactory.setLookupOnStartup(false);
            jndiFactory.setCache(true);
            jndiFactory.setProxyInterface(DataSource.class);
            try {
                jndiFactory.afterPropertiesSet();
            } catch (IllegalArgumentException e) {
                LOG.error("No se pudo crear el objeto DataSource", e);
            } catch (NamingException e) {
                LOG.error("No se encuentra registrado el nombre del jndi [" + jndiName + "]", e);
            }
            dataSource = (DataSource) jndiFactory.getObject();
            dataSources.put(jndiName, dataSource);
        } else {
            dataSource = dataSources.get(jndiName);
        }

        try {
            LOG.error(dataSource.toString());
        } catch (Exception e) {
            LOG.error("No se pudo crear el datasource desde Spring", e);
            dataSource = DBUtil.getInstance().getDataSource(jndiName);
            dataSources.put(jndiName, dataSource);
        }
        
        return dataSource;
    }
    
    public List<Map<String, Object>> executeQuery(String jndi, String query) throws BussinesException {
        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
        Map<String, Object> row;
        int j;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        String[] colNames;
        
        try {
            connection = getDataSource(jndi).getConnection();
            statement = connection.createStatement();
            statement.setFetchSize(100);
            resultset = statement.executeQuery(query);
            metadata = resultset.getMetaData();
            
            colNames = new String[metadata.getColumnCount()];
            for(j = 1; j <= colNames.length; j++) {
                colNames[j - 1] = metadata.getColumnName(j);
            }
            
            while(resultset.next()) {
                row = new HashMap<String, Object>();
                for(j = 0; j < colNames.length; j++) {
                    row.put(colNames[j], resultset.getObject(colNames[j]));
                }
                result.add(row);
            }
        } catch(SQLException e) {
            throw new BussinesException("No se pudo ejecutar la consulta: [" + query + "]", e);
        } finally {
            if(resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE RESULTSET", e);
                }
            }

            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE STATEMENT", e);
                }
            }

            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE CONNECTION", e);
                }
            }
        }
        
        return result;
    }
    
    public String executeQueryUniqueResult(String jndi, String query) throws BussinesException {
        String result = null;
        int j = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        String[] colNames;
        
        try {
            connection = getDataSource(jndi).getConnection();
            statement = connection.createStatement();
            statement.setFetchSize(100);
            resultset = statement.executeQuery(query);
            metadata = resultset.getMetaData();
            
            colNames = new String[metadata.getColumnCount()];
            if(colNames.length > 1) {
                throw new ArrayIndexOutOfBoundsException("El numero de campos de resultados supera lo esperado");
            }
            for(j = 1; j <= colNames.length; j++) {
                colNames[j - 1] = metadata.getColumnName(j);
            }
            
            if(resultset.next()) {
                result = resultset.getString(colNames[0]);
            }
        } catch(SQLException e) {
            throw new BussinesException("No se pudo ejecutar la consulta: [" + query + "]", e);
        } catch(Exception e) {
            throw new BussinesException("Error al procesar la consulta: [" + query + "]", e);
        } finally {
            if(resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE RESULTSET", e);
                }
            }

            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE STATEMENT", e);
                }
            }

            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE CONNECTION", e);
                }
            }
        }
        
        return result;
    }
    
    public boolean execute(String jndi, String query) throws BussinesException {
        boolean result = true;
        Connection connection = null;
        Statement statement = null;
        
        try {
            connection = getDataSource(jndi).getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute(query);
            
            try {
                connection.commit();
            } catch (SQLException e) {
                LOG.error("NOT Commit : [" + query + "]");
                connection.rollback();
                result = false;
            }

            result = true;
        } catch (SQLException e) {
            throw new BussinesException("No se pudo ejecutar la consulta: [" + query + "]", e);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE STATEMENT", e);
                }
            }

            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE CONNECTION", e);
                }
            }
        }
        
        return result;
    }

    public Map<String, Object> executeProcedure(String jndi, String procedure, Map<String, Object> paramsIN, Map<String, Integer> paramsOUT) throws BussinesException {
        Map<String, Object> result = new HashMap<String, Object>();
        Connection connection = null;
        CallableStatement statement = null;
        int i;
        String params = "";

        try {
        	i = paramsIN.size() + paramsOUT.size();
        	params = StringUtils.repeat("?,", i);
        	
        	if(params.length() > 0) {
        		params = params.substring(0, params.length() - 1);
        	}
        	
        	procedure = "{call " + procedure + "(" + params + ")}";
        	
            connection = getDataSource(jndi).getConnection();
            statement = connection.prepareCall(procedure);

            for(Map.Entry<String, Object> in : paramsIN.entrySet()) {
                statement.setObject(in.getKey(), in.getValue());
            }
            
            for(Map.Entry<String, Integer> out : paramsOUT.entrySet()) {
                statement.registerOutParameter(out.getKey(), out.getValue());
            }

            statement.execute();

            i = 0;
            for(Map.Entry<String, Integer> out : paramsOUT.entrySet()) {
                if(Types.NUMERIC == out.getValue().intValue()) {
                    result.put(out.getKey(), statement.getBigDecimal(out.getKey()));
                } else if(Types.VARCHAR == out.getValue().intValue()) {
                    result.put(out.getKey(), statement.getString(out.getKey()));
                } else {
                    result.put(out.getKey(), statement.getObject(out.getKey()));
                }
                
                i++;
            }

        } catch (SQLException e) {
            throw new BussinesException("No se pudo ejecutar la consulta: [" + procedure + "]", e);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE STATEMENT", e);
                }
            }

            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error("Lectura de base de datos:CLOSE CONNECTION", e);
                }
            }
        }

        return result;
    }
}
