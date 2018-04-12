/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.selfnet.ae.storm.dto;

import java.util.List;

public class AnalysisRule {
    private List<Plugin> plugin;

    public AnalysisRule() {
    }

    public List<Plugin> getPlugin() {
        return plugin;
    }

    public void setPlugin(List<Plugin> plugin) {
        this.plugin = plugin;
    }
    
    
}
