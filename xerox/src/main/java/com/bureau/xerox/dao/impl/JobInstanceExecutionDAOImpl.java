package com.bureau.xerox.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.bureau.batch.domain.JobInstanceExecution;
import com.bureau.xerox.dao.JobInstanceExecutionDAO;
import com.everis.core.dao.impl.HibernateDAO;

@Repository("jobInstanceExecutionDAO")
public class JobInstanceExecutionDAOImpl extends HibernateDAO<JobInstanceExecution> implements JobInstanceExecutionDAO {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public List<JobInstanceExecution> listar(JobInstanceExecution jobInstanceExecution) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        StringBuilder qc = new StringBuilder();

        sb.append("SELECT");
        sb.append(" A.JOB_INSTANCE_ID jobInstanceId");
        sb.append(", A.JOB_NAME jobName");
        sb.append(", B.JOB_EXECUTION_ID jobExecutionId");
        sb.append(", B.CREATE_TIME createTime");
        sb.append(", B.START_TIME startTime");
        sb.append(", B.END_TIME endTime");
        sb.append(", B.STATUS status");
        sb.append(", B.EXIT_CODE exitCode");
        sb.append(", B.EXIT_MESSAGE exitMessage");
        sb.append(" FROM FATCA.BATCH_JOB_INSTANCE A");
        sb.append(" INNER JOIN FATCA.BATCH_JOB_EXECUTION B ON A.JOB_INSTANCE_ID=B.JOB_INSTANCE_ID");

        if (jobInstanceExecution.getJobName() != null && !jobInstanceExecution.getJobName().isEmpty()) {
            qc.append(" AND UPPER(A.JOB_NAME) = :jobName");
            params.put("jobName", jobInstanceExecution.getJobName().toUpperCase());
        }

        if (jobInstanceExecution.getExitCode() != null && !jobInstanceExecution.getExitCode().isEmpty()) {
            qc.append(" AND UPPER(B.EXIT_CODE) = :exitCode");
            params.put("exitCode", jobInstanceExecution.getExitCode().toUpperCase());
        }

        if (jobInstanceExecution.getCreateTime() != null) {
            qc.append(" AND TRUNC(B.CREATE_TIME) = :createTime");
            params.put("createTime", jobInstanceExecution.getCreateTime());
        }

        if (qc.length() > 0) {
            sb.append(" WHERE ");
            sb.append(qc.toString().substring(5));
        }

        sb.append(" ORDER BY B.JOB_EXECUTION_ID DESC");

        SQLQuery query = getSession().createSQLQuery(sb.toString());
        query.addScalar("jobInstanceId", StandardBasicTypes.LONG).addScalar("jobName", StandardBasicTypes.STRING).addScalar("jobExecutionId", StandardBasicTypes.LONG)
                .addScalar("createTime", StandardBasicTypes.TIMESTAMP).addScalar("startTime", StandardBasicTypes.TIMESTAMP).addScalar("endTime", StandardBasicTypes.TIMESTAMP)
                .addScalar("status", StandardBasicTypes.STRING).addScalar("exitCode", StandardBasicTypes.STRING).addScalar("exitMessage", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(JobInstanceExecution.class));

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return (List<JobInstanceExecution>) query.list();
    }

}
