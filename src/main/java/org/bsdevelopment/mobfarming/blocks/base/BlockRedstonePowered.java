package org.bsdevelopment.mobfarming.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nonnull;

public abstract class BlockRedstonePowered extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockRedstonePowered(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    public abstract void onPoweredStateChange(boolean powered, @Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Block block);

    public boolean isPowered (BlockState state) {
        return state.getValue(POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide) return;

        boolean flag = state.getValue(POWERED);
        if (flag != level.hasNeighborSignal(pos)) {
            onPoweredStateChange(flag, state, level, pos, block);

            if (flag) {
                level.scheduleTick(pos, this, 4);
            } else {
                level.setBlock(pos, state.cycle(POWERED), 2);
            }
        }

    }
}
