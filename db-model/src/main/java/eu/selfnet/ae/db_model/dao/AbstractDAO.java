/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.dao;

import com.google.gson.Gson;
import eu.selfnet.ae.conf_reader.ConfReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * Abstract DAO to manage DB access.
 *
 * This class uses JPA so it can be used with all types of DBs supported by
 * hibernate.
 *
 * @param <T> Class type to be used with the DAO
 */
public abstract class AbstractDAO<T> {

    protected Class<T> entityClass;

    private final EntityManagerFactory factory;

    private final EntityManager em;

    private static final Gson GSON = new Gson();

    public AbstractDAO(Class<T> entityClass) {

        Map accessProperties = new HashMap();
        ConfReader config = new ConfReader();
        Map<String, String> section = config.getSection("Database");

        section.keySet().stream().forEach((String key) -> {
            String hibernateKey = new StringBuilder("hibernate.ogm.datastore.")
                    .append(key).toString();
            accessProperties.put(hibernateKey, section.get(key));
        });

        factory = Persistence.createEntityManagerFactory(
                "ae", accessProperties
        );
        this.entityClass = entityClass;
        this.em = this.getEntityManager();
    }

    public EntityManager getEntityManager() {
        if (this.em != null && this.em.isOpen()) {
            return this.em;
        }
        return factory.createEntityManager();
    }

    public void persist(T obj) {
        //EntityManager em = getEntityManager();
        em.clear();
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
        em.clear();
    }

    public void update(T obj) {
        //EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
        em.clear();
    }

    public void remove(T obj) {
        //EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(obj);
        em.getTransaction().commit();
        em.clear();
    }

    public void remove(List<T> objs) {
        //EntityManager em = getEntityManager();
        em.getTransaction().begin();
        objs.stream().forEach((obj) -> {
            em.remove(obj);
        });
        em.getTransaction().commit();
        em.clear();
    }

    public T find(Object id) {
        //EntityManager em = getEntityManager();
        T obj = em.find(entityClass, id);
        return obj;
    }

}
