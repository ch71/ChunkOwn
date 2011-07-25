/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ChunkOwn;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author Cole Erickson
 */
public class ChunkOwnPlayerListener extends PlayerListener{
    
    public static ChunkOwn plugin;
    public ChunkOwnPlayerListener(ChunkOwn instance){
        plugin = instance;
    }
                    
    @Override
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        
        if (!ChunkOwn.conquest.containsKey(player)){
            ChunkOwn.conquest.put(player, ChunkOwnConfig.getConquestCap());
        }
    }
}