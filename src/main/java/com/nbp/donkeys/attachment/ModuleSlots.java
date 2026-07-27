package com.nbp.donkeys.attachment;

import net.minecraft.world.item.ItemStack;

public class ModuleSlots {
    private ItemStack left;
    private ItemStack right;

    public ModuleSlots() {
        this(ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public ModuleSlots(ItemStack left, ItemStack right) {
        this.left = left;
        this.right = right;
    }

    public ItemStack getLeft() {
        return left;
    }

    public ItemStack getRight() {
        return right;
    }

    public void setLeft(ItemStack stack) {
        this.left = stack;
    }

    public void setRight(ItemStack stack) {
        this.right = stack;
    }

    public ItemStack removeLeft() {
        var s = this.left;
        this.left = ItemStack.EMPTY;
        return s;
    }

    public ItemStack removeRight() {
        var s = this.right;
        this.right = ItemStack.EMPTY;
        return s;
    }

    public ItemStack getModule(Side side) {
        return side == Side.LEFT ? left : right;
    }

    public void setModule(Side side, ItemStack stack) {
        if (side == Side.LEFT) {
            this.left = stack;
        } else {
            this.right = stack;
        }
    }

    public ItemStack removeModule(Side side) {
        if (side == Side.LEFT) {
            return removeLeft();
        }
        return removeRight();
    }

    public boolean hasModule(Side side) {
        return !getModule(side).isEmpty();
    }

    public boolean isEmpty() {
        return left.isEmpty() && right.isEmpty();
    }

    public enum Side {
        LEFT,
        RIGHT
    }
}
