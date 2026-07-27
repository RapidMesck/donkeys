package com.nbp.donkeys.item.module;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.equine.Donkey;

public abstract class AbstractModule {

    public abstract void onEquip(Donkey donkey, ItemStack moduleStack);

    public abstract void onUnequip(Donkey donkey, ItemStack moduleStack);

    public abstract void onTick(Donkey donkey, ItemStack moduleStack);

    public abstract boolean canInteract(Player player, Donkey donkey, ItemStack moduleStack);

    public abstract void interact(Player player, Donkey donkey, ItemStack moduleStack);
}
