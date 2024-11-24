package fr.mrcubee.fastgui.inventory;

import fr.mrcubee.weak.WeakHashSet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Set;

/** This class manages the inventory created by a plugin.
 * @author MrCubee
 * @Contributor Lightnew
 * @version 1.0
 * @since 1.0
 */
public class FastInventoryManager implements Listener {

    /**
     * Inventories created by the plugin.
     */
    private final Set<FastInventory> inventories;

    public FastInventoryManager() {
        this.inventories = new WeakHashSet<>();
    }

    /** Create an inventory.
     * @since 1.0
     * @param line Number of lines that the inventory must have. (Between 1 and 6 inclusive)
     * @return Returns the created inventory. On error, returns null.
     */
    public FastInventory createInventory(int line) {
        FastInventory fastInventory;

        if (line < 1 || line > 6)
            return null;
        fastInventory = new FastInventory(line * 9);
        this.inventories.add(fastInventory);
        return fastInventory;
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if (event == null)
            return;
        for (FastInventory fastInventory : this.inventories) {
            if (fastInventory.playerExecute(event, event.getWhoClicked(), event.getSlot())
            || fastInventory.isInventoryOpen(event.getWhoClicked())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event) {
        if (event == null)
            return;
        for (FastInventory fastInventory : this.inventories)
            fastInventory.closeInventory(event.getPlayer());
    }
}
