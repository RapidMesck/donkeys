package com.nbp.donkeys;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue COLLECTOR_RADIUS = BUILDER
            .comment("Radius in blocks for the collector module to pick up items")
            .defineInRange("collectorRadius", 5, 1, 32);

    public static final ModConfigSpec.IntValue COLLECTOR_TICK_RATE = BUILDER
            .comment("Ticks between collector sweeps")
            .defineInRange("collectorTickRate", 20, 5, 100);

    public static final ModConfigSpec.IntValue COLLECTOR_ITEMS_PER_SWEEP = BUILDER
            .comment("Max items collected per sweep by the collector module")
            .defineInRange("collectorItemsPerSweep", 32, 1, 128);

    static final ModConfigSpec SPEC = BUILDER.build();
}
