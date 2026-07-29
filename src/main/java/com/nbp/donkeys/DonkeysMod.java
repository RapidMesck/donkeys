package com.nbp.donkeys;

import com.mojang.logging.LogUtils;
import com.nbp.donkeys.attachment.ModAttachments;
import com.nbp.donkeys.entity.ModEntities;
import com.nbp.donkeys.item.module.ModuleRegistry;
import com.nbp.donkeys.item.saddle.SaddleDataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(DonkeysMod.MODID)
public class DonkeysMod {
    public static final String MODID = "donkeys";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Identifier locate(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<Item> GALA_APPLE = ITEMS.registerSimpleItem("gala_apple",
            p -> p.food(new net.minecraft.world.food.FoodProperties.Builder().nutrition(4).saturationModifier(1.2f).alwaysEdible().build()).stacksTo(16));

    public static final DeferredItem<Item> IRON_CHEST_MODULE = ITEMS.registerSimpleItem("iron_chest_module");
    public static final DeferredItem<Item> GOLD_CHEST_MODULE = ITEMS.registerSimpleItem("gold_chest_module");
    public static final DeferredItem<Item> DIAMOND_CHEST_MODULE = ITEMS.registerSimpleItem("diamond_chest_module");
    public static final DeferredItem<Item> COLLECTOR_MODULE = ITEMS.registerSimpleItem("collector_module");
    public static final DeferredItem<Item> CUSTOM_SADDLE = ITEMS.registerSimpleItem("custom_saddle", p -> p.stacksTo(1));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DONKEYS_TAB = CREATIVE_MODE_TABS.register("donkeys_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.donkeys"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> GALA_APPLE.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(GALA_APPLE.get());
                        output.accept(IRON_CHEST_MODULE.get());
                        output.accept(GOLD_CHEST_MODULE.get());
                        output.accept(DIAMOND_CHEST_MODULE.get());
                        output.accept(COLLECTOR_MODULE.get());
                        output.accept(CUSTOM_SADDLE.get());
                    }).build());

    public DonkeysMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerAttributes);

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        SaddleDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        ModuleRegistry.init();
        LOGGER.info("Donkeys mod common setup complete");
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CUSTOM_DONKEY.get(),
                AbstractHorse.createBaseHorseAttributes().build());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Donkeys mod server starting");
    }
}
