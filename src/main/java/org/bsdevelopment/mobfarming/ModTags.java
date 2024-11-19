package org.bsdevelopment.mobfarming;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModTags extends ItemTagsProvider {
    public ModTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), ModConstants.MOD_ID, helper);
    }
    public static final TagKey<Item> FUEL_CANISTER_DENY = ItemTags.create(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, ""));
    public static final TagKey<Item> HIDDEN_FROM_RECIPE_VIEWERS = forgeTag("hidden_from_recipe_viewers");

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(HIDDEN_FROM_RECIPE_VIEWERS)
                .add(ModItems.BLENDER_BLADE1.get())
                .add(ModItems.BLENDER_BLADE1.get())
                .add(ModBlocks.CREATIVE_POWER.getItem());
    }



    private static TagKey<Item> forgeTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
