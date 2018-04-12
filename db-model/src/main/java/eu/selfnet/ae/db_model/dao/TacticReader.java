/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.dao;

import eu.selfnet.ae.db_model.model.tal.Tactic;
import java.util.concurrent.LinkedBlockingQueue;
import org.bson.conversions.Bson;

public class TacticReader extends MongoTaskReader<Tactic> {

    public TacticReader(Bson query, Bson update) {
        super(Tactic.class, new LinkedBlockingQueue<>(), "tactic",
                query, update);
    }
}
