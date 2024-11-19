package org.bsdevelopment.mobfarming.utilities;

import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.blocks.entity.base.FEPoweredEntity;

public final class EnergyIntegration {
    @SubscribeEvent
    public static void attachCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlocks.CREATIVE_POWER.getTileEntityType(), PowerEntityStorage::new);
    }

    public record PowerEntityStorage(FEPoweredEntity entity) implements IEnergyStorage {
        PowerEntityStorage(FEPoweredEntity entity, Direction ignored) {
            this(entity);
        }

        static int clamp(long value) {
            return Math.clamp(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            var received = entity.addEnergy(maxReceive * FEPoweredEntity.ONE_FE, simulate);
            return clamp(received / FEPoweredEntity.ONE_FE);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            // not extractable
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return clamp(entity.getEnergy() / FEPoweredEntity.ONE_FE);
        }

        @Override
        public int getMaxEnergyStored() {
            return clamp(entity.getMaxEnergy() / FEPoweredEntity.ONE_FE);
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    }
}