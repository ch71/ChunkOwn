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
    
    //TODO consolidate storage
    public static void storeCache() {
        Log.debug("Writing cache to hard drive...");
        for (Chunk key : cache.keySet()) {
            cache.get(key).write(new File(ChunkOwn.dataFolder + File.separator + key.getWorld().getName(), key.getX() + ", " + key.getZ() + ".yml"));
        }
        Log.debug("Cache write complete.");
    }
    
    public static void loadCache() {        
        File[] folderContents = ChunkOwn.dataFolder.listFiles();
        
        List<World> actualWorlds = ChunkOwn.plugin.getServer().getWorlds();
        
        List<File> validWorldEntries = new ArrayList<File>();
        List<File> validChunkFiles = new ArrayList<File>();
        
        for (File worldFolder : folderContents) {
            for (World world : actualWorlds) {
                if (worldFolder.getName().equals(world.getName())) {
                    validWorldEntries.add(worldFolder);
                }
            }
        }
        
        for (File world : validWorldEntries) {
            for (File chunkFile : world.listFiles()) {
                if (chunkFile.getName().replaceAll(".yml", "").matches("-?[0-9]+, -?[0-9]+")) {
                    validChunkFiles.add(chunkFile);
                }
            }
        }
        
        
        for (File file : validChunkFiles){
            Configuration fileConfig = new Configuration(file);
            
            int xCoord = Integer.parseInt(file.getName().split(", ")[0]);
            int zCoord = Integer.parseInt(file.getName().split(", ")[1].replaceAll(".yml", ""));
            
            Chunk chunk = ChunkOwn.plugin.getServer().getWorld(file.getParentFile().getName()).getChunkAt(xCoord, zCoord);
            ChunkDetails details = new ChunkDetails();
                
            fileConfig.load();
            details.setName(fileConfig.getString("name", ""));
            details.setOwner(fileConfig.getString("owner", ""));
            details.setConquestBacking(fileConfig.getInt("conquestBacking", 1));
            
            Cache.cache.put(chunk, details);
        }
    }
}