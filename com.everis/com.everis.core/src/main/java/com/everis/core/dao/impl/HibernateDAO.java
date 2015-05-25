package com.everis.core.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.everis.core.dao.IHibernateDAO;

public class HibernateDAO<T> extends HibernateDaoSupport implements IHibernateDAO<T> {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.everis.core.dao.impl.IHibernateDAO#setSessionFactoryHibernate(org
     * .hibernate.SessionFactory)
     */
    @Override
    @Autowired
    public void setSessionFactoryHibernate(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.everis.core.dao.impl.IHibernateDAO#saveOrUpdateAll(java.util.List)
     */
    @Override
    public void saveOrUpdateAll(List<T> list) {
        super.getHibernateTemplate().saveOrUpdateAll(list);
        super.getHibernateTemplate().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.everis.core.dao.impl.IHibernateDAO#saveOrUpdate(T)
     */
    @Override
    public void saveOrUpdate(T t) {
        super.getHibernateTemplate().saveOrUpdate(t);
        super.getHibernateTemplate().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.everis.core.dao.impl.IHibernateDAO#save(T)
     */
    @Override
    public void save(T t) {
        super.getHibernateTemplate().save(t);
        super.getHibernateTemplate().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.everis.core.dao.impl.IHibernateDAO#update(T)
     */
    @Override
    public void update(T t) {
        super.getHibernateTemplate().update(t);
        super.getHibernateTemplate().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.everis.core.dao.impl.IHibernateDAO#merge(T)
     */
    @Override
    public void merge(T t) {
        super.getHibernateTemplate().merge(t);
        super.getHibernateTemplate().flush();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.everis.core.dao.impl.IHibernateDAO#delete(T)
     */
    @Override
    public void delete(T t) {
        super.getHibernateTemplate().delete(t);
        super.getHibernateTemplate().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.everis.core.dao.impl.IHibernateDAO#deleteAll(List<T>)
     */
    @Override
    public void deleteAll(List<T> t) {
        super.getHibernateTemplate().deleteAll(t);
        super.getHibernateTemplate().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.everis.core.dao.impl.IHibernateDAO#getCriteria(java.lang.Class)
     */
    @Override
    public Criteria getCriteria(Class<?> clazz) {
        Criteria criteria = super.getSession().createCriteria(clazz);
        return criteria;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.everis.core.dao.impl.IHibernateDAO#getCriteria(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public Criteria getCriteria(Class<?> clazz, String alias) {
        Criteria criteria = super.getSession().createCriteria(clazz, alias);
        return criteria;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.everis.core.dao.impl.IHibernateDAO#getCurrentSession()
     */
    @Override
    public Session getCurrentSession() {
        return super.getSession();
    }

}
