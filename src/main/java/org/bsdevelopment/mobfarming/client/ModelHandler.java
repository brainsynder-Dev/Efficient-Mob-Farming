package org.bsdevelopment.mobfarming.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.client.render.MobBlenderRender;

public class ModelHandler {
    public static void register(IEventBus eventBus) {
        eventBus.<EntityRenderersEvent.RegisterRenderers>addListener(registerRenderers -> {
            registerRenderers.registerBlockEntityRenderer(ModBlocks.MOB_BLENDER.getTileEntityType(), MobBlenderRender::new);
        });
    }
}
