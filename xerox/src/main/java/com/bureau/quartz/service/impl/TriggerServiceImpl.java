package com.bureau.quartz.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bureau.quartz.dao.TriggerDAO;
import com.bureau.quartz.domain.Trigger;
import com.bureau.quartz.service.TriggerService;

/*
 * (non-Javadoc)
 * 
 * @see
 * com.bbva.quartz.service.TriggerService
 */
@Service("triggerService")
public class TriggerServiceImpl implements TriggerService {

    @Resource(name = "triggerDAO")
    private TriggerDAO triggerDAO;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bbva.quartz.service.TriggerService#listar()
     */
    @Transactional(readOnly = true)
    @Override
    public List<Trigger> listar() {
        return triggerDAO.listar();
    }

}
