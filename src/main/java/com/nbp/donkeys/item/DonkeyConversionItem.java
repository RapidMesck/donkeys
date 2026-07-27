package com.nbp.donkeys.item;

import com.nbp.donkeys.DonkeysMod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DonkeyConversionItem extends Item {
    public DonkeyConversionItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof Donkey donkey && donkey.isTamed()) {
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }
}
