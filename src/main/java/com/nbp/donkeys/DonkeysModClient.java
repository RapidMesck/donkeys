package com.nbp.donkeys;

import com.nbp.donkeys.entity.ModEntities;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.DonkeyRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = DonkeysMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = DonkeysMod.MODID, value = Dist.CLIENT)
public class DonkeysModClient {
    public DonkeysModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        DonkeysMod.LOGGER.info("Donkeys client setup complete");
    }

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CUSTOM_DONKEY.get(),
                ctx -> new DonkeyRenderer<>(
                        ctx,
                        EquipmentClientInfo.LayerType.DONKEY_SADDLE,
                        ModelLayers.DONKEY_SADDLE,
                        DonkeyRenderer.Type.DONKEY,
                        DonkeyRenderer.Type.DONKEY_BABY
                ));
    }
}
