package eu.selfnet.ae.db_model.dao;


import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import eu.selfnet.ae.conf_reader.ConfReader;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * Class to atomically access Mongo document, using the find and modify
 *
 * @see
 * https://docs.mongodb.com/manual/reference/method/db.collection.findAndModify/
 *
 * This class uses a runnable interface to constantly query the MongoDB for new
 * objects. The objects are then collected in a poll.
 *
 * @param <T> Class type to be used with the Reader
 */
public abstract class MongoTaskReader<T> implements Runnable, Serializable {

    protected Class<T> entityClass;
    private LinkedBlockingQueue<T> queue;
    private String mongoCollectionName;
    private MongoDatabase mongoDB;
    private final Bson query;
    private final Bson update;
    private final AtomicBoolean open = new AtomicBoolean(false);
    private final Gson gson;

    /**
     * Creates a new Task Reader Instance
     *
     * @param entityClass - The class type to use
     * @param queue - Queue to hold the retrieved objects
     * @param mongoCollectionName - Collection name to search for
     * @param query - The query that triggers new results
     * @param update - The new value for the modify
     */
    public MongoTaskReader(Class<T> entityClass, LinkedBlockingQueue<T> queue,
            String mongoCollectionName, Bson query, Bson update) {
        this.entityClass = entityClass;
        this.queue = queue;
        this.mongoCollectionName = mongoCollectionName;
        this.query = query;
        this.update = update;
        this.connect(new ConfReader());
        this.gson = new Gson();
    }

    /**
     * Establishes a new MongoDB connection
     *
     * @param confReader - Object containing the configurations
     */
    public final void connect(ConfReader confReader) {
        Map<String, String> config = confReader.getSection("Database");
        // TODO New ways of reading authentication or other parameters
        MongoClient mongo = new MongoClient(config.get("host"),
                Integer.valueOf(config.get("port")));
        this.mongoDB = mongo.getDatabase(config.get("database"));

    }

    @Override
    public void run() {
        while (this.open.get()) {
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
            // Load the document with updated field
            options.returnDocument(ReturnDocument.AFTER);
            Document doc = mongoDB.getCollection(mongoCollectionName)
                    .findOneAndUpdate(query, update, options);
            try {
                handleReading(doc);
            } catch (InterruptedException ex) {
                Logger.getLogger(MongoTaskReader.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Put a document in a queue
     *
     * @param doc - Document to be stored
     * @throws InterruptedException
     */
    private void handleReading(Document doc) throws InterruptedException {
        if (doc == null) {
            Thread.sleep(250);
            return;
        }

        this.queue.put(this.gson.fromJson(doc.toJson(), this.entityClass));
    }

    /**
     * Method to get the next object in the poll
     *
     * @return Next object in the poll
     */
    public T next() {
        T obj = this.queue.poll();
        if (obj == null) {
            return null;
        }
        return obj;
    }

    public LinkedBlockingQueue<T> getQueue() {
        return queue;
    }

    public void setQueue(LinkedBlockingQueue<T> queue) {
        this.queue = queue;
    }

    public String getMongoCollectionName() {
        return mongoCollectionName;
    }

    public void setMongoCollectionName(String mongoCollectionName) {
        this.mongoCollectionName = mongoCollectionName;
    }

    public void startRunning() {
        this.changeOpen(true);
    }

    public void stopRunning() {
        this.changeOpen(false);
    }

    private void changeOpen(boolean b) {
        this.open.set(b);
    }
}
