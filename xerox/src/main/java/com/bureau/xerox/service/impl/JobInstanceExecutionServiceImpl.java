package com.bureau.xerox.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bureau.batch.domain.JobInstanceExecution;
import com.bureau.xerox.dao.JobInstanceExecutionDAO;
import com.bureau.xerox.service.JobInstanceExecutionService;
import com.everis.core.service.impl.DataManipulationService;

@Transactional
@Service("jobInstanceExecutionService")
public class JobInstanceExecutionServiceImpl extends DataManipulationService<JobInstanceExecution, JobInstanceExecutionDAO> implements JobInstanceExecutionService {

    private static final long serialVersionUID = 1L;

    @Autowired
    @Qualifier("jobInstanceExecutionDAO")
    public void setHibernateDAO(JobInstanceExecutionDAO hibernateDAO) {
        super.setHibernateDAO(hibernateDAO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<JobInstanceExecution> listar(JobInstanceExecution jobInstanceExecution) {
        return getHibernateDAO().listar(jobInstanceExecution);
    }

}
