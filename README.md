# FastGUI Library

## What is its use ?
**Allows you to easily create menus.**

## How to use it ?
### First of all, you need to integrate it into your plugin. You can do this using:

#### Maven:
Repository:
```xml
<repositories>
    <repository>
        <id>mrcubee-minecraft</id>
        <url>http://nexus.mrcubee.net/repository/minecraft/</url>
    </repository>
</repositories>
```
Dependency:
```xml
<dependencies>  
  <dependency>
    <groupId>fr.mrcubee.minecraft.library</groupId>  
    <artifactId>fastgui</artifactId>  
    <version>1.0</version>  
    <scope>compile</scope>  
  </dependency>
 </dependencies>
```
#### Gradle:
Repository:
```groovy
repositories {
    maven {
        url "http://nexus.mrcubee.net/repository/minecraft/"
    }
}
```
Dependency:
```groovy
dependencies {
    compile 'fr.mrcubee.minecraft.library:fastgui:1.0'
}
```
### Use in the plugin.
#### Example:
```java
public class TestPlugin extends JavaPlugin {

    private FastInventory fastInventory;

    @Override
    public void onEnable() {
        
        // Items you want to use
        ItemStack opItem = new ItemStack(Material.GOLDEN_APPLE);
        ItemStack deopItem = new ItemStack(Material.BARRIER);
        ItemStack exampleItem = new ItemStack(Material.GRASS);
        ItemStack fillItem = new ItemStack(Material.APPLE);

        // Create an inventory with 1 single line (composed of 9 * 1 slots)
        this.fastInventory = FastGUI.createInventory(1);
        
        // Checks if there is an error during the creation of the inventory.
        if (this.fastInventory == null)
            return;
        
        // Create an item button for which, when the player clicks on it, it puts them as operator.
        this.fastInventory.createItemButton(0, opItem, (button, player) ->{
            player.sendMessage(ChatColor.GREEN + "Vous êtes op !");
            player.setOp(true);
            player.closeInventory();
        });
        
        // Create an item button for which when the player clicks it removes them as an operator.
        this.fastInventory.createItemButton(8, deopItem, ((button, player) -> {
            player.sendMessage(ChatColor.GREEN + "Vous êtes deop !");
            player.setOp(true);
            player.closeInventory();
        }));
        
        // When the player clicks on another slot it displays an error message.
        this.fastInventory.createDefaultButton((button, player) -> {
            player.sendMessage(ChatColor.RED + "No button in slot N°" + button.slot + ".");
        });
        
        //Create item and use event directly here
        this.fastInventory.createCustomItemButton(10, exampleItem, (event) -> {
            //Use InventoryClickEvent here
        });
        
        //Use system of action, used if item is clicked
        this.fastInventory.createCustomItemButton(11, exampleItem, (button, player) -> {
            //use action on click on item
            player.sendMessage(ChatColor.RED + "Used!");
        });

        // Fill in the free slots in the first row with the desired item.
        this.fastInventory.setRow(0, fillItem);

        // Fill in the empty slots in the second column with the desired item.
        this.fastInventory.setColumn(1, fillItem);

        // Fill in the rest of the free slots with a default item.
        this.fastInventory.fillInventory(fillItem);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        // Check if the inventory is created.
        if (this.fastInventory != null) {
            // Open the inventory to the player.

            this.fastInventory.openInventory((HumanEntity) sender, "Inventory Name");
        }
        return true;
    }
}
```