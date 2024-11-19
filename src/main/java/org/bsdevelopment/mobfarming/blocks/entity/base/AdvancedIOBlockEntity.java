package org.bsdevelopment.mobfarming.blocks.entity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class AdvancedIOBlockEntity extends InventoryBlockEntity implements ICapabilityProvider{
    private final Map<Direction, SideConfig> sideConfigMap;
    private final ItemStackHandler inputHandler = new ItemStackHandler(1);
    private final ItemStackHandler outputHandler = new ItemStackHandler(1);
    public static final BlockCapability<IItemHandler, @Nullable Direction> ITEM_HANDLER_BLOCK =
            BlockCapability.createSided(
                    // Provide a name to uniquely identify the capability.
                    ResourceLocation.fromNamespaceAndPath("mymod", "item_handler"),
                    // Provide the queried type. Here, we want to look up `IItemHandler` instances.
                    IItemHandler.class);

    public AdvancedIOBlockEntity(BlockEntityType<?> blockEntityType, int inventorySize, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, inventorySize, blockPos, blockState);
        sideConfigMap = new EnumMap<>(Direction.class);

        // Initialize all sides to NONE
        for (Direction direction : Direction.values()) {
            sideConfigMap.put(direction, SideConfig.NONE);
        }
    }

    @Override
    public void loadData(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadData(compound, registries);
        // Load side configurations from NBT
        for (Direction direction : Direction.values()) {
            String configName = compound.getString("side_" + direction.getName());
            sideConfigMap.put(direction, SideConfig.valueOf(configName));
        }

        inputHandler.deserializeNBT(registries, compound.getCompound("InputHandler"));
        outputHandler.deserializeNBT(registries, compound.getCompound("OutputHandler"));
    }

    @Override
    public void saveData(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveData(compound, registries);
        // Save side configurations to NBT
        for (Direction direction : Direction.values()) {
            compound.putString("side_" + direction.getName(), sideConfigMap.get(direction).name());
        }

        compound.put("InputHandler", inputHandler.serializeNBT(registries));
        compound.put("OutputHandler", outputHandler.serializeNBT(registries));
    }

    // Allow setting the configuration for each side
    public void setSideConfig(Direction side, SideConfig config) {
        sideConfigMap.put(side, config);
        setChanged();  // Mark the block entity as updated
    }

    // Get the configuration for a specific side
    public SideConfig getSideConfig(Direction side) {
        return sideConfigMap.getOrDefault(side, SideConfig.NONE);
    }



    public @Nullable Object getCapability(Object cap, Object context) {
        if (cap == Capabilities.ItemHandler.BLOCK && context instanceof Direction side) {
            SideConfig config = sideConfigMap.get(side);
            if (config == SideConfig.INPUT) {
                return inputHandler;
            } else if (config == SideConfig.OUTPUT) {
                return outputHandler;
            }
        }
        return null;
    }

    public enum SideConfig {
        NONE,    // Neither input nor output
        INPUT,   // Input only
        OUTPUT,  // Output only
        BOTH     // Input and output
    }
}
