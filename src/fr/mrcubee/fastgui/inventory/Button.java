package fr.mrcubee.fastgui.inventory;

import org.bukkit.entity.HumanEntity;
import java.util.Objects;
import java.util.function.BiConsumer;

/** This class represents a button in the inventory.
 * @author MrCubee
 * @version 1.0
 * @since 1.0
 */
public class Button {

    /**
     * The inventory where the button is placed.
     */
    private final FastInventory fastInventory;

    /**
     * The inventory slot where the button is placed.
     */
    public final int slot;

    /**
     * The action to be performed by the button.
     */
    private final BiConsumer<? super Button, ? super HumanEntity> action;

    /** Create the instance of a button for a specific inventory.
     * @since 1.0
     * @param fastInventory The inventory where the button is placed.
     * @param slot The inventory slot where the button is placed.
     * @param action The action to be performed by the button.
     */
    protected Button(FastInventory fastInventory, int slot, BiConsumer<? super Button, ? super HumanEntity> action) {
        this.fastInventory = fastInventory;
        this.slot = slot;
        this.action = action;
    }

    /** Perform the button action depending on the player who clicked.
     * @since 1.0
     * @param humanEntity The player who clicked the button.
     */
    protected void execute(HumanEntity humanEntity) {
        if (humanEntity == null || this.action == null)
            return;
        this.action.accept(this, humanEntity);
    }

    /** Remove the button from inventory
     * @since 1.0
     */
    public void remove() {
        if (this.fastInventory != null)
            this.fastInventory.buttons.remove(this);
    }

    /** Give the inventory where the button is located.
     * @since 1.0
     * @return Returns the inventory where the button is located.
     */
    public FastInventory getFastInventory() {
        return this.fastInventory;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Button && obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fastInventory, this.slot);
    }
}
