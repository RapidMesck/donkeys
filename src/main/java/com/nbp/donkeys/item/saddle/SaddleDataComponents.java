package com.nbp.donkeys.item.saddle;

import com.mojang.serialization.Codec;
import com.nbp.donkeys.DonkeysMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SaddleDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, DonkeysMod.MODID);

    public static final Supplier<DataComponentType<Float>> SPEED =
            register("saddle_speed", b -> b.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

    public static final Supplier<DataComponentType<Float>> JUMP =
            register("saddle_jump", b -> b.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

    public static final Supplier<DataComponentType<Float>> HEALTH =
            register("saddle_health", b -> b.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

    public static final Supplier<DataComponentType<Float>> ARMOR =
            register("saddle_armor", b -> b.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

    public static final Supplier<DataComponentType<Float>> STEP_HEIGHT =
            register("saddle_step_height", b -> b.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

    public static final Supplier<DataComponentType<Float>> SWIM_SPEED =
            register("saddle_swim_speed", b -> b.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

    public static final Supplier<DataComponentType<Float>> KNOCKBACK_RESIST =
            register("saddle_knockback_resist", b -> b.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

    public static final Supplier<DataComponentType<Integer>> LOOT_BONUS =
            register("saddle_loot_bonus", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }
}
