/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ChunkOwn;

import java.io.File;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import wut.cholo71796.ChunkOwn.commands.*;
import wut.cholo71796.ChunkOwn.utilities.*;

/**
 *
 * @author Cole Erickson
 */
public class ChunkOwn extends JavaPlugin{
    private final ChunkOwnPlayerListener playerListener = new ChunkOwnPlayerListener(this);
    private final ChunkOwnBlockListener blockListener = new ChunkOwnBlockListener(this);
    
    public static Plugin plugin;
    public static File dataFolder;
    
    private static PluginDescriptionFile pdfFile;
    private static PermissionHandler permissionHandler;
    
    public static Map<Player, Integer> conquest = new HashMap<Player, Integer>();
    
    @Override
    public void onDisable() {
        Cache.storeCache();
    }
    
    @Override
    public void onEnable() {
        plugin = this;
        pdfFile = plugin.getDescription();
        dataFolder = getDataFolder();
        
        new Log(pdfFile.getName());
        new Cache(plugin.getServer());
        
        setupPermissions();
        conquestHandler();
        getCommand("chunkown").setExecutor(new ChunkOwnCommand());
        
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
        
        if (ChunkOwnConfig.isVerbose())
            Log.debug("enabled in DEBUG mode.");
        else
            Log.info("enabled.");
    }
    
    private void setupPermissions() {
        if (permissionHandler != null) {
            return;
        }
        
        Plugin permissionsPlugin = getServer().getPluginManager().getPlugin("Permissions");
        
        if (permissionsPlugin == null) {
            Log.severe("Permission system not detected!");
            return;
        }
        
        permissionHandler = ((Permissions) permissionsPlugin).getHandler();
        Log.info("Permissions found: " + ((Permissions) permissionsPlugin).getDescription().getFullName());
    }
    
    public static boolean hasPermission(Player player, String permnode) {
        if (player.isOp())
            return true;
        else
            return ChunkOwn.permissionHandler.has(player, permnode);
    }
    
    
    
    public static String getMessageArgs(int startPoint, String[] args) {
        StringBuilder output = new StringBuilder("");
        for (int i = startPoint ; i < args.length ; i++ ) {
            output.append(args[i] + " ");
        }
        return output.toString().trim();
    }
    
    private static void conquestHandler() {
        plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
            Long iii = System.currentTimeMillis() / ChunkOwnConfig.getTimeBetweenConquestDistribution();
            @Override
            public void run() {
                Log.info("cycled");
                if (System.currentTimeMillis() / ChunkOwnConfig.getTimeBetweenConquestDistribution() > iii) {
                    Log.info("real cycled");
                    for (Player player : conquest.keySet()) {
                        int playersConquest = conquest.get(player);
                        if (playersConquest + ChunkOwnConfig.getAmountOfConquestPerConquestDistribution() >= ChunkOwnConfig.getConquestCap())
                            conquest.put(player, ChunkOwnConfig.getConquestCap());
                        else
                            conquest.put(player, playersConquest + ChunkOwnConfig.getAmountOfConquestPerConquestDistribution());
                    }
                    iii++;
                }
            }
        }, 0L, ChunkOwnConfig.getTimeBetweenConquestDistribution() / 1000 * 20 / 6);
    }  // 1000 to convert millis, 20 to convert to ticks, 6 for a sixth of whatever
}
