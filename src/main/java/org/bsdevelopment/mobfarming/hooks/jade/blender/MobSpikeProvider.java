package org.bsdevelopment.mobfarming.hooks.jade.blender;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.entity.MobSpikeBlockEntity;
import org.bsdevelopment.mobfarming.config.Config;
import org.bsdevelopment.mobfarming.utilities.ModUtilities;
import org.bsdevelopment.mobfarming.utilities.storage.BulkStorage;
import org.bsdevelopment.mobfarming.utilities.storage.ListPager;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum MobSpikeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag nbt = accessor.getServerData();
        BulkStorage storage = BulkStorage.CODEC.codec().parse(NbtOps.INSTANCE, nbt.get("storage")).result().orElseGet(BulkStorage::new);

        IElementHelper helper = IElementHelper.get();
        ListPager<IElement> elements = new ListPager<>(Config.maxItemsPerLine);
        ModUtilities.sortByValue(storage.getItemMapCopy()).forEach((itemRecord, aLong) -> {
            if (elements.totalPages() >= 4) return;
            elements.add(helper.item(itemRecord.toStack(Math.toIntExact(aLong))));
        });

        elements.getAllPages().forEach(tooltip::add);
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof MobSpikeBlockEntity blockEntity)) return;
        accessor.getServerData().put("storage", BulkStorage.CODEC.codec().encodeStart(NbtOps.INSTANCE, blockEntity.getBulkStorage()).getOrThrow());
    }

    @Override
    public boolean shouldRequestData(BlockAccessor accessor) {
        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "mob_spike");
    }
}