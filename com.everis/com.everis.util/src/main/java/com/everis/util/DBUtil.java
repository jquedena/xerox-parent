package com.everis.util;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DBUtil {

    private static final Logger LOG = Logger.getLogger(DBUtil.class);
    private static DBUtil instance = new DBUtil();
    private Map<String, DataSource> dataSources;

    public DBUtil() {
        super();
        dataSources = new HashMap<String, DataSource>();
    }

    public static DBUtil getInstance() {
        return instance;
    }

    public DataSource getDataSource(String jndiName) {
        DataSource dataSource = null;

        if (!dataSources.containsKey(jndiName)) {
            InitialContext context = null;

            try {
                context = new InitialContext();
                dataSource = (DataSource) context.lookup(jndiName);
            } catch (NamingException e) {
                try {
                    context = new InitialContext();
                    Context initCtx  = (Context) context.lookup("java:/comp/env");
                    dataSource = (DataSource) initCtx.lookup(jndiName);
                } catch (NamingException e1) {
                    LOG.error("Error:", e1);
                }
            }

            dataSources.put(jndiName, dataSource);
        } else {
            dataSource = dataSources.get(jndiName);
        }

        return dataSource;
    }
}
