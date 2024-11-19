package org.bsdevelopment.mobfarming.utilities;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.items.ModItems;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;


public class BlockRegistration<BLOCK extends Block, ITEM extends Item, TILE extends BlockEntity> implements Supplier<BLOCK> {
    private final String name;
    private final DeferredBlock<BLOCK> block;
    private final DeferredItem<ITEM> item;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<TILE>> tile;

    public BlockRegistration(String name, Supplier<BLOCK> blockSupplier, Function<BLOCK, ITEM> itemSupplier, BlockEntityType.BlockEntitySupplier<TILE> tileSupplier) {
        this.name = name;
        block = ModBlocks.BLOCKS.register(name, blockSupplier);
        item = ModItems.ITEMS.register(name, () -> itemSupplier.apply(block.get()));
        tile = ModBlocks.TILE_ENTITIES.register(name, () -> BlockEntityType.Builder.of(tileSupplier, block.get()).build(null));
    }

    public BlockRegistration(String name, Supplier<BLOCK> blockSupplier, Function<BLOCK, ITEM> itemSupplier) {
        this.name = name;
        block = ModBlocks.BLOCKS.register(name, blockSupplier);
        item = ModItems.ITEMS.register(name, () -> itemSupplier.apply(block.get()));
    }

    public String getName() {
        return name;
    }

    @Override
    public BLOCK get() {
        return getBlock();
    }

    public DeferredBlock<BLOCK> getBlockHolder() {
        return block;
    }
    public DeferredItem<ITEM> getItemHolder() {
        return item;
    }
    public DeferredHolder<BlockEntityType<?>, BlockEntityType<TILE>> getTileHolder() {
        return tile;
    }

    @Nonnull
    public BLOCK getBlock() {
        return block.get();
    }

    @Nonnull
    public ITEM getItem() {
        return item.get();
    }

    @Nonnull
    public BlockEntityType<TILE> getTileEntityType() {
        return Objects.requireNonNull(tile).get();
    }
}