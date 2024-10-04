package org.bsdevelopment.mobfarming.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MobSpawnConfig {

    @SerializedName("spawns")
    public List<SpawnEntry> spawns;

    public static class SpawnEntry {
        @SerializedName("entity-type")
        public String entityType;
        @SerializedName("weight")
        public int weight;
        @SerializedName("minimum-spawn-count")
        public int minCount;
        @SerializedName("maximum-spawn-count")
        public int maxCount;
        @SerializedName("allowed-biomes")
        public List<String> biomes;
        @SerializedName("allowed-dimensions")
        public List<String> dimensions;

        public SpawnEntry(String entityType, int minCount, int maxCount, int weight) {
            this.entityType = entityType;
            this.minCount = minCount;
            this.maxCount = maxCount;
            this.weight = weight;
        }

        public SpawnEntry setBiomes(String... biomes) {
            this.biomes.addAll(List.of(biomes));
            return this;
        }

        public SpawnEntry setDimensions(String... dimensions) {
            this.dimensions.addAll(List.of(dimensions));
            return this;
        }

        public ResourceLocation getMobResource() {
            return ResourceLocation.parse(entityType);
        }

        public boolean isBiomeValid(String biome) {
            return biomes.contains("ANY") || biomes.contains(biome);
        }

        public boolean isDimensionValid(String dimension) {
            return dimensions.contains("ANY") || dimensions.contains(dimension);
        }
    }

    public static MobSpawnConfig loadConfig(Path path, SpawnEntry... defaultEntries) {
        if (!path.toFile().exists()) return saveDefaultConfig(path, defaultEntries);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return new Gson().fromJson(reader, MobSpawnConfig.class);
        } catch (IOException e) {
            return null;
        }
    }

    private static MobSpawnConfig saveDefaultConfig(Path path, SpawnEntry... entries) {
        MobSpawnConfig config = new MobSpawnConfig();
        config.spawns = List.of(entries);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }
}
