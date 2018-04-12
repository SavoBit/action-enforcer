/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.db_model.dao;

import eu.selfnet.ae.db_model.model.tal.Action;
import java.util.concurrent.LinkedBlockingQueue;
import org.bson.conversions.Bson;

public class ActionReader extends MongoTaskReader<Action>{
    public ActionReader(String mongoCollectionName, Bson query, Bson update) {
        super(Action.class, new LinkedBlockingQueue<>(), mongoCollectionName,
                query, update);
    }
}