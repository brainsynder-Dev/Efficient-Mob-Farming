package org.bsdevelopment.mobfarming.datagen;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class ModRecipes extends RecipeProvider {
    private final String CHARACTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public ModRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MOB_SPIKE.get())
//                .pattern(" i ")
//                .pattern("idi")
//                .pattern("IDI")
//                .define('i', Items.IRON_INGOT)
//                .define('d', Items.POINTED_DRIPSTONE)
//                .define('I', Items.IRON_BLOCK)
//                .define('D', Items.DRIPSTONE_BLOCK)
//                .group("mob_farming")
//                .unlockedBy("has_iron_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
//                .save(consumer);
        ItemLike air = Items.AIR;
        ItemLike ironIngot = Items.IRON_INGOT;
        ItemLike ironBlock = Items.IRON_BLOCK;
        ItemLike pointed = Items.POINTED_DRIPSTONE;
        ItemLike dripstoneBlock = Items.DRIPSTONE_BLOCK;

        setupRecipe(consumer, List.of(
                List.of(air, ironIngot, air),
                List.of(ironIngot, pointed, ironIngot),
                List.of(ironBlock, dripstoneBlock, ironBlock)
        ), ModBlocks.MOB_SPIKE.get(), Items.IRON_INGOT);
    }

    private void setupRecipe (RecipeOutput consumer, List<List<ItemLike>> input, ItemLike output, ItemLike unlockedBy) {
        Map<ItemLike, Character> keyMap = new HashMap<>();
        AtomicInteger index = new AtomicInteger();
        input.forEach(items -> {
            for (ItemLike item : items) {
                if (keyMap.containsKey(item)) continue;

                Character character = CHARACTER.charAt(index.getAndIncrement());
                keyMap.putIfAbsent(item, character);
                System.out.println("- "+item.asItem().getDescriptionId()+" to "+character);
            }
        });

        ShapedRecipeBuilder recipe = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output);
        recipe.group("mob_farming");
        input.forEach(items -> {
            StringBuilder key = new StringBuilder();
            for (ItemLike item : items) {
                key.append(keyMap.get(item));
            }
            System.out.println(key);
            recipe.pattern(key.toString());
        });
        keyMap.forEach((item, character) -> recipe.define(character, item));
        recipe.unlockedBy("has_"+unlockedBy.asItem().getDescriptionId().substring(17), InventoryChangeTrigger.TriggerInstance.hasItems(unlockedBy));
        recipe.save(consumer);
    }
}