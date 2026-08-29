package nl.bandit.warps;
import nl.bandit.warps.command.WarpCommand; import nl.bandit.warps.menu.*; import nl.bandit.warps.storage.WarpStore; import org.bukkit.plugin.java.JavaPlugin;
public final class BanditWarpsPlugin extends JavaPlugin {
 private WarpStore warps; private MenuService menus; private TextInputService input;
 @Override public void onEnable(){saveDefaultConfig();for(String f:new String[]{"menus.yml","icons.yml","messages.yml","commands.yml"})saveResource(f,false);warps=new WarpStore(getDataFolder());menus=new MenuService(this);input=new TextInputService(this);WarpCommand command=new WarpCommand(this);getCommand("pwarp").setExecutor(command);getCommand("pwarp").setTabCompleter(command);getServer().getPluginManager().registerEvents(new MenuListener(this),this);getServer().getPluginManager().registerEvents(input,this);}
 public WarpStore warps(){return warps;} public MenuService menus(){return menus;} public TextInputService input(){return input;} public void reloadAll(){reloadConfig();menus.reload();warps.save();}
}
