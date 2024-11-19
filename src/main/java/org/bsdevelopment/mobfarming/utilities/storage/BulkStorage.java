package org.bsdevelopment.mobfarming.utilities.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BulkStorage {
    private static final MapCodec<CondencesItemRecord> ITEM_KEY_COUNT_MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            RecordCodecBuilder.of(c -> c.itemRecord().item(), "item", BuiltInRegistries.ITEM.byNameCodec()),
            DataComponentPatch.CODEC.optionalFieldOf("componentPatch").forGetter(c -> Optional.of(c.itemRecord().componentPatch())),
            RecordCodecBuilder.of(CondencesItemRecord::count, "count", Codec.LONG)
    ).apply(i, (a, b, c) -> new CondencesItemRecord(new ItemRecord(a, b.orElse(DataComponentPatch.EMPTY)), c)));

    public static final MapCodec<BulkStorage> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            RecordCodecBuilder.of(BulkStorage::convertToList, "items", ITEM_KEY_COUNT_MAP_CODEC.codec().listOf())
    ).apply(i, (itemKeyCounts) -> new BulkStorage(CondencesItemRecord.list2Map(itemKeyCounts))));

    private Map<ItemRecord, Long> itemMap = new LinkedHashMap<>();

    public BulkStorage() {
        this.itemMap = new LinkedHashMap<>();
    }
    public BulkStorage(Map<ItemRecord, Long> itemKeyLongMap) {
        this.itemMap = itemKeyLongMap;
    }

    public void addItem(ItemStack stack) {
        if (stack.isEmpty()) return;

        var key = ItemRecord.of(stack);
        itemMap.merge(key, (long) stack.getCount(), Long::sum);
    }

    public Map<ItemRecord, Long> getItemMapRaw() {
        return itemMap;
    }

    public Map<ItemRecord, Long> getItemMapCopy() {
        return Map.copyOf(itemMap);
    }

    public record ItemRecord(Item item, DataComponentPatch componentPatch) {
        static ItemRecord of(ItemStack stack) {
            return new ItemRecord(stack.getItem(), stack.getComponentsPatch());
        }

        public ItemStack toStack(int count) {
            return new ItemStack(Holder.direct(item), count, componentPatch);
        }
    }

    public record CondencesItemRecord(ItemRecord itemRecord, long count) {
        static Map<ItemRecord, Long> list2Map(List<CondencesItemRecord> list) {
            Map<ItemRecord, Long> map = new HashMap<>();
            list.forEach(record -> map.put(record.itemRecord(), record.count()));
            return map;
        }
    }

    public List<CondencesItemRecord> convertToList() {
        return itemMap.entrySet().stream().map(e -> new CondencesItemRecord(e.getKey(), e.getValue())).toList();
    }
}