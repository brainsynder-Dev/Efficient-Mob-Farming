package org.bsdevelopment.mobfarming.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.bsdevelopment.mobfarming.blocks.BlockTransferNode;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.blocks.entity.base.DataSaverBlockEntity;
import org.bsdevelopment.mobfarming.blocks.entity.base.ILinkable;
import org.bsdevelopment.mobfarming.blocks.entity.base.IStorage;
import org.bsdevelopment.mobfarming.utilities.ItemTransferUtils;

import java.util.ArrayList;
import java.util.List;

public class TransferNodeBlockEntity extends DataSaverBlockEntity implements ILinkable {
    private final List<BlockPos> containerPositions = new ArrayList<>();
    protected BlockCapabilityCache<IItemHandler, Direction> attachedInventory;

    public TransferNodeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.TRANSFER_NODE.getTileEntityType(), pPos, pBlockState);
    }

    @Override
    public void linkPosition(BlockPos pos) {
        containerPositions.add(pos);
        setChanged();
    }

    @Override
    public boolean isLinked() {
        return !containerPositions.isEmpty();
    }


    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if (t instanceof TransferNodeBlockEntity node) {
            if (level.getGameTime() % 10 != 0) return;
            if (!(level.getBlockState(blockPos).getBlock() instanceof BlockTransferNode)) return;
            if (!node.isLinked()) return;

            Direction direction = Direction.DOWN;
            BlockEntity targetBlockEntity = level.getBlockEntity(blockPos.relative(direction));
            if (targetBlockEntity == null) return;

            for (BlockPos pos : node.containerPositions) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if ((!(blockEntity instanceof IStorage storage))) {
                    node.containerPositions.remove(pos);
                    node.setChanged();
                    return;
                }

                IItemHandler outputTargetHandler = node.getAttachedInventory();
                if (outputTargetHandler == null) return;

                ItemTransferUtils.transferAllItems(storage, outputTargetHandler);
            }
        }
    }



    private IItemHandler getAttachedInventory() {
        if (attachedInventory == null) {
            assert this.level != null;
            // BlockState state = level.getBlockState(getBlockPos());
            Direction facing = Direction.DOWN;// state.getValue(BlockStateProperties.FACING);
            BlockPos inventoryPos = getBlockPos().relative(facing);
            attachedInventory = BlockCapabilityCache.create(
                    Capabilities.ItemHandler.BLOCK, // capability to cache
                    (ServerLevel) this.level, // level
                    inventoryPos, // target position
                    facing.getOpposite() // context (The side of the block we're trying to pull/push from?)
            );
        }
        return attachedInventory.getCapability();
    }

    @Override
    public void loadData(CompoundTag tag, HolderLookup.Provider registries) {
        if (!tag.contains("bound_targets")) return;

        ListTag listTag = tag.getList("bound_targets", Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            BlockPos pos = new BlockPos(compoundTag.getInt("x"), compoundTag.getInt("y"), compoundTag.getInt("z"));
            containerPositions.add(pos);
        }
    }

    @Override
    public void saveData(CompoundTag tag, HolderLookup.Provider registries) {
        if (containerPositions.isEmpty()) return;

        ListTag listTag = new ListTag();
        for (BlockPos pos : containerPositions) {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("x", pos.getX());
            compoundTag.putInt("y", pos.getY());
            compoundTag.putInt("z", pos.getZ());
            listTag.add(compoundTag);
        }
        tag.put("bound_targets", listTag);
    }


}
