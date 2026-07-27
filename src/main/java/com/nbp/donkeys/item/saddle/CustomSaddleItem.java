package com.nbp.donkeys.item.saddle;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CustomSaddleItem extends Item {
    public CustomSaddleItem(Properties properties) {
        super(properties);
    }

    public static float getSpeed(ItemStack stack) {
        return stack.getOrDefault(SaddleDataComponents.SPEED, 0f);
    }

    public static float getJump(ItemStack stack) {
        return stack.getOrDefault(SaddleDataComponents.JUMP, 0f);
    }

    public static float getHealth(ItemStack stack) {
        return stack.getOrDefault(SaddleDataComponents.HEALTH, 0f);
    }

    public static float getArmor(ItemStack stack) {
        return stack.getOrDefault(SaddleDataComponents.ARMOR, 0f);
    }

    public static float getStepHeight(ItemStack stack) {
        return stack.getOrDefault(SaddleDataComponents.STEP_HEIGHT, 0f);
    }

    public static float getSwimSpeed(ItemStack stack) {
        return stack.getOrDefault(SaddleDataComponents.SWIM_SPEED, 0f);
    }

    public static float getKnockbackResist(ItemStack stack) {
        return stack.getOrDefault(SaddleDataComponents.KNOCKBACK_RESIST, 0f);
    }

    public static int getLootBonus(ItemStack stack) {
        return stack.getOrDefault(SaddleDataComponents.LOOT_BONUS, 0);
    }
}
