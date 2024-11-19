package org.bsdevelopment.mobfarming.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bsdevelopment.mobfarming.blocks.base.BlockBasePlate;
import org.bsdevelopment.mobfarming.blocks.entity.WarpPlateBlockEntity;
import org.jetbrains.annotations.Nullable;

public class BlockWarpPlate extends BlockBasePlate implements EntityBlock {

    public BlockWarpPlate(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.isCrouching()) return;
        if (entity.getY() <= (double) pos.getY() + 0.4d) {
            if (level.getBlockEntity(pos) instanceof WarpPlateBlockEntity warpPlate) warpPlate.teleportEntity(entity);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WarpPlateBlockEntity(pPos, pState);
    }
}
