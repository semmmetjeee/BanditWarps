package nl.bandit.warps.storage;

import nl.bandit.warps.model.Warp;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.*;
import java.util.*;

public final class WarpStore {
    private final File file; private final Map<String,Warp> warps=new HashMap<>();
    public WarpStore(File folder){file=new File(folder,"warps.yml");load();}
    public Optional<Warp> findByName(String name){return warps.values().stream().filter(w->w.name().equalsIgnoreCase(name)).findFirst();}
    public Optional<Warp> findById(String id){return Optional.ofNullable(warps.get(id));} public Collection<Warp> all(){return new ArrayList<>(warps.values());}
    public long countOwnedBy(UUID owner){return warps.values().stream().filter(w->w.owner().equals(owner)).count();}
    public boolean nameAvailable(String name,String exceptId){return warps.values().stream().noneMatch(w->!w.id().equals(exceptId)&&w.name().equalsIgnoreCase(name));}
    public void save(Warp warp){warps.put(warp.id(),warp);saveAll();} public void delete(Warp warp){warps.remove(warp.id());saveAll();}
    private void load(){YamlConfiguration y=YamlConfiguration.loadConfiguration(file);ConfigurationSection root=y.getConfigurationSection("warps");if(root==null)return;for(String id:root.getKeys(false)){ConfigurationSection s=root.getConfigurationSection(id);if(s==null)continue;try{World world=Bukkit.getWorld(s.getString("location.world",s.getString("world","")));if(world==null)continue;String base=s.contains("location")?"location.":"";Location l=new Location(world,s.getDouble(base+"x"),s.getDouble(base+"y"),s.getDouble(base+"z"),(float)s.getDouble(base+"yaw"),(float)s.getDouble(base+"pitch"));Map<UUID,Long> visits=new HashMap<>();ConfigurationSection vc=s.getConfigurationSection("visitor-cooldowns");if(vc!=null)for(String uuid:vc.getKeys(false))visits.put(UUID.fromString(uuid),vc.getLong(uuid));Warp w=new Warp(id,UUID.fromString(s.getString("owner")),s.getString("name",id),s.getString("description",""),s.getString("icon","default"),s.getBoolean("public",true),l,s.getLong("created-at",s.getLong("created",System.currentTimeMillis())),s.getInt("visitors"),visits);warps.put(id,w);}catch(RuntimeException ex){Bukkit.getLogger().warning("[BanditWarps] Warp '"+id+"' overgeslagen: "+ex.getMessage());}}}
    public void saveAll(){YamlConfiguration y=new YamlConfiguration();for(Warp w:warps.values()){String p="warps."+w.id()+".";Location l=w.location();y.set(p+"owner",w.owner().toString());y.set(p+"name",w.name());y.set(p+"description",w.description());y.set(p+"icon",w.icon());y.set(p+"public",w.isPublic());y.set(p+"created-at",w.createdAt());y.set(p+"visitors",w.visitors());y.set(p+"location.world",l.getWorld().getName());y.set(p+"location.x",l.getX());y.set(p+"location.y",l.getY());y.set(p+"location.z",l.getZ());y.set(p+"location.yaw",l.getYaw());y.set(p+"location.pitch",l.getPitch());w.visitorCooldowns().forEach((uuid,time)->y.set(p+"visitor-cooldowns."+uuid,time));}try{y.save(file);}catch(IOException ex){throw new IllegalStateException("Kon warps.yml niet opslaan",ex);}}
}
