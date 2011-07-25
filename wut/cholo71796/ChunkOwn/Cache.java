package wut.cholo71796.ChunkOwn;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Chunk;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.util.config.Configuration;

import wut.cholo71796.ChunkOwn.utilities.*;
import wut.cholo71796.ChunkOwn.variables.ChunkDetails;

public class Cache { //not actually a real cache, just holding the data
    
    public static Map<Chunk, ChunkDetails> cache = new HashMap<Chunk, ChunkDetails>();
    
    public Cache(final Server server) {
        loadCache();
        server.getScheduler().scheduleAsyncRepeatingTask(ChunkOwn.plugin, new Runnable() {
            @Override
            public void run() {
                if (!Cache.cache.isEmpty()){
                    storeCache();
                }
            }
            
        }, 500L, 500L);
    }
    
    public static void storeCache() {
        Log.debug("Writing cache to hard drive...");
        for (Chunk key : cache.keySet()) {
            new WorldConfig(key, Cache.cache.get(key)).write();
        }
        Log.debug("Cache write complete.");
    }
    
    public static void loadCache() {
        List<File> validWorldEntries = new ArrayList<File>();
        
        for (File prospectConfig : ChunkOwn.dataFolder.listFiles()) {
            for (World world : ChunkOwn.plugin.getServer().getWorlds()) {
                if (prospectConfig.getName().replaceAll(".yml", "").equals(world.getName())) {
                    validWorldEntries.add(prospectConfig);
                    Log.debug("added " + prospectConfig.getPath());
                }
            }
        }
        
        for (File file : validWorldEntries){
            Configuration fileConfig = new Configuration(file);
            Log.debug("keys: " + fileConfig.getKeys());
            fileConfig.load();
            for (String key : fileConfig.getKeys()) {
                Log.debug("Found key: " + key);
                String[] coords = key.split(",");
                int xCoord = Integer.parseInt(coords[0]);
                int zCoord = Integer.parseInt(coords[1]);
                Chunk chunk = ChunkOwn.plugin.getServer().getWorld(file.getName().replaceAll(".yml", "")).getChunkAt(xCoord, zCoord);
                Log.debug("Discovered chunk " + chunk.getX() + "," + chunk.getZ());
                
                ChunkDetails details = new ChunkDetails();                
                details.setName(fileConfig.getString(key + ".name", ""));
                details.setOwner(fileConfig.getString(key + ".owner", ""));
                details.setConquestBacking(fileConfig.getInt(key + ".conquestBacking", 1));                
                Cache.cache.put(chunk, details);
            }
        }
    }
}