package wut.cholo71796.ChunkOwn.variables;

import java.io.File;

import org.bukkit.util.config.Configuration;
import wut.cholo71796.ChunkOwn.ChunkOwn;

import wut.cholo71796.ChunkOwn.ChunkOwnConfig;

public class ChunkDetails {
    
    private File configFile;
    private Configuration config;
    
    private String owner;
    private String name;
    private int conquestBacking;
    
    public ChunkDetails() {
        this.name = ChunkOwnConfig.getDefaultChunkName();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + conquestBacking;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChunkDetails other = (ChunkDetails) obj;
        if (conquestBacking != other.conquestBacking)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        return true;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public String getName() {
        if (name != null)
            return name;
        else
            return ChunkOwnConfig.getUnownedChunkName();
    }
    
    public String getNameExact() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getConquestBacking() {
        return conquestBacking;
    }
    
    public void setConquestBacking(int conquestBacking) {
        this.conquestBacking = conquestBacking;
    }
    
    public void write(String world, String chunkPrefix) {
        config = new Configuration(new File(ChunkOwn.dataFolder, world + ".yml"));        
        config.setProperty(chunkPrefix + ".name", this.getName());
        config.setProperty(chunkPrefix + ".owner", this.getOwner());
        config.setProperty(chunkPrefix + ".conquestBacking", this.getConquestBacking());
        config.save();
        config.load();
        name = config.getString(chunkPrefix + ".name", "");
        owner = config.getString(chunkPrefix + ".owner", "");
        conquestBacking = config.getInt(chunkPrefix + ".conquestBacking", 1);
        config.save();
    }
}
