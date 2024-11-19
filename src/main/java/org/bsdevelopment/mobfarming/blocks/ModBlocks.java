package org.bsdevelopment.mobfarming.blocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.entity.CreativePowerBlockEntity;
import org.bsdevelopment.mobfarming.blocks.entity.MobBlenderBase;
import org.bsdevelopment.mobfarming.blocks.entity.MobSpikeBlockEntity;
import org.bsdevelopment.mobfarming.blocks.entity.TransferNodeBlockEntity;
import org.bsdevelopment.mobfarming.blocks.entity.WarpPlateBlockEntity;
import org.bsdevelopment.mobfarming.items.base.BlockItemHandler;
import org.bsdevelopment.mobfarming.utilities.BlockRegistration;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModConstants.MOD_ID);
    public static DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModConstants.MOD_ID);

    public static BlockRegistration<BlockCreativePower, BlockItemHandler, CreativePowerBlockEntity> CREATIVE_POWER = new BlockRegistration<>(
            "creative_power",
            () -> new BlockCreativePower(),
            (var) -> new BlockItemHandler(var, new Item.Properties()), CreativePowerBlockEntity::new);

    public static BlockRegistration<BlockMobBlender, BlockItemHandler, MobBlenderBase> MOB_BLENDER = new BlockRegistration<>(
            "mob_blender",
            () -> new BlockMobBlender(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)),
            (var) -> new BlockItemHandler(var, new Item.Properties()), MobBlenderBase::new);

    public static BlockRegistration<BlockMobSpike, BlockItemHandler, MobSpikeBlockEntity> MOB_SPIKE = new BlockRegistration<>(
            "mob_spike",
            () -> new BlockMobSpike(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE)),
            var -> new BlockItemHandler(var, new Item.Properties()), MobSpikeBlockEntity::new);

    public static BlockRegistration<BlockDeathrootDirt, BlockItemHandler, ?> DEATHROOT_DIRT = new BlockRegistration<>(
            "deathroot_dirt",
            () -> new BlockDeathrootDirt(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY)),
            var -> new BlockItemHandler(var, new Item.Properties())
    );
    public static BlockRegistration<BlockTransferNode, BlockItemHandler, TransferNodeBlockEntity> TRANSFER_NODE = new BlockRegistration<>(
            "transfer_node",
            () -> new BlockTransferNode(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(3f, 10f)
                    .sound(SoundType.DEEPSLATE_TILES)),
            (var) -> new BlockItemHandler(var, new Item.Properties()), TransferNodeBlockEntity::new
    );


    public static BlockRegistration<BlockDestinationPlate, BlockItemHandler, ?> DESTINATION_PLATE = new BlockRegistration<>(
            "destination_plate",
            () -> new BlockDestinationPlate(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(3f, 10f)
                    .noCollission()
                    .sound(SoundType.DEEPSLATE_TILES)),
            var -> new BlockItemHandler(var, new Item.Properties())
    );
    public static BlockRegistration<BlockGlidePlate, BlockItemHandler, ?> GLIDE_PLATE = new BlockRegistration<>(
            "glide_plate",
            () -> new BlockGlidePlate(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(3f, 10f)
                    .noCollission()
                    .sound(SoundType.DEEPSLATE_TILES)),
            var -> new BlockItemHandler(var, new Item.Properties())
    );
    public static BlockRegistration<BlockWarpPlate, BlockItemHandler, WarpPlateBlockEntity> WARP_PLATE = new BlockRegistration<>(
            "warp_plate",
            () -> new BlockWarpPlate(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(3f, 10f)
                    .sound(SoundType.DEEPSLATE_TILES)),
            (var) -> new BlockItemHandler(var, new Item.Properties()), WarpPlateBlockEntity::new
    );

    public static void register(IEventBus evt) {
        BLOCKS.register(evt);
        TILE_ENTITIES.register(evt);
    }
}
