package com.nbp.donkeys.item.module;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.equine.Donkey;

public class FunctionalModule extends AbstractModule {
    private final FunctionalType type;

    public FunctionalModule(FunctionalType type) {
        this.type = type;
    }

    public FunctionalType getFunctionalType() {
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
        return true;
    }

    @Override
    public void interact(Player player, Donkey donkey, ItemStack moduleStack) {
        if (donkey.level() instanceof ServerLevel) {
            type.openMenu(player, moduleStack);
        }
    }

    public enum FunctionalType {
        CRAFTING_TABLE {
            @Override
            public void openMenu(Player player, ItemStack stack) {
                player.openMenu(new net.minecraft.world.MenuProvider() {
                    @Override
                    public net.minecraft.network.chat.Component getDisplayName() {
                        return stack.getHoverName();
                    }

                    @Override
                    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inv, Player p) {
                        return new net.minecraft.world.inventory.CraftingMenu(id, inv, net.minecraft.world.inventory.ContainerLevelAccess.create(player.level(), player.blockPosition()));
                    }
                });
            }
        },
        FURNACE {
            @Override
            public void openMenu(Player player, ItemStack stack) {
                player.openMenu(new net.minecraft.world.MenuProvider() {
                    @Override
                    public net.minecraft.network.chat.Component getDisplayName() {
                        return stack.getHoverName();
                    }

                    @Override
                    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inv, Player p) {
                        return new net.minecraft.world.inventory.FurnaceMenu(id, inv);
                    }
                });
            }
        },
        ANVIL {
            @Override
            public void openMenu(Player player, ItemStack stack) {
                player.openMenu(new net.minecraft.world.MenuProvider() {
                    @Override
                    public net.minecraft.network.chat.Component getDisplayName() {
                        return stack.getHoverName();
                    }

                    @Override
                    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inv, Player p) {
                        return new net.minecraft.world.inventory.AnvilMenu(id, inv, net.minecraft.world.inventory.ContainerLevelAccess.create(player.level(), player.blockPosition()));
                    }
                });
            }
        },
        BED {
            @Override
            public void openMenu(Player player, ItemStack stack) {
            }
        },
        ENDER_CHEST {
            @Override
            public void openMenu(Player player, ItemStack stack) {
                player.openMenu(new net.minecraft.world.MenuProvider() {
                    @Override
                    public net.minecraft.network.chat.Component getDisplayName() {
                        return net.minecraft.network.chat.Component.translatable("container.enderchest");
                    }

                    @Override
                    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inv, Player p) {
                        return net.minecraft.world.inventory.ChestMenu.threeRows(id, inv, p.getEnderChestInventory());
                    }
                });
            }
        },
        BREWING_STAND {
            @Override
            public void openMenu(Player player, ItemStack stack) {
                player.openMenu(new net.minecraft.world.MenuProvider() {
                    @Override
                    public net.minecraft.network.chat.Component getDisplayName() {
                        return stack.getHoverName();
                    }

                    @Override
                    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inv, Player p) {
                        return new net.minecraft.world.inventory.BrewingStandMenu(id, inv);
                    }
                });
            }
        };

        public abstract void openMenu(Player player, ItemStack stack);
    }
}
