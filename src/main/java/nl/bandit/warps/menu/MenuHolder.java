package nl.bandit.warps.menu;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
public record MenuHolder(MenuType type) implements InventoryHolder { @Override public Inventory getInventory(){return null;} }
