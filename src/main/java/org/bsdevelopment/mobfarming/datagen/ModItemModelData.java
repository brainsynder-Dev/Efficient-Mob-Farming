package org.bsdevelopment.mobfarming.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.items.ModItems;

public class ModItemModelData extends ItemModelProvider {
    public ModItemModelData(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModConstants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleGenerated(ModItems.SWORD_CORE.get());
        simpleGenerated(ModItems.SPEED_UPGRADE.get());
        simpleGenerated(ModItems.UPGRADE_BASE.get());
        simpleGenerated(ModItems.SWORD_UPGRADE.get());
        simpleGenerated(ModItems.LOOTING_UPGRADE.get());
        simpleGenerated(ModItems.CREATIVE_SWORD_UPGRADE.get());
        simpleGenerated(ModItems.LINKING_WAND.get());
        simpleGenerated(ModItems.WARP_CORE.get());

        simpleBlockItemGenerated(ModBlocks.MOB_SPIKE.getBlockHolder());
        simpleBlockItemGenerated(ModBlocks.CREATIVE_POWER.getBlockHolder());
    }

    protected ItemModelBuilder generated(ItemLike itemLike, ResourceLocation texture) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath(), "item/generated").texture("layer0", texture);
    }

    public void simpleGenerated(ItemLike itemLike) {
        generated(itemLike, modLoc("item/"+itemLike.asItem().getDescriptionId().substring(17)));
    }

    public void simpleBlockItemGenerated(DeferredBlock<?> block) {
        withExistingParent(block.getId().getPath(), modLoc("block/"+block.getId().getPath()));
    }
}