package org.bsdevelopment.mobfarming.utilities;

import com.google.gson.Gson;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bsdevelopment.mobfarming.blocks.entity.base.ILinkable;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class BlockSelector {

    private final Map<Predicate<Block>, Predicate<Block>> blockPairs = new HashMap<>();
    private boolean firstBlockStoresData = true; // Default to the first block storing data

    public BlockSelector() {
        loadBlockPairsFromConfig();
    }

    /**
     * Load the block pairs from a JSON configuration.
     */
    private void loadBlockPairsFromConfig() {
        Gson gson = new Gson();

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/config/linking_wand.json"))) {
            LinkingWandConfig config = gson.fromJson(reader, LinkingWandConfig.class);

            for (LinkingWandConfig.Pair pair : config.pairs) {
                Block firstBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(pair.first_block));
                Block secondBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(pair.second_block));

                blockPairs.put(block -> block == firstBlock, block -> block == secondBlock);
            }

            // Determine where to store data (first or second block)
            this.firstBlockStoresData = config.store_data_in.equalsIgnoreCase("first_block");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFirstBlockStoringData() {
        return firstBlockStoresData;
    }

    public void storeLinkedData(Level level, BlockPos firstBlockPos, BlockPos secondBlockPos) {
        BlockPos sourcePos = firstBlockStoresData ? firstBlockPos : secondBlockPos;
        BlockPos targetPos = firstBlockStoresData ? secondBlockPos : firstBlockPos;

        // Logic to store linked data, like linking block entities
        BlockEntity sourceEntity = level.getBlockEntity(sourcePos);
        BlockEntity targetEntity = level.getBlockEntity(targetPos);

        if (targetEntity != null && sourceEntity instanceof ILinkable linkable) {
            linkable.linkPosition(targetPos);
        }
    }

    public void writeToNBT(CompoundTag tag) {
        tag.putBoolean("FirstStoresData", firstBlockStoresData);
    }

    public void readFromNBT(CompoundTag tag) {
        if (tag.contains("FirstStoresData")) {
            this.firstBlockStoresData = tag.getBoolean("FirstStoresData");
        }
    }

    // Class to load JSON structure
    private static class LinkingWandConfig {
        public Pair[] pairs;
        public String store_data_in;

        public static class Pair {
            public String first_block;
            public String second_block;
        }
    }
}
