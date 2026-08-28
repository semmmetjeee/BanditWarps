package nl.bandit.warps;
import nl.bandit.warps.command.WarpCommand; import nl.bandit.warps.menu.MenuListener; import nl.bandit.warps.menu.MenuService; import nl.bandit.warps.storage.WarpStore; import org.bukkit.plugin.java.JavaPlugin;
public final class BanditWarpsPlugin extends JavaPlugin {
 private WarpStore warps; private MenuService menus;
 @Override public void onEnable(){saveDefaultConfig();for(String f:new String[]{"menus.yml","icons.yml","messages.yml","commands.yml"})saveResource(f,false);warps=new WarpStore(getDataFolder());menus=new MenuService(this);WarpCommand command=new WarpCommand(this);getCommand("pwarp").setExecutor(command);getCommand("pwarp").setTabCompleter(command);getServer().getPluginManager().registerEvents(new MenuListener(this),this);}
 public WarpStore warps(){return warps;} public MenuService menus(){return menus;} public void reloadAll(){reloadConfig();menus.reload();warps.save();}
}
