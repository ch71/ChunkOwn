package wut.cholo71796.ChunkOwn.commands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wut.cholo71796.ChunkOwn.ChunkOwn;
import wut.cholo71796.ChunkOwn.Cache;
import wut.cholo71796.ChunkOwn.ChunkOwnConfig;
import wut.cholo71796.ChunkOwn.variables.ChunkDetails;

public class ChunkOwnCommand implements CommandExecutor {
    
    private Player player;
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)) {
            sender.sendMessage("The command must be run by a player.");
            return false;
        }
        
        this.player = (Player) sender;
        
        if (args.length == 0)
            return runConquestCommand();
                // normal commands
        else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("information") || args[0].equalsIgnoreCase("here"))
            return runInfoCommand();
        else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("restart"))
            return runReloadCommand();
        else if (args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("grab") || args[0].equalsIgnoreCase("take")) {
            if (args.length >= 2)
                return runClaimAndNameCommand(ChunkOwn.getMessageArgs(1, args));
            else
                return runClaimCommand();
        } else if (args[0].equalsIgnoreCase("name") || args[0].equalsIgnoreCase("rename"))
            return runNameCommand(ChunkOwn.getMessageArgs(1, args));
        return false;
    }
    
    //        private boolean runGetCacheCommand() {
    //            for (Chunk key : Cache.cache.keySet()) {
    //                Log.debug(key.getX() +  ", " + key.getZ() + " with:");
    //                Log.debug(" " + Cache.cache.get(key).getName());
    //                Log.debug(" " + Cache.cache.get(key).getOwner());
    //                Log.debug(" " + Cache.cache.get(key).getConquestBacking());
    //            }
    //            return true;
    //        }
    
    private boolean runReloadCommand() {
        Cache.storeCache();
        Cache.loadCache();
        return true;
    }
    
    private boolean runConquestCommand() {
        player.sendMessage(ChatColor.GOLD + "You have " + ChatColor.WHITE + ChunkOwn.conquest.get(player) + " conquest" + ChatColor.GOLD + ".");
        return true;
    }
    
    private boolean runClaimCommand() {
        Chunk chunk = player.getLocation().getBlock().getChunk();
        
        if (ChunkOwn.conquest.get(player) < ChunkOwnConfig.getCostClaimChunk()) {
            player.sendMessage(ChatColor.GOLD + "You do not have enough " + ChatColor.WHITE + "conquest " + ChatColor.GOLD + "to do that.");
            return false;
        }        
       
        if (!Cache.cache.containsKey(chunk)) {
            ChunkDetails details = new ChunkDetails();
            details.setOwner(player.getDisplayName());
            Cache.cache.put(chunk, details);
            
            ChunkOwn.conquest.put(player, ChunkOwn.conquest.get(player) - ChunkOwnConfig.getCostClaimChunk());
            
            player.sendMessage(ChatColor.GOLD + "You now own this chunk.");
            return true;
        } else if (Cache.cache.get(chunk).getOwner().equals(player.getDisplayName())) {
            player.sendMessage(ChatColor.GOLD + "You already own this chunk.");
            return false;
        } else {
            player.sendMessage(ChatColor.GOLD + "You may not claim this chunk.");
            return false;
        }
    }
    
    private boolean runClaimAndNameCommand(String name) {
        Chunk chunk = player.getLocation().getBlock().getChunk();
        
        if (ChunkOwn.conquest.get(player) < ChunkOwnConfig.getCostClaimChunk()) {
            player.sendMessage(ChatColor.GOLD + "You do not have enough " + ChatColor.WHITE + "conquest " + ChatColor.GOLD + "to do that.");
            return false;
        }
        
        if (!Cache.cache.containsKey(chunk)) {
            ChunkDetails details = new ChunkDetails();
            details.setOwner(player.getDisplayName());
            details.setName(name);
            Cache.cache.put(chunk, details);
            
            ChunkOwn.conquest.put(player, ChunkOwn.conquest.get(player) - ChunkOwnConfig.getCostClaimChunk());
            
            player.sendMessage(ChatColor.GOLD + "You now own " + ChatColor.WHITE + name + ChatColor.GOLD + ".");
            return true;
        } else if (Cache.cache.get(chunk).getOwner().equals(player.getDisplayName())) {
            player.sendMessage(ChatColor.GOLD + "You already own this chunk.");
            return false;
        } else {
            player.sendMessage(ChatColor.GOLD + "You may not claim this chunk.");
            return false;
        }
    }
    
    private boolean runInfoCommand() {
        Chunk chunk = player.getLocation().getBlock().getChunk();
        
        String chunkDisplayName;
        if (Cache.cache.containsKey(chunk)) //If there's data on the chunk, use the name
            chunkDisplayName = Cache.cache.get(chunk).getName();
        else //otherwise give it the unowned name
            chunkDisplayName = ChunkOwnConfig.getUnownedChunkName();
        
        // tell the player whatever it is
        player.sendMessage(ChatColor.DARK_GRAY + "[-----] " + ChatColor.GOLD + chunkDisplayName + ChatColor.DARK_GRAY + " [-----]");
        
        if (Cache.cache.containsKey(chunk)) { //If there's data, tell who is owner
            player.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.WHITE + Cache.cache.get(chunk).getOwner());
        }
        return true;
    }
    
    private boolean runNameCommand(String name) {
        Chunk chunk = player.getLocation().getBlock().getChunk();
        
        if (!Cache.cache.containsKey(chunk)) {
            player.sendMessage(ChatColor.GOLD + "You may not name " + ChunkOwnConfig.getUnownedChunkName().toLowerCase() + " chunks.");
            return false;
        } else if (Cache.cache.get(chunk).getOwner().equals(player.getDisplayName())) {
            if(name.length() > 20){
                player.sendMessage(ChatColor.GOLD + "The chunk name must be 20 characters or less.");
                return false;
            } else if (name.equals("")) {
                player.sendMessage(ChatColor.GOLD + "Chunk name cleared.");
                Cache.cache.get(chunk).setName("");
                return true;
            } else if (name.equals(Cache.cache.get(chunk).getNameExact())) {
                player.sendMessage(ChatColor.GOLD + "That is already the chunk's name!");
                return false;
            } else {
                player.sendMessage(ChatColor.GOLD + "Chunk renamed to: " + ChatColor.WHITE + name);
                Cache.cache.get(chunk).setName(name);
                return true;
            }
        } else {
            player.sendMessage(ChatColor.GOLD + "You may not name this chunk.");
            return false;
        }
    }
}
