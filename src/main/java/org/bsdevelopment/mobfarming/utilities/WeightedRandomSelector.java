package org.bsdevelopment.mobfarming.utilities;

import org.bsdevelopment.mobfarming.config.MobSpawnConfig;

import java.util.List;
import java.util.Random;

public class WeightedRandomSelector {
    private final Random random = new Random();

    /**
     * Selects a random entry based on its weight.
     */
    public MobSpawnConfig.SpawnEntry selectRandomEntry(List<MobSpawnConfig.SpawnEntry> validEntries) {
        int totalWeight = validEntries.stream().mapToInt(entry -> entry.weight).sum();
        int randomWeight = random.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (MobSpawnConfig.SpawnEntry entry : validEntries) {
            cumulativeWeight += entry.weight;

            if (randomWeight < cumulativeWeight) return entry;
        }
        return null;
    }

    /**
     * Determines the number of mobs to spawn based on the entry's min and max count.
     */
    public int getRandomSpawnCount(MobSpawnConfig.SpawnEntry entry) {
        return random.nextInt(entry.maxCount - entry.minCount + 1) + entry.minCount;
    }
}
