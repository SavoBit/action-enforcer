/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.dao;

import eu.selfnet.ae.db_model.model.tal.Action;
import java.util.List;

public class ActionDAO extends AbstractDAO<Action> {

    public ActionDAO() {
        super(Action.class);
    }

    public Action findByTactic(String requestID, Integer order) {
        final String query = "SELECT t FROM "
                + entityClass.getSimpleName()
                + " t WHERE t.tactic.requestId=:val1 and t.actionOrder=:val2";

        return getEntityManager()
                .createQuery(query, entityClass)
                .setParameter("val1", requestID)
                .setParameter("val2", order)
                .getSingleResult();
    }

    public List<Action> findByTactic(String requestID) {
        final String query = "SELECT t FROM "
                + entityClass.getSimpleName()
                + " t WHERE t.tactic.requestId=:val1";

        return getEntityManager()
                .createQuery(query, entityClass)
                .setParameter("val1", requestID)
                .getResultList();
    }
    
}
