package org.bsdevelopment.mobfarming.blocks.entity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class InventoryBlockEntity extends DataSaverBlockEntity implements WorldlyContainer {
    private NonNullList<ItemStack> inventory;

    public InventoryBlockEntity(BlockEntityType<?> blockEntityType, int inventorySize, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void saveData(CompoundTag compound, HolderLookup.Provider registries) {
        ContainerHelper.saveAllItems(compound, inventory, registries);
    }

    @Override
    public void loadData(CompoundTag compound, HolderLookup.Provider registries) {
        inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (compound.contains("Items", 9))
            ContainerHelper.loadAllItems(compound, inventory, registries);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = ContainerHelper.removeItem(inventory, index, count);
        if (!itemstack.isEmpty()) setChanged();
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(getItems(), index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        inventory.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) stack.setCount(this.getMaxStackSize());
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }



    @Override
    public void startOpen(Player player) {}

    @Override
    public void stopOpen(Player player) {}
}
