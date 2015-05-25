package com.bureau.xerox.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.springframework.batch.core.JobInstance;
import org.springframework.stereotype.Repository;

import com.bureau.xerox.dao.JobInstanceDAO;
import com.everis.core.dao.impl.HibernateDAO;

@Repository("jobInstanceDAO")
public class JobInstanceDAOImpl extends HibernateDAO<JobInstance> implements JobInstanceDAO {

    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings("unchecked")
    @Override
    public Long obtenerUltimaInstancia(String name) {
        SQLQuery query = getSession().createSQLQuery("SELECT MAX(JOB_INSTANCE_ID) JOB_INSTANCE_ID FROM FATCA.BATCH_JOB_INSTANCE WHERE JOB_NAME = :name");
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        query.setString("name", name);
        Map<String, Object> o = (HashMap<String, Object>) query.uniqueResult();
        BigDecimal jobInstanceID = o.get("JOB_INSTANCE_ID") == null ? null : (BigDecimal) o.get("JOB_INSTANCE_ID");
        Long lJobInstanceID = null;
        if (jobInstanceID != null) {
            lJobInstanceID = jobInstanceID.longValue();
        }

        return lJobInstanceID;
    }
}
