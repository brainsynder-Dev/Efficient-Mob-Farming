package org.bsdevelopment.mobfarming.utilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CapabilityHelper {

    public static boolean hasBlockCapability(BlockCapability capability, @Nonnull Level level, @Nonnull BlockPos pos) {
        return level.getCapability(capability, pos) != null;
    }

    // Item Capability Helpers

    @Nonnull
    public static Optional<IItemHandler> getItemHandler(ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(Capabilities.ItemHandler.ITEM));
    }

    @Nonnull
    public static Optional<IItemHandler> getBlockItemHandler(@Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction side) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                return Optional.ofNullable(level.getCapability(Capabilities.ItemHandler.BLOCK, pos, level.getBlockState(pos), blockEntity, side));
            }
        }
        return Optional.empty();
    }

    // Fluid Capability Helpers

    @Nonnull
    public static Optional<IFluidHandler> getItemFluidHandler(ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(Capabilities.FluidHandler.ITEM));
    }

    @Nonnull
    public static Optional<IFluidHandler> getBlockFluidHandler(@Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction side) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                return Optional.ofNullable(level.getCapability(Capabilities.FluidHandler.BLOCK, pos, level.getBlockState(pos), blockEntity, side));
            }
        }
        return Optional.empty();
    }

    // Energy Capability Helpers

    @Nonnull
    public static Optional<IEnergyStorage> getItemEnergyHandler(ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM));
    }

    @Nonnull
    public static Optional<IEnergyStorage> getBlockEnergyHandler(@Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction side) {
        BlockState blockState = level.getBlockState(pos);
        if (!blockState.hasBlockEntity()) return Optional.empty();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return Optional.empty();

        return Optional.ofNullable(level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, level.getBlockState(pos), blockEntity, side));
    }
}