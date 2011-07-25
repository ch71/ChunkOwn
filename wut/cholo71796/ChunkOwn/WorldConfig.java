/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ChunkOwn;

import java.io.File;
import org.bukkit.Chunk;
import org.bukkit.util.config.Configuration;
import wut.cholo71796.ChunkOwn.variables.ChunkDetails;

/**
 *
 * @author Cole Erickson
 */
public class WorldConfig {
    
    private Configuration config;
    private Chunk chunk;
    private ChunkDetails details;
    
    public WorldConfig(Chunk chunk, ChunkDetails details) {
        this.chunk = chunk;
        this.details = details;
    }
    
    public void write() {        
        String chunkPrefix = chunk.getX() + "," + chunk.getZ();
        
        config = new Configuration(new File(ChunkOwn.dataFolder, chunk.getWorld().getName() + ".yml"));
        config.setProperty(chunkPrefix + ".name", details.getNameExact());
        config.setProperty(chunkPrefix + ".owner", details.getOwner());
        config.setProperty(chunkPrefix + ".conquestBacking", details.getConquestBacking());
        config.save();
        config.load();
        details.setName(config.getString(chunkPrefix + ".name", ""));
        details.setOwner(config.getString(chunkPrefix + ".owner", ""));
        details.setConquestBacking(config.getInt(chunkPrefix + ".conquestBacking", 1));
        config.save();
    }
}
