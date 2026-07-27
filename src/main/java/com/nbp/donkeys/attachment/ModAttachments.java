package com.nbp.donkeys.attachment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nbp.donkeys.DonkeysMod;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DonkeysMod.MODID);

    private static final MapCodec<ModuleSlots> MODULE_SLOTS_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.optionalFieldOf("left", ItemStack.EMPTY).forGetter(ModuleSlots::getLeft),
            ItemStack.CODEC.optionalFieldOf("right", ItemStack.EMPTY).forGetter(ModuleSlots::getRight)
    ).apply(instance, ModuleSlots::new));

    public static final Supplier<AttachmentType<ModuleSlots>> MODULE_SLOTS =
            ATTACHMENT_TYPES.register("module_slots", () -> AttachmentType.builder(ModuleSlots::new)
                    .serialize(MODULE_SLOTS_CODEC)
                    .build());
}
