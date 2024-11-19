package org.bsdevelopment.mobfarming.blocks.entity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FEPoweredEntity extends DataSaverBlockEntity {
    public static final long ONE_FE = 1_000_000_000L;
    private long energy;
    private long maxEnergy;

    public FEPoweredEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putLong("energy", energy);
        tag.putLong("maxEnergy", maxEnergy);
    }

    @Override
    public void loadData(CompoundTag tag, HolderLookup.Provider registries) {
        energy = tag.getLong("energy");
        setMaxEnergy(tag.getLong("maxEnergy"));
    }

    // ------------------------------------------------
    // -------------------- ENERGY --------------------
    // ------------------------------------------------

    /**
     * Gets the current amount of energy stored in the entity
     *
     * @return The current amount of energy stored in the entity
     */
    public long getEnergy() {
        return energy;
    }

    /**
     * Gets the maximum amount of energy that can be stored in the entity
     *
     * @return The maximum amount of energy that can be stored in the entity
     */
    public long getMaxEnergy() {
        return maxEnergy;
    }

    /**
     * Checks if the entity has enough energy to perform an action
     *
     * @param target The amount of energy required to perform the action
     * @return True if the entity has enough energy, false otherwise
     */
    public boolean hasEnoughEnergy(long target) {
        return getEnergy() > target;
    }

    /**
     * Gets the maximum amount of energy that can be received
     *
     * @return The maximum amount of energy that can be received
     */
    public long getMaxReceive() {
        return maxEnergy;
    }

    /**
     * Sets the maximum amount of energy that can be stored in the entity
     *
     * @param maxEnergy The maximum amount of energy that can be stored in the entity
     */
    public void setMaxEnergy(long maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    /**
     * Adds energy to the entity
     *
     * @param amount The amount of energy to add
     * @param simulate Whether to simulate the energy transfer or not
     * @return The amount of energy that was accepted
     */
    public long addEnergy(long amount, boolean simulate) {
        long accepted = Math.min(Math.min(maxEnergy - energy, amount), getMaxReceive());
        if (!simulate && accepted >= 0) {
            energy += accepted;
            setChanged();
        }
        return accepted;
    }

    /**
     * Uses energy from the entity
     *
     * @param amount The amount of energy to use
     * @param simulate Whether to simulate the energy transfer or not
     * @param force Whether to force the energy transfer or not
     * @return The amount of energy that was used
     */
    public long useEnergy(long amount, boolean simulate, boolean force) {
        long used = force ? amount : Math.min(amount, energy);
        if (!simulate && used >= 0) {
            energy -= used;
            setChanged();
        }
        return used;
    }

    /**
     * Sets the current amount of energy stored in the entity
     *
     * @param energy The current amount of energy stored in the entity
     */
    public final void setEnergy(long energy) {
        this.energy = energy;
        setChanged();
    }
}