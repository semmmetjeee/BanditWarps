package nl.bandit.warps.model;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Warp {
    private final String id;
    private final UUID owner;
    private String name;
    private String description;
    private String icon;
    private boolean publicWarp;
    private Location location;
    private final long createdAt;
    private int visitors;
    private final Map<UUID, Long> visitorCooldowns;

    public Warp(String id, UUID owner, String name, Location location) {
        this(id, owner, name, "", "default", true, location, System.currentTimeMillis(), 0, new HashMap<>());
    }
    public Warp(String id, UUID owner, String name, String description, String icon, boolean publicWarp,
                Location location, long createdAt, int visitors, Map<UUID, Long> visitorCooldowns) {
        this.id=id; this.owner=owner; this.name=name; this.description=description; this.icon=icon;
        this.publicWarp=publicWarp; this.location=location.clone(); this.createdAt=createdAt;
        this.visitors=visitors; this.visitorCooldowns=new HashMap<>(visitorCooldowns);
    }
    public Warp copy() { return new Warp(id,owner,name,description,icon,publicWarp,location,createdAt,visitors,visitorCooldowns); }
    public void applyEditableFields(Warp draft) { name=draft.name; description=draft.description; icon=draft.icon; publicWarp=draft.publicWarp; }
    public String id(){return id;} public UUID owner(){return owner;} public String name(){return name;} public void name(String v){name=v;}
    public String description(){return description;} public void description(String v){description=v;} public String icon(){return icon;} public void icon(String v){icon=v;}
    public boolean isPublic(){return publicWarp;} public void publicWarp(boolean v){publicWarp=v;} public Location location(){return location.clone();}
    public void location(Location v){location=v.clone();} public long createdAt(){return createdAt;} public int visitors(){return visitors;}
    public Map<UUID,Long> visitorCooldowns(){return visitorCooldowns;}
    public boolean registerVisit(UUID player,long now,long cooldown){if(owner.equals(player))return false;long last=visitorCooldowns.getOrDefault(player,0L);if(now-last<cooldown)return false;visitorCooldowns.put(player,now);visitors++;return true;}
}
