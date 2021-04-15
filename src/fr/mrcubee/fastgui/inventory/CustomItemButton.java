package fr.mrcubee.fastgui.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/** This class represents a item button in the inventory. The item can be changed depending on the player opening it.
 * @author MrCubee
 * @version 1.0
 * @since 1.0
 */
public class CustomItemButton extends ItemButton {

    /**
     * The action to apply to the item before display.
     */
    private final BiConsumer<? super HumanEntity, ItemStack> itemEditor;

    /** Create the instance of a item button for a specific inventory.
     * @since 1.0
     * @param fastInventory The inventory where the button is placed.
     * @param slot The inventory slot where the button is placed.
     * @param action The action to be performed by the button.
     * @param itemStack The item that represents the button.
     * @param itemEditor The action to apply to the item before display.
     */
    public CustomItemButton(FastInventory fastInventory, int slot, BiConsumer<? super Button, ? super HumanEntity> action, ItemStack itemStack, BiConsumer<? super HumanEntity, ItemStack> itemEditor) {
        super(fastInventory, slot, action, itemStack);
        this.itemEditor = itemEditor;
    }

    @Override
    public ItemStack getItemStack(HumanEntity player) {
        ItemStack itemStack = super.getItemStack(player);

        if (player != null && itemStack != null && this.itemEditor != null)
            this.itemEditor.accept(player, itemStack);
        return itemStack;
    }
}
