package org.bsdevelopment.mobfarming.hooks.jade;

import org.bsdevelopment.mobfarming.blocks.BlockMobBlender;
import org.bsdevelopment.mobfarming.blocks.BlockMobSpike;
import org.bsdevelopment.mobfarming.hooks.jade.blender.MobBlenderProvider;
import org.bsdevelopment.mobfarming.hooks.jade.blender.MobSpikeProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeHook implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(MobBlenderProvider.INSTANCE, BlockMobBlender.class);
        registration.registerBlockComponent(MobSpikeProvider.INSTANCE, BlockMobSpike.class);
    }


    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(MobBlenderProvider.INSTANCE, BlockMobBlender.class);
        registration.registerBlockDataProvider(MobSpikeProvider.INSTANCE, BlockMobSpike.class);
    }
}
