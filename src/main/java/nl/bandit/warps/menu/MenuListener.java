package nl.bandit.warps.menu;
import nl.bandit.warps.BanditWarpsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;

public final class MenuListener implements Listener {
 private final BanditWarpsPlugin plugin; public MenuListener(BanditWarpsPlugin plugin){this.plugin=plugin;}
 @EventHandler public void onClick(InventoryClickEvent event){if(!(event.getWhoClicked() instanceof Player player)||!(event.getView().getTopInventory().getHolder() instanceof MenuHolder holder))return;event.setCancelled(true);if(event.getRawSlot()<0||event.getRawSlot()>=event.getView().getTopInventory().getSize())return;String action=plugin.menus().action(event.getCurrentItem());if(action==null)return;switch(action){case "warp"->handleWarp(player,holder.type(),plugin.menus().warpId(event.getCurrentItem()));case "sort"->plugin.menus().nextSort(player);case "previous"->plugin.menus().nextPage(player,false,holder.type());case "next"->plugin.menus().nextPage(player,true,holder.type());case "your-warps"->plugin.menus().owned(player);case "back"->{if(holder.type()==MenuType.ICONS)plugin.menus().openSettings(player);else plugin.menus().browser(player);}case "name"->openInput(player,true);case "description"->openInput(player,false);case "status"->{EditSession s=plugin.menus().edit(player);if(s!=null){s.draft().publicWarp(!s.draft().isPublic());plugin.menus().openSettings(player);}}case "icon"->plugin.menus().openIcons(player);case "select-icon"->{EditSession s=plugin.menus().edit(player);String icon=plugin.menus().iconId(event.getCurrentItem());if(s!=null&&icon!=null){s.draft().icon(icon);plugin.menus().openSettings(player);}}case "save"->plugin.menus().saveEdit(player);case "cancel"->plugin.menus().cancelEdit(player);case "delete"->plugin.menus().openDeleteConfirm(player);case "confirm-delete"->plugin.menus().deleteEdited(player);case "cancel-delete"->plugin.menus().openSettings(player);}}
 private void handleWarp(Player player,MenuType type,String id){if(id==null)return;plugin.warps().findById(id).ifPresent(warp->{if(type==MenuType.OWNED)plugin.menus().beginEdit(player,warp,false);else player.performCommand("pwarp go "+warp.name());});}
 private void openInput(Player player,boolean name){EditSession session=plugin.menus().edit(player);if(session!=null)plugin.input().open(player,session.draft(),name);}
 @EventHandler public void onDrag(InventoryDragEvent event){if(event.getView().getTopInventory().getHolder() instanceof MenuHolder)event.setCancelled(true);}
}
