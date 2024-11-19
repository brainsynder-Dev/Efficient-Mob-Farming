package org.bsdevelopment.mobfarming.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.blocks.entity.base.FEPoweredEntity;

public class CreativePowerBlockEntity extends FEPoweredEntity {
    public CreativePowerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.CREATIVE_POWER.getTileEntityType(), pos, blockState);

        setMaxEnergy(Integer.MAX_VALUE * FEPoweredEntity.ONE_FE);
        setEnergy(getMaxEnergy());
    }

    @Override
    public long useEnergy(long amount, boolean simulate, boolean force) {
        return amount;
    }

    @Override
    public long addEnergy(long amount, boolean simulate) {
        return amount;
    }
}
