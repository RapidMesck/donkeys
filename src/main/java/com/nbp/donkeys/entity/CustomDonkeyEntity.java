package com.nbp.donkeys.entity;

import com.nbp.donkeys.Config;
import com.nbp.donkeys.DonkeysMod;
import com.nbp.donkeys.attachment.ModAttachments;
import com.nbp.donkeys.attachment.ModuleSlots;
import com.nbp.donkeys.item.module.ModuleRegistry;
import com.nbp.donkeys.item.saddle.SaddleDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CustomDonkeyEntity extends Donkey {

    private int collectorTickCounter = 0;

    public CustomDonkeyEntity(EntityType<? extends CustomDonkeyEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (!heldItem.isEmpty() && ModuleRegistry.isModuleItem(heldItem)) {
            if (level() instanceof ServerLevel) {
                ModuleSlots.Side side = getNearestSide(player);
                var opt = getExistingData(ModAttachments.MODULE_SLOTS);
                ModuleSlots slots = opt.orElse(new ModuleSlots());
                if (slots.hasModule(side)) {
                    ItemStack old = slots.removeModule(side);
                    player.getInventory().placeItemBackInInventory(old);
                }
                slots.setModule(side, heldItem.split(1));
                setData(ModAttachments.MODULE_SLOTS, slots);
            }
            return InteractionResult.SUCCESS;
        }

        if (!heldItem.isEmpty() && heldItem.is(DonkeysMod.CUSTOM_SADDLE.get()) && !this.isWearingBodyArmor()) {
            if (level() instanceof ServerLevel) {
                this.equipBodyArmor(player, heldItem);
            }
            return InteractionResult.SUCCESS;
        }

        if (heldItem.isEmpty() && player.isShiftKeyDown()) {
            return handleModuleInteraction(player);
        }

        return super.mobInteract(player, hand);
    }

    private InteractionResult handleModuleInteraction(Player player) {
        var opt = getExistingData(ModAttachments.MODULE_SLOTS);
        if (opt.isEmpty() || opt.get().isEmpty()) {
            return super.mobInteract(player, InteractionHand.MAIN_HAND);
        }

        ModuleSlots slots = opt.get();
        ModuleSlots.Side side = getNearestSide(player);
        ItemStack module = slots.getModule(side);
        if (module.isEmpty()) {
            side = side == ModuleSlots.Side.LEFT ? ModuleSlots.Side.RIGHT : ModuleSlots.Side.LEFT;
            module = slots.getModule(side);
        }

        if (module.isEmpty()) {
            return super.mobInteract(player, InteractionHand.MAIN_HAND);
        }

        if (level() instanceof ServerLevel) {
            ModuleRegistry.ModuleType type = ModuleRegistry.getType(module);
            if (type == ModuleRegistry.ModuleType.INVENTORY) {
                openChestInventory(player, side);
            } else if (type == ModuleRegistry.ModuleType.FUNCTIONAL) {
                ModuleRegistry.openFunctionalGui(player, module);
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void openChestInventory(Player player, ModuleSlots.Side side) {
        var opt = getExistingData(ModAttachments.MODULE_SLOTS);
        if (opt.isEmpty()) return;
        ModuleSlots slots = opt.get();
        ItemStack module = slots.getModule(side);
        int rows = ModuleRegistry.getInventoryRows(module);

        SimpleContainer inventory = new SimpleContainer(rows * 9);
        var contents = module.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        contents.copyInto(inventory.getItems());

        Component title = module.getHoverName();

        player.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return title;
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                return new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x4,
                        containerId, playerInventory, inventory, rows) {
                    @Override
                    public boolean stillValid(Player p) {
                        return CustomDonkeyEntity.this.isAlive()
                                && CustomDonkeyEntity.this.distanceToSqr(p) < 64.0;
                    }

                    @Override
                    public void removed(Player p) {
                        super.removed(p);
                        saveInventoryToModule(side, inventory);
                    }
                };
            }
        });

        playSound(SoundEvents.DONKEY_CHEST, 1.0f, 1.0f);
    }

    private void saveInventoryToModule(ModuleSlots.Side side, SimpleContainer inventory) {
        var opt = getExistingData(ModAttachments.MODULE_SLOTS);
        if (opt.isEmpty()) return;
        ModuleSlots slots = opt.get();
        ItemStack module = slots.getModule(side);
        if (!module.isEmpty()) {
            module.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(inventory.getItems()));
            slots.setModule(side, module);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel) {
            tickCollector();
            applySaddleAttributes();
        }
    }

    private void tickCollector() {
        collectorTickCounter++;
        int tickRate = Config.COLLECTOR_TICK_RATE.get();
        if (collectorTickCounter < tickRate) return;
        collectorTickCounter = 0;

        var opt = getExistingData(ModAttachments.MODULE_SLOTS);
        if (opt.isEmpty()) return;
        ModuleSlots slots = opt.get();
        if (!ModuleRegistry.hasCollectorModule(slots)) return;

        int radius = Config.COLLECTOR_RADIUS.get();
        int maxItems = Config.COLLECTOR_ITEMS_PER_SWEEP.get();

        List<ItemEntity> items = level().getEntitiesOfClass(
                ItemEntity.class,
                this.getBoundingBox().inflate(radius),
                e -> e.isAlive() && !e.getItem().isEmpty());

        int collected = 0;
        for (ItemEntity itemEntity : items) {
            if (collected >= maxItems) break;
            ItemStack stack = itemEntity.getItem();
            ItemStack remainder = tryInsertIntoModules(slots, stack);
            int pickedUp = stack.getCount() - remainder.getCount();
            if (remainder.isEmpty()) {
                itemEntity.discard();
                collected += stack.getCount();
            } else if (pickedUp > 0) {
                itemEntity.setItem(remainder);
                collected += pickedUp;
            }
        }
    }

    private ItemStack tryInsertIntoModules(ModuleSlots slots, ItemStack stack) {
        ItemStack remaining = stack.copy();
        remaining = tryInsertIntoModule(slots, ModuleSlots.Side.LEFT, remaining);
        if (!remaining.isEmpty()) {
            remaining = tryInsertIntoModule(slots, ModuleSlots.Side.RIGHT, remaining);
        }
        return remaining;
    }

    private ItemStack tryInsertIntoModule(ModuleSlots slots, ModuleSlots.Side side, ItemStack stack) {
        ItemStack module = slots.getModule(side);
        if (module.isEmpty() || ModuleRegistry.getType(module) != ModuleRegistry.ModuleType.INVENTORY) {
            return stack;
        }

        var contents = module.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        NonNullList<ItemStack> items = NonNullList.withSize(contents.getSlots(), ItemStack.EMPTY);
        contents.copyInto(items);

        ItemStack remaining = stack.copy();
        for (int i = 0; i < items.size() && !remaining.isEmpty(); i++) {
            ItemStack slotStack = items.get(i).copy();
            if (ItemStack.isSameItemSameComponents(slotStack, remaining)) {
                int transfer = Math.min(slotStack.getMaxStackSize() - slotStack.getCount(), remaining.getCount());
                if (transfer > 0) {
                    slotStack.grow(transfer);
                    remaining.shrink(transfer);
                    items.set(i, slotStack);
                }
            }
        }
        for (int i = 0; i < items.size() && !remaining.isEmpty(); i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, remaining.copy());
                remaining = ItemStack.EMPTY;
            }
        }

        module.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
        slots.setModule(side, module);
        return remaining;
    }

    private void applySaddleAttributes() {
        ItemStack bodyItem = this.getItemBySlot(EquipmentSlot.BODY);
        if (bodyItem.isEmpty() || !bodyItem.is(DonkeysMod.CUSTOM_SADDLE.get())) {
            return;
        }

        var comps = bodyItem.getComponents();
        applyModifier(Attributes.MOVEMENT_SPEED, comps.getOrDefault(SaddleDataComponents.SPEED, 0f) * 0.175f);
        applyModifier(Attributes.JUMP_STRENGTH, comps.getOrDefault(SaddleDataComponents.JUMP, 0f) * 0.1f);
        applyModifier(Attributes.MAX_HEALTH, comps.getOrDefault(SaddleDataComponents.HEALTH, 0f));
        applyModifier(Attributes.ARMOR, comps.getOrDefault(SaddleDataComponents.ARMOR, 0f) * 0.3f);
        applyModifier(Attributes.KNOCKBACK_RESISTANCE, comps.getOrDefault(SaddleDataComponents.KNOCKBACK_RESIST, 0f) * 0.01f);
    }

    private void applyModifier(Holder<Attribute> attribute, double value) {
        var instance = this.getAttribute(attribute);
        if (instance != null && value != 0) {
            instance.removeModifier(DonkeysMod.locate("saddle_bonus"));
            instance.addPermanentModifier(new AttributeModifier(DonkeysMod.locate("saddle_bonus"), value, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    private ModuleSlots.Side getNearestSide(Player player) {
        Vec3 look = this.position().vectorTo(player.position());
        Vec3 forward = this.getLookAngle();
        double cross = look.x * forward.z - look.z * forward.x;
        return cross > 0 ? ModuleSlots.Side.LEFT : ModuleSlots.Side.RIGHT;
    }

    public static void convert(Donkey vanilla, Player player, ItemStack conversionItem) {
        ServerLevel level = (ServerLevel) vanilla.level();
        var spawnPos = vanilla.blockPosition();
        CustomDonkeyEntity custom = ModEntities.CUSTOM_DONKEY.get().create(level, null, spawnPos,
                net.minecraft.world.entity.EntitySpawnReason.COMMAND, false, false);
        if (custom == null) return;

        custom.copyPosition(vanilla);
        custom.setYRot(vanilla.getYRot());
        custom.setXRot(vanilla.getXRot());
        custom.yBodyRot = vanilla.yBodyRot;
        custom.setYHeadRot(vanilla.getYHeadRot());
        custom.setDeltaMovement(vanilla.getDeltaMovement());
        custom.xpReward = vanilla.getExperienceReward(level, null);

        custom.setTamed(vanilla.isTamed());

        custom.setHealth(Math.max(1f, vanilla.getHealth()));
        if (vanilla.hasCustomName()) {
            custom.setCustomName(vanilla.getCustomName());
            custom.setCustomNameVisible(vanilla.isCustomNameVisible());
        }

        vanilla.discard();
        level.addFreshEntity(custom);

        if (!player.getAbilities().instabuild) {
            conversionItem.shrink(1);
        }
    }
}
