package org.bsdevelopment.mobfarming;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.client.ModelHandler;
import org.bsdevelopment.mobfarming.client.screen.MobBlenderScreen;
import org.bsdevelopment.mobfarming.config.Config;
import org.bsdevelopment.mobfarming.container.ModContainers;
import org.bsdevelopment.mobfarming.items.ModItems;
import org.bsdevelopment.mobfarming.listeners.BlenderDeathListener;
import org.bsdevelopment.mobfarming.listeners.SpikeDeathListener;
import org.bsdevelopment.mobfarming.utilities.EnergyIntegration;
import org.slf4j.Logger;

@Mod(ModConstants.MOD_ID)
public class EfficientMobFarming {
    private static final Logger LOGGER = LogUtils.getLogger();

    public EfficientMobFarming(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, ModConstants.CONFIG_DIR.resolve("config.toml").toString());
        NeoForge.EVENT_BUS.register(this);

        ModComponents.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModContainers.register(modEventBus);

        modEventBus.register(EnergyIntegration.class);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModelHandler.register(modEventBus);
        }

        NeoForge.EVENT_BUS.register(new BlenderDeathListener());
        NeoForge.EVENT_BUS.register(new SpikeDeathListener());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    @EventBusSubscriber(modid = ModConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

        }


        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModContainers.MOB_BLENDER.get(), MobBlenderScreen::new);
        }

    }
}
