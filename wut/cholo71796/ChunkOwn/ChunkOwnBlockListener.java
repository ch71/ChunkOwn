package wut.cholo71796.ChunkOwn;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author Cole Erickson
 */
public class ChunkOwnBlockListener extends BlockListener{
    public static ChunkOwn plugin;
    ChunkOwnBlockListener(ChunkOwn instance) {
        plugin = instance;
    }
    
    @Override
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        
        if (!Cache.cache.containsKey(chunk) || !Cache.cache.get(chunk).getOwner().equals(player.getDisplayName())) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        
        if (!Cache.cache.containsKey(chunk) || !Cache.cache.get(chunk).getOwner().equals(player.getDisplayName())) {
            event.setCancelled(true);
        }
    }
}