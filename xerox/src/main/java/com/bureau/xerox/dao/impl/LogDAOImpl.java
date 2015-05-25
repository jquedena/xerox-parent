package com.bureau.xerox.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bureau.xerox.dao.LogDAO;
import com.bureau.xerox.domain.Log;
import com.everis.core.dao.impl.HibernateDAO;

@Repository("logDAO")
public class LogDAOImpl extends HibernateDAO<Log> implements LogDAO {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public List<Log> listarLogPorProceso(Long idProceso) throws Exception {
        List<Log> registros = new ArrayList<Log>();

        Criteria criterioLog = super.getCriteria(Log.class);
        criterioLog.setFetchMode("cuentas", FetchMode.JOIN);
        criterioLog.setFetchMode("accionistas", FetchMode.JOIN);
        if (idProceso != null) {
            criterioLog.add(Restrictions.eq("idProceso", idProceso));
        }
        criterioLog.addOrder(Order.asc("id"));
        registros = (List<Log>) criterioLog.list();
        return registros;

    }

}
