package org.bsdevelopment.mobfarming.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ModUtilities {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T readGSON(File file, Class<T> type) {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) return null;

        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void writeGSON(File file, T t) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(gson.toJson(t));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Holder<Enchantment> toHolder(ResourceKey<Enchantment> resourceKey, Level level) {
        return level.registryAccess().registry(Registries.ENCHANTMENT).get().getHolderOrThrow(resourceKey);
    }

    public static LinkedList<Component> formatMultiLine(String section) {
        LinkedList<Component> components = new LinkedList<>();
        int loreIndex = 1;
        while (true) {
            String key = section+"." + loreIndex;
            Component loreLine = Component.translatable(key);

            // Break if the itemRecord is not found (means no more lines)
            if (loreLine.getString().equals(key)) {
                break;
            }

            // Add the line to the list
            components.add(loreLine);
            loreIndex++;
        }
        return components;
    }

    public static boolean inBounds(int x, int y, int w, int h, double ox, double oy) {
        return ox >= x && ox <= x + w && oy >= y && oy <= y + h;
    }

    public static <K1, V1, R> Function<Map.Entry<K1, V1>, R> toAny(final BiFunction<? super K1, ? super V1, ? extends R> f) {
        return e -> f.apply(e.getKey(), e.getValue());
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> entryToMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <E, K, V> Function<E, Map.Entry<K, V>> toEntry(final Function<? super E, ? extends K> ketFactory, final Function<? super E, ? extends V> valueFactory) {
        return e -> new AbstractMap.SimpleImmutableEntry<>(ketFactory.apply(e), valueFactory.apply(e));
    }

    public static <K, V> Predicate<Map.Entry<K, V>> byKey(final Predicate<? super K> f) {
        return e -> f.test(e.getKey());
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static <FROM, TO extends FROM> BiConsumer<FROM, Consumer<TO>> cast(Class<TO> toClass) {
        Objects.requireNonNull(toClass);
        return (from, toConsumer) -> {
            if (toClass.isInstance(from)) {
                toConsumer.accept(toClass.cast(from));
            }
        };
    }

    /**
     * Scans the blocks around a given BlockPos within a specified radius and processes
     * those that match a given BlockState using a provided Consumer.
     *
     * @param level            The current level (world) where the blocks are being checked.
     * @param originPos        The center position to start checking around.
     * @param radius           The radius (in blocks) around the center to scan for matching blocks.
     * @param targetBlockState The BlockState that the surrounding blocks should be compared to.
     * @param blockProcessor   A Consumer that processes the BlockPos of matching blocks.
     */
    public static void processMatchingBlocks(Level level, BlockPos originPos, int radius, Block targetBlockState, Consumer<BlockPos> blockProcessor) {
        // Iterate through the cube defined by the radius around the origin position
        for (int xOffset = -radius; xOffset <= radius; xOffset++) {
            for (int yOffset = -radius; yOffset <= radius; yOffset++) {
                for (int zOffset = -radius; zOffset <= radius; zOffset++) {
                    // Skip the origin position if radius > 0
                    if ((xOffset == 0) && (yOffset == 0) && (zOffset == 0) && (radius > 0)) continue;

                    // Calculate the offset position
                    BlockPos currentPos = originPos.offset(xOffset, yOffset, zOffset);

                    // Get the block state at the current position
                    BlockState currentBlockState = level.getBlockState(currentPos);

                    // If the block state matches the target, pass the position to the consumer
                    if (currentBlockState.getBlock().getDescriptionId().equals(targetBlockState.getDescriptionId())) {
                        blockProcessor.accept(currentPos);
                    }
                }
            }
        }
    }

    public static BlockHitResult getLookingAt(Player player, ClipContext.Fluid rayTraceFluid, int range) {
        Level level = player.level();

        Vec3 look = player.getLookAngle();
        Vec3 start = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());

        Vec3 end = new Vec3(player.getX() + look.x * (double) range, player.getY() + player.getEyeHeight() + look.y * (double) range, player.getZ() + look.z * (double) range);
        ClipContext context = new ClipContext(start, end, ClipContext.Block.COLLIDER, rayTraceFluid, player);
        return level.clip(context);
    }
}
