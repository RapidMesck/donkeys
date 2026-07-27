package com.nbp.donkeys.entity;

import com.nbp.donkeys.DonkeysMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, DonkeysMod.MODID);

    public static final Supplier<EntityType<CustomDonkeyEntity>> CUSTOM_DONKEY = ENTITIES.register("custom_donkey",
            () -> EntityType.Builder.of(CustomDonkeyEntity::new, MobCategory.CREATURE)
                    .sized(1.3964844f, 1.6f)
                    .clientTrackingRange(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, DonkeysMod.locate("custom_donkey"))));
}
