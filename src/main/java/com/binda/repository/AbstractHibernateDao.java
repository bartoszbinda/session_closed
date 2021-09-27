package com.binda.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractHibernateDao<T extends Serializable> {
    private Class<T> clazz;

    @Autowired
    private SessionFactory sessionFactory;

    public void setClazz(Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public T findOne(long id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    public List findAll() {
        return getCurrentSession()
                .createQuery("from " + clazz.getName()).list();
    }

    @Transactional
    public T saveOrUpdate(T entity) {
        getCurrentSession().saveOrUpdate(clazz.getName(), entity);
        return entity;
    }


    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    public void deleteById(long id) {
        final T entity = findOne(id);
        delete(entity);
    }

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}