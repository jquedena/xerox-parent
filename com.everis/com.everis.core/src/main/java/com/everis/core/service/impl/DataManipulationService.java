package com.everis.core.service.impl;

import java.util.List;

import com.everis.core.dao.IHibernateDAO;
import com.everis.core.service.IDataManipulationService;

public abstract class DataManipulationService<T, D extends IHibernateDAO<T>> implements IDataManipulationService<T> {

    private static final long serialVersionUID = 1L;
    private D hibernateDAO;

    public void setHibernateDAO(D hibernateDAO) {
        this.hibernateDAO = hibernateDAO;
    }

    public D getHibernateDAO() {
        return hibernateDAO;
    }

    @Override
    public T insertar(T o) {
        hibernateDAO.save(o);
        return o;
    }

    @Override
    public T actualizar(T o) {
        hibernateDAO.saveOrUpdate(o);
        return o;
    }

    @Override
    public List<T> actualizar(List<T> o) {
        hibernateDAO.saveOrUpdateAll(o);
        return o;
    }

    @Override
    public void eliminar(T o) {
        hibernateDAO.delete(o);
    }

    @Override
    public void eliminar(List<T> o) {
        hibernateDAO.deleteAll(o);
    }

}
