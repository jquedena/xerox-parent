package com.bureau.quartz.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.bureau.quartz.dao.TriggerDAO;
import com.bureau.quartz.domain.Trigger;
import com.everis.core.dao.impl.HibernateDAO;

/**
 * Clase para acceder a los datos de los trabajos configurados mediante el uso de la vista QRTZ_VW_TRIGGERS
 * @author jquedena
 *
 */
@Repository("triggerDAO")
public class TriggerDAOImpl extends HibernateDAO<Trigger> implements Serializable, TriggerDAO {

    private static final long serialVersionUID = 1L;

    /**
	 * Lista los trabajos configurados mediante el uso de la vista QRTZ_VW_TRIGGERS
	 * @return La lista de trabajos configurados
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Trigger> listar() {
        Criteria criteria = getCriteria(Trigger.class);
        return (List<Trigger>) criteria.list();
    }
}
