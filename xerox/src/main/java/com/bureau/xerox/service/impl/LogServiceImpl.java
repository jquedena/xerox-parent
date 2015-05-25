package com.bureau.xerox.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bureau.xerox.dao.LogDAO;
import com.bureau.xerox.domain.Log;
import com.bureau.xerox.service.LogService;
import com.everis.core.service.impl.DataManipulationService;

@Transactional
@Service("logService")
public class LogServiceImpl extends DataManipulationService<Log, LogDAO> implements LogService {

    private static final long serialVersionUID = 1L;

    @Autowired
    @Qualifier("logDAO")
    public void setHibernateDAO(LogDAO hibernateDAO) {
        super.setHibernateDAO(hibernateDAO);
    }

    @Transactional(readOnly = true)
    @Override
    public ByteArrayOutputStream generarLogFile(Long idProceso) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        List<Log> registros = getHibernateDAO().listarLogPorProceso(idProceso);

        for (Log log : registros) {
            String line = log.getMensaje() + "\n";
            byte buf[] = line.getBytes("UTF8");
            bytes.write(buf);
        }

        bytes.close();

        return bytes;
    }
}
