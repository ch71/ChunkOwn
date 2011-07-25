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
            Log.debug("found " + key.getX() + ", " + key.getZ());
            cache.get(key).write(key.getWorld().getName(), key.getX() + ", " + key.getZ());
        }
        Log.debug("Cache write complete.");
    }
    
    public static void loadCache() {
        File[] folderContents = ChunkOwn.dataFolder.listFiles();
        
        List<World> actualWorlds = ChunkOwn.plugin.getServer().getWorlds();
        
        List<File> validWorldEntries = new ArrayList<File>();
        
        for (File worldConfig : folderContents) {
            for (World world : actualWorlds) {
                if (worldConfig.getName().replaceAll(".yml", "").equals(world.getName())) {
                    validWorldEntries.add(worldConfig);
                    Log.debug("added " + worldConfig.getPath());
                }
            }
        }
        
        for (File file : validWorldEntries){
            Log.debug("started check for " + file.getPath());
            Configuration fileConfig = new Configuration(file);
            fileConfig.load();
            Log.debug("keys: " + fileConfig.getKeys());
            for (String key : fileConfig.getKeys()) {
                Log.debug("Found key: " + key);
                String[] coords = key.split(", ");
                int xCoord = Integer.parseInt(coords[0]);
                int zCoord = Integer.parseInt(coords[1]);
                Log.debug("Coords: " + coords[0] +", "+ coords[1]);
                Chunk chunk = ChunkOwn.plugin.getServer().getWorld(file.getName().replaceAll(".yml", "")).getChunkAt(xCoord, zCoord);
                Log.debug("Discovered chunk " + chunk.getX() + ", " + chunk.getZ());
                ChunkDetails details = new ChunkDetails();
                
                details.setName(fileConfig.getString(key + ".name", ""));
                details.setOwner(fileConfig.getString(key + ".owner", ""));
                details.setConquestBacking(fileConfig.getInt(key + ".conquestBacking", 1));
                fileConfig.save();
                
                Cache.cache.put(chunk, details);
            }
        }
    }
}