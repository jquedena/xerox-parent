package com.everis.core.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface IHibernateDAO<T> extends Serializable {

    /**
     * @param sessionFactory
     */
    void setSessionFactoryHibernate(SessionFactory sessionFactory);

    /**
     * Inserta o actualiza una lista de objetos
     *
     * @param list
     */
    void saveOrUpdateAll(List<T> list);

    /**
     * Inserta o actualiza un objeto
     *
     * @param t
     */
    void saveOrUpdate(T t);

    /**
     * Inserta un objeto
     *
     * @param t
     */
    void save(T t);

    /**
     * Actualiza un objeto
     *
     * @param t
     */
    void update(T t);

    /**
     * Actualiza un objeto
     *
     * @param t
     */
    void merge(T t);
    
    /**
     * Elimina un objeto
     *
     * @param t
     */
    void delete(T t);

    /**
     * Elimina una coleccion de objetos
     *
     * @param t
     */
    void deleteAll(List<T> t);

    /**
     * Retorna un objeto <b>Criteria</b>
     *
     * @param clazz
     * @return Criteria
     */
    Criteria getCriteria(Class<?> clazz);

    /**
     * Retorna un objeto <b>Criteria</b>
     *
     * @param clazz
     * @param alias
     * @return Criteria
     */
    Criteria getCriteria(Class<?> clazz, String alias);

    /**
     * Retorna la sesion actual
     *
     * @return Session
     */
    Session getCurrentSession();
}