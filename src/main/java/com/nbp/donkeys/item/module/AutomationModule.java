package com.nbp.donkeys.item.module;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.equine.Donkey;

public class AutomationModule extends AbstractModule {
    public static final String COLLECTOR = "collector";

    private final String type;

    public AutomationModule(String type) {
        this.type = type;
    }

    public String getAutomationType() {
        return type;
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
        return false;
    }

    @Override
    public void interact(Player player, Donkey donkey, ItemStack moduleStack) {
    }
}
