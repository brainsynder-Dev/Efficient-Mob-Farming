package org.bsdevelopment.mobfarming.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.blocks.entity.base.DataSaverBlockEntity;
import org.bsdevelopment.mobfarming.blocks.entity.base.ILinkable;

public class WarpPlateBlockEntity extends DataSaverBlockEntity implements ILinkable {
    private BlockPos destinationPos = null;

    public WarpPlateBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.WARP_PLATE.getTileEntityType(), pPos, pBlockState);
    }

    public void teleportEntity (Entity entity) {
        if (destinationPos == null) return;
        if (!level.getBlockState(destinationPos).is(ModBlocks.DESTINATION_PLATE.getBlock())) return;

        entity.teleportTo(destinationPos.getX(), destinationPos.getY(), destinationPos.getZ());
         level.gameEvent(GameEvent.TELEPORT, destinationPos, GameEvent.Context.of(entity));
         if (entity instanceof Player) level.playSound(null, destinationPos, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 0.7F, 0.5F);
    }

    @Override
    public void linkPosition(BlockPos pos) {
        destinationPos = pos;
        setChanged();
    }

    @Override
    public boolean isLinked() {
        return destinationPos != null;
    }

    @Override
    public void loadData(CompoundTag tag, HolderLookup.Provider registries) {
        if (!tag.contains("bound_destination")) return;
        CompoundTag compoundTag = tag.getCompound("bound_destination");
        destinationPos = new BlockPos(compoundTag.getInt("x"), compoundTag.getInt("y"), compoundTag.getInt("z"));
    }

    @Override
    public void saveData(CompoundTag tag, HolderLookup.Provider registries) {
        if (destinationPos == null) return;

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("x", destinationPos.getX());
        compoundTag.putInt("y", destinationPos.getY());
        compoundTag.putInt("z", destinationPos.getZ());
        tag.put("bound_destination", compoundTag);
    }
}
