/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.conf_reader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

/**
 *
 * Class used to load the application's configuration. The configurations must
 * be formatted in as an ini file.
 *
 * The file must be located under the resources folder and named conf.ini
 */
public class ConfReader {

    private HierarchicalINIConfiguration config;

    public ConfReader() {
        try {
            // Load Configuration file
            /*
            //Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            String filePath = classLoader.getResource("conf.ini").toString();
             */
            String filePath = "/home/ptin_admin/ae/conf.ini";
            //String filePath = "/home/mohammad/ae/conf.ini";
            this.config = new HierarchicalINIConfiguration(filePath);
        } catch (ConfigurationException ex) {
            Logger.getLogger(ConfReader.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads a specific section of a conf ini
     *
     * @param name - the section to be loaded
     * @return a Map containing the full loaded section.
     */
    public HashMap<String, String> getSection(String name) {
        SubnodeConfiguration section = this.config.getSection(name);
        Iterator sections = section.getKeys();
        HashMap<String, String> map = new HashMap<>();
        while (sections.hasNext()) {
            String key = sections.next().toString();
            map.put(key, section.getString(key, ""));
        }
        return map;
    }

}
