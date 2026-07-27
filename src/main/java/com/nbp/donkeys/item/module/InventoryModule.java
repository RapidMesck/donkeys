package com.nbp.donkeys.item.module;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.equine.Donkey;

public class InventoryModule extends AbstractModule {
    private final int slots;

    public InventoryModule(int slots) {
        this.slots = slots;
    }

    public int getSlots() {
        return slots;
    }

    public int getRows() {
        return slots / 9;
    }

    @Override
    public void onEquip(Donkey donkey, ItemStack moduleStack) {
    }

    @Override
    public void onUnequip(Donkey donkey, ItemStack moduleStack) {
    }

    @Override
    public void onTick(Donkey donkey, ItemStack moduleStack) {
    }

    @Override
    public boolean canInteract(Player player, Donkey donkey, ItemStack moduleStack) {
        return true;
    }

    @Override
    public void interact(Player player, Donkey donkey, ItemStack moduleStack) {
    }
}
