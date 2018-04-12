/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.dao;

import eu.selfnet.ae.db_model.model.tal.Tactic;

public class TacticDAO extends AbstractDAO<Tactic> {

    public TacticDAO() {
        super(Tactic.class);
    }

    public Tactic findByRequestID(String requestID) {
        final String query = "SELECT t FROM "
                + entityClass.getSimpleName()
                + " t WHERE t.requestId=:val";

        return getEntityManager()
                .createQuery(query, entityClass)
                .setParameter("val", requestID)
                .getResultList().get(0);
    }

}
