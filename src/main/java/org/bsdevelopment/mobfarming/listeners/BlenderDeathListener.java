package org.bsdevelopment.mobfarming.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.entity.MobBlenderBase;
import org.bsdevelopment.mobfarming.utilities.fakeplayer.CustomFakePlayer;

public class BlenderDeathListener {

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onEntityDamage(LivingDamageEvent.Pre event) {
        Entity killer = event.getSource().getDirectEntity();
        if (killer == null) return;

        // Was killed by the mob blender
        if ((killer instanceof CustomFakePlayer fakePlayer) && fakePlayer.getPersistentData().contains(ModConstants.MOD_ID)) {
            event.getEntity().getPersistentData().put(ModConstants.MOD_ID+".killed_by_blender", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, fakePlayer.blockPosition()).getOrThrow());
        }
    }


    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onEntityDeath(LivingDropsEvent event) {
        Entity killer = event.getSource().getDirectEntity();
        if ((killer instanceof CustomFakePlayer fakePlayer) && fakePlayer.getPersistentData().contains(ModConstants.MOD_ID)) {
            handleItemDrop(event, fakePlayer.level(), fakePlayer.blockPosition());
            return;
        }

        LivingEntity entity = event.getEntity();
        if (entity.getPersistentData().contains(ModConstants.MOD_ID+".killed_by_blender")) {
            BlockPos blenderPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, entity.getPersistentData().get(ModConstants.MOD_ID+".killed_by_blender")).getOrThrow().getFirst();
            handleItemDrop(event, event.getEntity().level(), blenderPos);
        }
    }


    public void handleItemDrop(LivingDropsEvent event, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity == null) return;
        if (!(blockEntity instanceof MobBlenderBase mobBlender)) return;

        for (ItemEntity drop : event.getDrops()) {
            mobBlender.getBulkStorage().addItem(drop.getItem());
        }

        event.getDrops().clear();
        event.setCanceled(true);
        mobBlender.setChanged();
    }

    public void handleExperienceDrop(LivingExperienceDropEvent event, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity == null) return;
        if (!(blockEntity instanceof MobBlenderBase mobBlender)) return;

        mobBlender.addStoredExperience(event.getDroppedExperience());

        event.setCanceled(true);
    }


    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onEntityDeath(LivingExperienceDropEvent event) {
        Player killer = event.getAttackingPlayer();
        if ((killer instanceof CustomFakePlayer fakePlayer) && fakePlayer.getPersistentData().contains(ModConstants.MOD_ID)) {
            handleExperienceDrop(event, fakePlayer.level(), fakePlayer.blockPosition());
            return;
        }

        LivingEntity entity = event.getEntity();
        if (entity.getPersistentData().contains(ModConstants.MOD_ID+".killed_by_blender")) {
            BlockPos blenderPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, entity.getPersistentData().get(ModConstants.MOD_ID+".killed_by_blender")).getOrThrow().getFirst();
            handleExperienceDrop(event, event.getEntity().level(), blenderPos);
        }
    }
}
