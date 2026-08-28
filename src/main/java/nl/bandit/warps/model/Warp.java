package nl.bandit.warps.model;
import java.util.*; import org.bukkit.*;
public final class Warp {
 public final String id; public final UUID owner; public String name,description="",icon="default"; public boolean isPublic=true; public final Location location; public final long created=System.currentTimeMillis(); public int visitors; public final Map<UUID,Long> cooldowns=new HashMap<>();
 public Warp(String id,UUID owner,String name,Location location){this.id=id;this.owner=owner;this.name=name;this.location=location;}
}
