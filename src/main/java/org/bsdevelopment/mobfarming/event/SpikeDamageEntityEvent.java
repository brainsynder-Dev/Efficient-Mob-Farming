package org.bsdevelopment.mobfarming.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import org.bsdevelopment.mobfarming.blocks.BlockMobSpike;

public class SpikeDamageEntityEvent extends LivingEvent {
    private final BlockMobSpike spike;
    private final BlockPos spikePosition;

    public SpikeDamageEntityEvent(LivingEntity entity, BlockMobSpike spike, BlockPos spikePosition) {
        super(entity);
        this.spike = spike;
        this.spikePosition = spikePosition;
    }

    public BlockPos getSpikePosition() {
        return spikePosition;
    }

    public BlockMobSpike getMobSpike() {
        return spike;
    }
}
