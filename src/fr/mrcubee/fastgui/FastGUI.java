package fr.mrcubee.fastgui;

import fr.mrcubee.fastgui.inventory.FastInventory;
import fr.mrcubee.fastgui.inventory.FastInventoryManager;
import fr.mrcubee.langlib.util.PluginFinder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.WeakHashMap;

/** This class is the main class of the library. It brings together useful library methods.
 * @author MrCubee
 * @version 1.0
 * @since 1.0
 */
public class FastGUI {

    /**
     * The Inventory managers for plugins.
     */
    private static final Map<Plugin, FastInventoryManager> INVENTORY_MANAGERS = new WeakHashMap<Plugin, FastInventoryManager>();

    /** Create an inventory.
     * @since 1.0
     * @param line Number of lines that the inventory must have. (Between 1 and 6 inclusive)
     * @return Returns the created inventory. On error, returns null.
     */
    public static FastInventory createInventory(int line) {
        Plugin plugin = (Plugin) PluginFinder.INSTANCE.findPlugin();
        FastInventoryManager inventoryManager;

        if (plugin == null) {
            Bukkit.getLogger().warning("FastGUI library can't find the main plugin class.");
            return null;
        } else if (line < 1 || line > 6)
            return null;
        inventoryManager = INVENTORY_MANAGERS.get(plugin);
        if (inventoryManager == null) {
            inventoryManager = new FastInventoryManager();
            plugin.getServer().getPluginManager().registerEvents(inventoryManager, plugin);
            INVENTORY_MANAGERS.put(plugin, inventoryManager);
        }
        return inventoryManager.createInventory(line);
    }
}
