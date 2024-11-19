package org.bsdevelopment.mobfarming.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.entity.MobSpikeBlockEntity;
import org.bsdevelopment.mobfarming.event.SpikeDamageEntityEvent;

public class SpikeDeathListener {
    private final String SPIKE_POSITION_KEY = ModConstants.MOD_ID+".spike_position";

    @SubscribeEvent
    public void onSpikeDamage(SpikeDamageEntityEvent event) {
        if (event.getEntity().getPersistentData().contains(SPIKE_POSITION_KEY)) return;
        event.getEntity().getPersistentData().put(SPIKE_POSITION_KEY, BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, event.getSpikePosition()).getOrThrow());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onEntityDeath(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getPersistentData().contains(SPIKE_POSITION_KEY)) {
            BlockPos blockPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, entity.getPersistentData().get(SPIKE_POSITION_KEY)).getOrThrow().getFirst();
            handleItemDrop(event, event.getEntity().level(), blockPos);
        }
    }


    public void handleItemDrop(LivingDropsEvent event, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity == null) return;
        if (!(blockEntity instanceof MobSpikeBlockEntity spikeBlock)) return;

        for (ItemEntity drop : event.getDrops()) {
            spikeBlock.getBulkStorage().addItem(drop.getItem());
        }

        event.getDrops().clear();
        event.setCanceled(true);
        spikeBlock.setChanged();
    }


    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onEntityDeath(LivingExperienceDropEvent event) {
        LivingEntity entity = event.getEntity();

        // If the entity was killed by a Mob Spike, we don't want to drop experience... as per design
        if (entity.getPersistentData().contains(SPIKE_POSITION_KEY)) {
            event.setDroppedExperience(0);
            event.setCanceled(true);
        }
    }
}
