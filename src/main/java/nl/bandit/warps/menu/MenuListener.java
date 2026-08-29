package nl.bandit.warps.menu;
import nl.bandit.warps.*; import nl.bandit.warps.model.Warp; import org.bukkit.*; import org.bukkit.entity.Player; import org.bukkit.event.*; import org.bukkit.event.inventory.InventoryClickEvent;
public final class MenuListener implements Listener {
 private final BanditWarpsPlugin plugin; public MenuListener(BanditWarpsPlugin p){plugin=p;}
 @EventHandler public void click(InventoryClickEvent e){if(!(e.getWhoClicked() instanceof Player p)||e.getCurrentItem()==null)return;String title=ChatColor.stripColor(e.getView().getTitle());if(!title.contains("Warps")&&!title.contains("Instellingen")&&!title.contains("Iconen")&&!title.contains("Jouw"))return;e.setCancelled(true);String name=ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());Warp w=plugin.menus().editing(p);
  if(title.contains("Instellingen")&&w!=null){if(name.contains("Status")){w.isPublic=!w.isPublic;plugin.warps().put(w);plugin.menus().settings(p,w);}else if(name.contains("icoon")||name.contains("Icoon"))plugin.menus().icons(p,w);else if(name.contains("Opslaan")){plugin.warps().put(w);p.closeInventory();}return;}
  if(title.contains("Iconen")&&w!=null){String icon=plugin.menus().icon(e.getCurrentItem());if(icon!=null){w.icon=icon;plugin.warps().put(w);plugin.menus().settings(p,w);}return;}
  if(title.contains("Warps")){if(name.contains("Sorteren")){plugin.menus().nextSort(p);plugin.menus().browser(p);return;}if(name.contains("Jouw warps")){plugin.menus().owned(p);return;}for(Warp warp:plugin.warps().all())if(ChatColor.stripColor(warp.name).equals(name)){p.performCommand("pwarp go "+warp.name);return;}}
 }
}
