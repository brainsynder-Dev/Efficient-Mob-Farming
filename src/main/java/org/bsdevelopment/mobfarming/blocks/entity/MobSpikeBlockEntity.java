package org.bsdevelopment.mobfarming.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.state.BlockState;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.blocks.entity.base.IStorage;
import org.bsdevelopment.mobfarming.blocks.entity.base.InventoryBlockEntity;
import org.bsdevelopment.mobfarming.utilities.storage.BulkStorage;

public class MobSpikeBlockEntity extends InventoryBlockEntity implements IStorage {
    private BulkStorage bulkStorage = new BulkStorage();

    public MobSpikeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.MOB_SPIKE.getTileEntityType(), 0, blockPos, blockState);
    }

    @Override
    public void saveStorage() {
        setChanged();
    }

    @Override
    public BulkStorage getBulkStorage() {
        return bulkStorage;
    }

    @Override
    public void loadData(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadData(nbt, registries);
        bulkStorage = BulkStorage.CODEC.codec().parse(NbtOps.INSTANCE, nbt.get("storage")).result().orElseGet(BulkStorage::new);
    }

    @Override
    public void saveData(CompoundTag nbt, HolderLookup.Provider registries) {
        populateCompound(nbt);
        super.saveData(nbt, registries);
    }

    public void populateCompound(CompoundTag nbt) {
        nbt.put("storage", BulkStorage.CODEC.codec().encodeStart(NbtOps.INSTANCE, bulkStorage).getOrThrow());
    }
}
