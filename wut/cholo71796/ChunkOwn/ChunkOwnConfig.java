/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ChunkOwn;

import java.io.File;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author Cole Erickson
 */
public class ChunkOwnConfig {
    
    private static final Configuration config;
    private static final File configfile;
    
    // CONFIGURATION VARIABLES //
     //gen
    private static boolean verbose;
    private static long timeBetweenConquestDistribution;
    private static int amountOfConquestPerConquestDistribution;
    private static int conquestCap;    
    private static String unownedChunkName;
    private static String defaultChunkName; 
     //cost
    private static int claimChunk;
    
    static {
        configfile = new File(ChunkOwn.dataFolder, "config.yml");
        
        if (!configfile.exists()){
            ChunkOwn.dataFolder.mkdirs();
            config = new Configuration(configfile);
            
            config.setProperty("verbose", false);
            config.setProperty("minutesBetweenConquestDistribution", 60);
            config.setProperty("amountOfConquestPerConquestDistribution", 3);
            config.setProperty("conquestCap", 3);
            config.setProperty("unownedChunkName", "Unowned");
            config.setProperty("defaultChunkName", "Freshly Claimed");
            
            config.setProperty("costs.claimChunk", 3);
        } else
            config = new Configuration(configfile);
        
        load();
    }
    
    public static void load() {
        config.load();
        
        verbose = config.getBoolean("verbose", false);
        timeBetweenConquestDistribution = new Long(config.getInt("minutesBetweenConquestDistribution", 30) * 60 * 1000);
        amountOfConquestPerConquestDistribution = config.getInt("amountOfConquestPerConquestDistribution", 3);
        conquestCap = config.getInt("conquestCap", 3);
        unownedChunkName = config.getString("unownedChunkName", "Unowned");
        defaultChunkName = config.getString("defaultChunkName", "Freshly Claimed");        
        
        claimChunk = config.getInt("costs.claimChunk", 3);
        
        config.save();
    }

    public static String getDefaultChunkName() {
        return defaultChunkName;
    }

    public static Long getTimeBetweenConquestDistribution() {
        return timeBetweenConquestDistribution;
    }
            
    public static int getAmountOfConquestPerConquestDistribution() {
        return amountOfConquestPerConquestDistribution;
    }
    
    public static int getConquestCap() {
        return conquestCap;
    }
    
    public static String getUnownedChunkName() {
        return unownedChunkName;
    }
    
    public static int getCostClaimChunk() {
        return claimChunk;
    }
    
    public static boolean isVerbose() {
        return verbose;
    }
}