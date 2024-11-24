package fr.mrcubee.fastgui.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** This class represents a item button in the inventory.
 * @author MrCubee
 * @Contributor Lightnew
 * @version 1.0
 * @since 1.0
 */
public class ItemButton extends Button {

    /**
     * The item that represents the button.
     */
    private final ItemStack itemStack;

    /** Create the instance of a item button for a specific inventory.
     * @since 1.0
     * @param fastInventory The inventory where the button is placed.
     * @param slot The inventory slot where the button is placed.
     * @param action The action to be performed by the button.
     * @param itemStack The item that represents the button.
     */
    public ItemButton(FastInventory fastInventory, int slot, BiConsumer<? super Button, ? super HumanEntity> action, ItemStack itemStack, Consumer<? super InventoryClickEvent> event) {
        super(fastInventory, slot, action, event);
        this.itemStack = itemStack;
    }

    /** This function gives the item which represents the button according to the player making the request.
     * @since 1.0
     * @param player The player who wants to have the item.
     * @return The item requested by the player.
     */
    public ItemStack getItemStack(HumanEntity player) {
        return this.itemStack.clone();
    }
}
