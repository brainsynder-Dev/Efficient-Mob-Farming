package org.bsdevelopment.mobfarming.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bsdevelopment.mobfarming.blocks.base.BlockRedstonePowered;
import org.bsdevelopment.mobfarming.blocks.entity.MobBlenderBase;
import org.bsdevelopment.mobfarming.container.MobBlenderContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BlockMobBlender extends BlockRedstonePowered implements EntityBlock {
    private final VoxelShape SAW_AABB = Block.box(1D, 1D, 1D, 15D, 15D, 15D);

    public BlockMobBlender(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity te = level.getBlockEntity(blockPos);
        if (!(te instanceof MobBlenderBase))
            return InteractionResult.FAIL;

        openMenu(player, blockPos);

        return InteractionResult.SUCCESS;
    }

    public void openMenu(Player player, BlockPos blockPos) {
        player.openMenu(new SimpleMenuProvider(
                (windowId, playerInventory, playerEntity) -> new MobBlenderContainer(windowId, playerInventory, blockPos), Component.translatable("")), (buf -> {
            buf.writeBlockPos(blockPos);
        }));
    }

    @Override
    public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (isPowered(state) && (tileEntity instanceof MobBlenderBase tile)) {
            tile.setActive(true);
        }
    }

    @Override
    public void onPoweredStateChange(boolean powered, @NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Block block) {
        if (level.isClientSide) return;
        if (!(level.getBlockEntity(pos) instanceof MobBlenderBase tile)) return;

        tile.setActive(!isPowered(state));
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (level.isClientSide) return;
        if (!(level.getBlockEntity(pos) instanceof MobBlenderBase tile)) return;

        if (state.getValue(POWERED) && (!level.hasNeighborSignal(pos))) {
            level.setBlock(pos, state.cycle(POWERED), 2);

            tile.setActive(!state.getValue(POWERED));
            tile.setChanged();
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MobBlenderBase(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? MobBlenderBase::clientTick : MobBlenderBase::serverTick;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SAW_AABB;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
