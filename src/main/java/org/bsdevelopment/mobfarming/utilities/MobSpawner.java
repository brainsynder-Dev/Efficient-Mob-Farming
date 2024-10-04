package org.bsdevelopment.mobfarming.utilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.event.EventHooks;
import org.bsdevelopment.mobfarming.config.MobSpawnConfig;

import java.util.ArrayList;
import java.util.List;

public class MobSpawner {
    private final MobSpawnConfig spawnConfig;
    private final WeightedRandomSelector randomSelector = new WeightedRandomSelector();

    public MobSpawner(MobSpawnConfig spawnConfig) {
        this.spawnConfig = spawnConfig;
    }

    public void spawnMobs(Level level, BlockPos pos) {
        List<MobSpawnConfig.SpawnEntry> validEntries = getValidSpawnEntries(level, pos);
        if (validEntries.isEmpty()) return;

        MobSpawnConfig.SpawnEntry selectedEntry = randomSelector.selectRandomEntry(validEntries);
        if (selectedEntry == null) return;
        System.out.println("Selected entry: "+ selectedEntry);

        int spawnCount = randomSelector.getRandomSpawnCount(selectedEntry);
        spawnEntities(selectedEntry, level, pos, spawnCount);
    }

    private List<MobSpawnConfig.SpawnEntry> getValidSpawnEntries(Level level, BlockPos pos) {
        List<MobSpawnConfig.SpawnEntry> validEntries = new ArrayList<>();
        ResourceLocation biomeLocation = level.registryAccess().registryOrThrow(Registries.BIOME).getKey(level.getBiome(pos).value());
        if (biomeLocation == null) return validEntries;

        String currentBiome = biomeLocation.toString();
        String currentDimension = level.dimension().location().toString();

        for (MobSpawnConfig.SpawnEntry entry : spawnConfig.spawns) {
            if (entry.isBiomeValid(currentBiome) && entry.isDimensionValid(currentDimension)) validEntries.add(entry);
        }

        return validEntries;
    }

    private void spawnEntities(MobSpawnConfig.SpawnEntry entry, Level level, BlockPos pos, int count) {
        for (int i = 0; i < count; i++) {
            ResourceLocation mobResource = entry.getMobResource();
            EntityType<?> type = level.registryAccess().registryOrThrow(Registries.ENTITY_TYPE).get(mobResource);
            if (type == null) break;

            Mob entity = (Mob) type.create(level);
            if (entity == null) continue;

            // set the mob's position, and add some randomness to it
            double xOffset = pos.getX() + 0.5 + (Math.random() - 0.5);
            double zOffset = pos.getZ() + 0.5 + (Math.random() - 0.5);
            entity.setPos(xOffset, pos.getY() + 1D, zOffset);

            // check if the mob can spawn at the current position
            if (!EventHooks.checkSpawnPosition(entity, (ServerLevelAccessor) level, MobSpawnType.NATURAL))
                return;
            // checks if the mobs bounding box collides with nearby blocks
            if (!level.noCollision(entity.getBoundingBox())) return;

            // checks if there are any entities of the same type in the current area
            if (!level.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty())
                continue;

            level.addFreshEntity(entity);
        }
    }
}
