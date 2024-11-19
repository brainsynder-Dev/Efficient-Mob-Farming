package org.bsdevelopment.mobfarming.container;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.blocks.entity.MobBlenderBase;
import org.bsdevelopment.mobfarming.client.screen.widgets.Widgets;
import org.bsdevelopment.mobfarming.config.Config;
import org.bsdevelopment.mobfarming.container.base.BaseInventoryContainer;
import org.bsdevelopment.mobfarming.items.ModItems;
import org.bsdevelopment.mobfarming.items.base.UpgradeItem;

public class MobBlenderContainer extends BaseInventoryContainer<MobBlenderBase> {
    private int experience = 0;
    private boolean disabledSwordSlot = true;

    public MobBlenderContainer(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, extraData.readBlockPos());
    }

    public MobBlenderContainer(int windowId, Inventory playerInventory, BlockPos blockPos) {
        super(ModContainers.MOB_BLENDER.get(), windowId, playerInventory, blockPos);

        // Upgrade Slot
        blockEntity.SLOTS.forEach((slotIndex, slotPosition) -> {
            addSlot(new Slot(blockEntity, slotIndex, slotPosition.x(), slotPosition.y()) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    if (slotPosition.widgetInfo() == Widgets.SWORD_SLOT) {
                        return (stack.getItem() instanceof SwordItem) && !blockEntity.disabledSwordSlot;
                    }
                    return stack.getItem() instanceof UpgradeItem;
                }

                @Override
                public int getMaxStackSize(ItemStack stack) {
                    if (stack.is(ModItems.LOOTING_UPGRADE.get())) return Config.maxLootingUpgrade;
                    if (stack.is(ModItems.SPEED_UPGRADE.get())) return 10;
                    if (stack.is(ModItems.SWORD_UPGRADE.get()) || stack.is(ModItems.CREATIVE_SWORD_UPGRADE.get())) return 1;
                    return super.getMaxStackSize(stack);
                }

                @Override
                public void set(ItemStack stack) {
                    super.set(stack);
                    if (slotPosition.widgetInfo() == Widgets.SWORD_SLOT) {
                        blockEntity.setSwordItem(stack);
                        return;
                    }
                    blockEntity.addUpgrade(stack);
                }
            });
        });

        // Sync Experience
        if (player.level().getBlockEntity(pos) instanceof MobBlenderBase blender) {
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return blender.getStoredExperience().intValue();
                }

                @Override
                public void set(int value) {
                    MobBlenderContainer.this.experience = (MobBlenderContainer.this.experience) | (value);
                }
            });

            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return blender.disabledSwordSlot ? 1 : 0;
                }

                @Override
                public void set(int value) {
                    MobBlenderContainer.this.disabledSwordSlot = (value == 1);
                }
            });
        }
        addPlayerSlots(playerInventory, 8, 84);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int SLOTS = blockEntity.SLOTS.size();
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack currentStack = slot.getItem();
            if (index < SLOTS) { //Slot to Player Inventory
                if (!this.moveItemStackTo(currentStack, SLOTS, Inventory.INVENTORY_SIZE + SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            }

            if (index >= SLOTS) { //Player Inventory to Slots
                if (!this.moveItemStackTo(currentStack, 0, SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (currentStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else if (slot.mayPlace(currentStack)) {
                slot.set(currentStack);
                slot.setChanged();
            }

            if (currentStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, currentStack);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, ModBlocks.MOB_BLENDER.getBlock());
    }

    public int getExperience() {
        return experience;
    }

    public boolean isSwordSlotDisabled() {
        return disabledSwordSlot;
    }
}
