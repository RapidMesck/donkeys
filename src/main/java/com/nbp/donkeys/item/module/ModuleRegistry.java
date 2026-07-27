package com.nbp.donkeys.item.module;

import com.nbp.donkeys.DonkeysMod;
import com.nbp.donkeys.attachment.ModuleSlots;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {
    public enum ModuleType {
        INVENTORY,
        FUNCTIONAL,
        AUTOMATION
    }

    private static final Map<Item, ModuleType> ITEM_TO_TYPE = new HashMap<>();
    private static final Map<Item, InventoryModule> INVENTORY_MODULES = new HashMap<>();
    private static final Map<Item, FunctionalModule> FUNCTIONAL_MODULES = new HashMap<>();
    private static final Map<Item, AutomationModule> AUTOMATION_MODULES = new HashMap<>();

    public static void init() {
        registerInventory(Items.CHEST, 27);
        registerInventory(DonkeysMod.IRON_CHEST_MODULE.get(), 45);
        registerInventory(DonkeysMod.GOLD_CHEST_MODULE.get(), 63);
        registerInventory(DonkeysMod.DIAMOND_CHEST_MODULE.get(), 81);

        registerFunctional(Items.CRAFTING_TABLE, FunctionalModule.FunctionalType.CRAFTING_TABLE);
        registerFunctional(Items.FURNACE, FunctionalModule.FunctionalType.FURNACE);
        registerFunctional(Items.ANVIL, FunctionalModule.FunctionalType.ANVIL);
        registerFunctional(Items.WHITE_BED, FunctionalModule.FunctionalType.BED);
        registerFunctional(Items.ENDER_CHEST, FunctionalModule.FunctionalType.ENDER_CHEST);
        registerFunctional(Items.BREWING_STAND, FunctionalModule.FunctionalType.BREWING_STAND);

        registerAutomation(DonkeysMod.COLLECTOR_MODULE.get(), AutomationModule.COLLECTOR);
    }

    private static void registerInventory(Item item, int slots) {
        ITEM_TO_TYPE.put(item, ModuleType.INVENTORY);
        INVENTORY_MODULES.put(item, new InventoryModule(slots));
    }

    private static void registerFunctional(Item item, FunctionalModule.FunctionalType type) {
        ITEM_TO_TYPE.put(item, ModuleType.FUNCTIONAL);
        FUNCTIONAL_MODULES.put(item, new FunctionalModule(type));
    }

    private static void registerAutomation(Item item, String type) {
        ITEM_TO_TYPE.put(item, ModuleType.AUTOMATION);
        AUTOMATION_MODULES.put(item, new AutomationModule(type));
    }

    public static boolean isModuleItem(ItemStack stack) {
        return ITEM_TO_TYPE.containsKey(stack.getItem());
    }

    public static ModuleType getType(ItemStack stack) {
        return ITEM_TO_TYPE.getOrDefault(stack.getItem(), null);
    }

    public static int getInventoryRows(ItemStack stack) {
        InventoryModule module = INVENTORY_MODULES.get(stack.getItem());
        return module != null ? module.getRows() : 3;
    }

    public static boolean hasCollectorModule(ModuleSlots slots) {
        return isCollectorItem(slots.getLeft()) || isCollectorItem(slots.getRight());
    }

    private static boolean isCollectorItem(ItemStack stack) {
        return !stack.isEmpty() && getType(stack) == ModuleType.AUTOMATION;
    }

    public static void openFunctionalGui(Player player, ItemStack module) {
        FunctionalModule moduleObj = FUNCTIONAL_MODULES.get(module.getItem());
        if (moduleObj != null) {
            moduleObj.interact(player, null, module);
        }
    }

    public static AbstractModule getModule(ItemStack stack) {
        ModuleType type = getType(stack);
        if (type == null) return null;
        return switch (type) {
            case INVENTORY -> INVENTORY_MODULES.get(stack.getItem());
            case FUNCTIONAL -> FUNCTIONAL_MODULES.get(stack.getItem());
            case AUTOMATION -> AUTOMATION_MODULES.get(stack.getItem());
        };
    }
}
