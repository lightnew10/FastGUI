package fr.mrcubee.fastgui.inventory;

import fr.mrcubee.fastgui.tool.WeakHashSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** This class builds the inventory that will be given to the player.
 * @author MrCubee
 * @Contributor Lightnew
 * @version 1.0
 * @since 1.0
 */
public class FastInventory {

    /**
     * The size of the inventory (the number of slots).
     */
    private final int inventorySize;

    /**
     * The contents of the inventory (Without the item buttons).
     */
    private final ItemStack[] contents;

    /**
     * The list of inventory buttons.
     */
    protected final Set<Button> buttons;

    /**
     * The default button instructions.
     */
    private BiConsumer<? super Button, HumanEntity> defaultButton;
    private Consumer<? super InventoryClickEvent> defaultButtonEvent;

    /**
     * The element to use to fill all free slots.
     */
    private ItemStack fillItem;

    /**
     * List of players whose inventory is open.
     */
    private final Set<HumanEntity> viewers;

    /** Create the inventory constructor instance with a specific size (Multiple of 9 between 9 and 54 included).
     * @since 1.0
     * @param inventorySize The size of the inventory (the number of slots).
     */
    protected FastInventory(int inventorySize) {
        this.inventorySize = inventorySize;
        this.contents = new ItemStack[this.inventorySize];
        this.buttons = new HashSet<>();
        this.defaultButton = null;
        this.defaultButtonEvent = null;
        this.fillItem = null;
        this.viewers = new WeakHashSet<>();
    }

    /** Defines the Item with which the constructor should fill the free slots.
     * @since 1.0
     * @param itemStack The item to use.
     */
    public void fillInventory(ItemStack itemStack) {
        this.fillItem = itemStack;
    }

    /** Place an item in the desired slot.
     * @since 1.0
     * @param slot The desired slot.
     * @param itemStack  The item to use.
     * @return Returns true if the element is placed, otherwise on error returns false.
     */
    public boolean setItem(int slot, ItemStack itemStack) {
        if (slot < 1 || slot > this.inventorySize)
            return false;
        this.contents[slot] = itemStack;
        return true;
    }

    /** Get the item from a player's point of view in the specified slot.
     * @since 1.0
     * @param slot The desired slot.
     * @param player The player to use for his point of view.
     * @return Returns the item seen by the specified player.
     */
    public ItemStack getItem(int slot, HumanEntity player) {
        Button button;

        if (slot < 1 || slot > this.inventorySize)
            return null;
        button = getButton(slot);
        if (button instanceof ItemButton)
            return ((ItemButton) button).getItemStack(player);
        if (this.contents[slot] == null)
            return this.fillItem;
        return this.contents[slot];
    }

    /** Get the item in the specified slot.
     * @since 1.0
     * @param slot The desired slot.
     * @return Returns the item seen by the specified player.
     */
    public ItemStack getItem(int slot) {
        return getItem(slot, null);
    }

    /** Get the item in the specified slot (Without buttons).
     * @since 1.0
     * @param slot The desired slot.
     * @return
     */
    public ItemStack getContentItem(int slot) {
        if (slot < 1 || slot > this.contents.length)
            return null;
        return this.contents[slot];
    }

    /** Get the item used by the constructor to fill in the free slots.
     * @since 1.0
     * @return The item used.
     */
    public ItemStack getFillItem() {
        return this.fillItem;
    }

    /** Fill the desired row with the given item.
     * @since 1.0
     * @param row The desired row.
     * @param itemStack The item to use.
     * @return Returns true if items are placed, otherwise returns false on error.
     */
    public boolean setRow(int row, ItemStack itemStack) {
        int start = row * 9;
        int stop = start + 9;

        if (start < 0 || stop > this.inventorySize)
            return false;
        for (int i = start; i < stop; i++)
            this.contents[i] = itemStack;
        return true;
    }

    /** Fill the desired column with the given item.
     * @since 1.0
     * @param column The desired column.
     * @param itemStack The item to use.
     * @return Returns true if items are placed, otherwise returns false on error.
     */
    public boolean setColumn(int column, ItemStack itemStack) {
        if (column < 0 || column >= 9)
            return false;
        for (int i = column; i < this.inventorySize; i += 9)
            this.contents[i] = itemStack;
        return true;
    }

    /** Create a default button in the inventory, which is executed if the player does not click any of the registered buttons.
     * @since 1.0
     * @param action The instructions to be executed by the button.
     * @return Returns the created button instance.
     */
    public Button createDefaultButton(BiConsumer<? super Button, HumanEntity> action, Consumer<? super InventoryClickEvent> event) {
        this.defaultButton = action;
        this.defaultButtonEvent = event;
        return getDefaultButton();
    }

    /** Get the default button instance.
     * @since 1.0
     * @return Returns the default button instance or returns null if the button has not been created.
     */
    public Button getDefaultButton() {
        if (this.defaultButton == null)
            return null;
        return new Button(this, -1, this.defaultButton, this.defaultButtonEvent) {
            @Override
            public void remove() {
                getFastInventory().defaultButton = null;
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(getFastInventory());
            }
        };
    }

    /** Remove the default button.
     * @since 1.0
     * @return Returns true if the button has been deleted, otherwise returns false.
     */
    public boolean removeDefaultButton() {
        if (this.defaultButton == null)
            return false;
        this.defaultButton = null;
        return true;
    }

    /** Create a button in the inventory.
     * @since 1.0
     * @param slot The desired slot.
     * @param action The instructions to be executed by the button.
     * @return Returns the created button instance.
     */
    public Button createButton(int slot, BiConsumer<? super Button, HumanEntity> action, Consumer<? super InventoryClickEvent> event) {
        Button button;

        if (slot < 0 || slot >= this.inventorySize)
            return null;
        button = new Button(this, slot, action, event);
        if (!this.buttons.add(button))
            return null;
        return button;
    }

    /** Create an item button in the inventory.
     * @since 1.0
     * @param slot The desired slot.
     * @param itemStack The item that will represent the button.
     * @param action The instructions to be executed by the button.
     * @return Returns the created button instance.
     */
    public ItemButton createItemButton(int slot, ItemStack itemStack, BiConsumer<? super Button, ? super HumanEntity> action, Consumer<? super InventoryClickEvent> event) {
        ItemButton button;

        if (slot < 0 || slot >= this.inventorySize)
            return null;
        button = new ItemButton(this, slot, action, itemStack, event);
        if (!this.buttons.add(button))
            return null;
        return button;
    }

    /** Create a customizable item button based on the player watching.
     * @since 1.0
     * @param slot The desired slot.
     * @param itemStack The item that will represent the button.
     * @param itemEditor The instructions to apply to the item that the button should do depending on the player watching.
     * @param action The instructions to be executed by the button.
     * @return Returns the created button instance.
     */
    public CustomItemButton createCustomItemButton(int slot, ItemStack itemStack, BiConsumer<? super HumanEntity, ItemStack> itemEditor, BiConsumer<? super Button, ? super HumanEntity> action, Consumer<? super InventoryClickEvent> event) {
        CustomItemButton customItemButton;

        if (slot < 0 || slot >= this.inventorySize)
            return null;
        customItemButton = new CustomItemButton(this, slot, action, itemStack, itemEditor, event);
        if (!this.buttons.add(customItemButton))
            return null;
        return customItemButton;
    }
    public CustomItemButton createCustomItemButton(int slot, ItemStack itemStack, Consumer<? super InventoryClickEvent> event) {
        return createCustomItemButton(slot, itemStack, (a, b) -> {}, (a, b) -> {}, event);
    }
    public CustomItemButton createCustomItemButton(int slot, ItemStack itemStack, BiConsumer<? super Button, ? super HumanEntity> action) {
        return createCustomItemButton(slot, itemStack, (a, b) -> {}, action, (a) -> {});
    }

    /** Get the button from a specific slot.
     * @since 1.0
     * @param slot The desired slot.
     * @return Returns the instance of the button, otherwise returns null if no button is found at this slot.
     */
    public Button getButton(int slot) {
        if (slot < 1 || slot > this.inventorySize)
            return null;
        for (Button button : this.buttons)
            if (button.slot == slot)
                return button;
        return null;
    }

    /** Remove the button from a specific slot.
     * @since 1.0
     * @param slot The desired slot.
     * @return Returns true if the button was deleted, otherwise false if no button was found at this slot.
     */
    public boolean removeButton(int slot) {
        Button button = createButton(slot, null, null);

        return button != null && this.buttons.remove(button);
    }

    /** Builds the inventory according to the player.
     * @since 1.0
     * @param player The player to use for his point of view.
     * @param title The title that the inventory will have.
     * @return Returns the instance of the constructed inventory.
     */
    private Inventory buildInventory(HumanEntity player, String title) {
        Inventory result;
        ItemButton itemButton;

        if (this.inventorySize > 54 || this.inventorySize < 9 || this.inventorySize % 9 != 0)
            return null;
        if (title == null)
            result = Bukkit.createInventory(null, this.inventorySize);
        else
            result = Bukkit.createInventory(null, this.inventorySize, title);
        for (int i = 0; i < this.inventorySize; i++) {
            if (this.contents[i] != null)
                result.setItem(i, this.contents[i]);
            else
                result.setItem(i, this.fillItem);
        }
        for (Button button : this.buttons) {
            if (button instanceof ItemButton) {
                itemButton = (ItemButton) button;
                result.setItem(itemButton.slot, itemButton.getItemStack(player));
            }
        }
        return result;
    }

    /** Removes all inventory items except button items.
     * @since 1.0
     */
    public void clearItems() {
        this.fillItem = null;
        for (int i = 0; i < this.inventorySize; i++)
            this.contents[i] = null;
    }

    /** Removes all buttons from inventory.
     * @since 1.0
     */
    public void clearButtons() {
        this.buttons.clear();
    }

    /** Remove items and buttons from inventory.
     * @since 1.0
     */
    public void clear() {
        clearItems();
        clearButtons();
    }

    /** Open the inventory to the player and adapt the buttons to him.
     * @since 1.0
     * @param player The player to use for his point of view.
     * @param title The title that the inventory will have.
     * @return Returns true if the inventory opens, otherwise returns false on error.
     */
    public boolean openInventory(HumanEntity player, String title) {
        Inventory inventory;

        if (player == null)
            return false;
        if (this.viewers.contains(player)) {
            player.closeInventory();
            this.viewers.remove(player);
        }
        inventory = buildInventory(player, title);
        if (inventory == null)
            return false;
        player.openInventory(inventory);
        this.viewers.add(player);
        return true;
    }

    /** Notifies the inventory that the player is closing it.
     * @since 1.0
     * @param player The player who closes the inventory.
     * @return Returns true if the inventory is correctly closed, otherwise returns false if the player did not open the inventory or on error.
     */
    public boolean closeInventory(HumanEntity player) {
        if (player == null)
            return false;
        return this.viewers.remove(player);
    }

    /** Check if the inventory is open for the player you want.
     * @since 1.0
     * @param player The player to check.
     * @return Returns true if the inventory is currently open for the player, otherwise returns false.
     */
    public boolean isInventoryOpen(HumanEntity player) {
        return player != null && this.viewers.contains(player);
    }

    /** Execute the click of the player in the inventory on the desired slot.
     * @since 1.0
     * @param player The player who clicked.
     * @param slot The clicked slot.
     * @return Returns true if the inventory to perform the action, otherwise returns false.
     */
    public boolean playerExecute(InventoryClickEvent event, HumanEntity player, int slot) {
        Button defaultButton;

        if (player == null || !this.viewers.contains(player))
            return false;
        for (Button button : this.buttons) {
            if (button.slot == slot) {
                button.execute(event, player);
                return true;
            }
        }
        if (this.defaultButton == null)
            return true;
        defaultButton = new Button(this, slot, this.defaultButton, this.defaultButtonEvent) {
            @Override
            public void remove() {
                getFastInventory().defaultButton = null;
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(getFastInventory());
            }
        };
        defaultButton.execute(event, player);
        return true;
    }
}
