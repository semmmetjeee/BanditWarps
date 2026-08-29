package nl.bandit.warps;
import nl.bandit.warps.command.WarpCommand; import nl.bandit.warps.menu.*; import nl.bandit.warps.storage.WarpStore;
import org.bukkit.ChatColor; import org.bukkit.command.CommandSender; import org.bukkit.configuration.file.YamlConfiguration; import org.bukkit.plugin.java.JavaPlugin;
import java.io.File; import java.util.Map;

public final class BanditWarpsPlugin extends JavaPlugin {
 private WarpStore warps; private MenuService menus; private TextInputService input; private YamlConfiguration messages,commands;
 @Override public void onEnable(){saveDefaultConfig();for(String file:new String[]{"menus.yml","icons.yml","messages.yml","commands.yml"})saveResource(file,false);loadExtraConfigs();warps=new WarpStore(getDataFolder());menus=new MenuService(this);input=new TextInputService(this);WarpCommand command=new WarpCommand(this);getCommand("pwarp").setExecutor(command);getCommand("pwarp").setTabCompleter(command);getServer().getPluginManager().registerEvents(new MenuListener(this),this);getLogger().info("BanditWarps v"+getDescription().getVersion()+" is ingeschakeld.");}
 @Override public void onDisable(){if(warps!=null)warps.saveAll();}
 private void loadExtraConfigs(){messages=YamlConfiguration.loadConfiguration(new File(getDataFolder(),"messages.yml"));commands=YamlConfiguration.loadConfiguration(new File(getDataFolder(),"commands.yml"));}
 public WarpStore warps(){return warps;} public MenuService menus(){return menus;} public TextInputService input(){return input;}
 public String word(String key){return commands.getString("subcommands."+key,key).toLowerCase();}
 public void message(CommandSender target,String key,Map<String,String> variables){String value=messages.getString(key,"&cOntbrekend bericht: "+key);for(Map.Entry<String,String> entry:variables.entrySet())value=value.replace(entry.getKey(),entry.getValue());target.sendMessage(ChatColor.translateAlternateColorCodes('&',messages.getString("prefix","&6&lBanditWarps &8» ")+value));}
 public void reloadAll(){reloadConfig();loadExtraConfigs();menus.reload();warps.saveAll();}
}
