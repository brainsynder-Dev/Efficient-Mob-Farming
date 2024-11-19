package org.bsdevelopment.mobfarming.container.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.bsdevelopment.mobfarming.blocks.entity.base.DataSaverBlockEntity;
import org.jetbrains.annotations.Nullable;

public abstract class BaseInventoryContainer<T extends DataSaverBlockEntity> extends BaseContainer {
    public T blockEntity;
    protected Player player;
    protected BlockPos pos;

    public BaseInventoryContainer(@Nullable MenuType<?> menuType, int windowId, Inventory playerInventory, BlockPos blockPos) {
        super(menuType, windowId);
        this.pos = blockPos;
        this.player = playerInventory.player;
        this.blockEntity = (T) player.level().getBlockEntity(pos);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }
}