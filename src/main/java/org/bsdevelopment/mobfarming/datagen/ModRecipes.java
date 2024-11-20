package org.bsdevelopment.mobfarming.datagen;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.items.ModItems;

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
        ItemLike air = Items.AIR;
        ItemLike rawIron = Items.RAW_IRON;
        ItemLike ironNugget = Items.IRON_NUGGET;
        ItemLike ironIngot = Items.IRON_INGOT;
        ItemLike ironBlock = Items.IRON_BLOCK;
        ItemLike ironSword = Items.IRON_SWORD;
        ItemLike pointed = Items.POINTED_DRIPSTONE;
        ItemLike dripstoneBlock = Items.DRIPSTONE_BLOCK;
        ItemLike enderpearl = Items.ENDER_PEARL;
        ItemLike eyeOfEnder = Items.ENDER_EYE;
        ItemLike heartOfTheSea = Items.HEART_OF_THE_SEA;
        ItemLike stick = Items.STICK;
        ItemLike polishedBlackstone = Items.POLISHED_BLACKSTONE;
        ItemLike polishedBlackstonePlate = Items.POLISHED_BLACKSTONE_PRESSURE_PLATE;
        ItemLike polishedBlackstoneSlab = Items.POLISHED_BLACKSTONE_SLAB;
        ItemLike smoothBasalt = Items.SMOOTH_BASALT;
        ItemLike chest = Items.CHEST;
        ItemLike witherRose = Items.WITHER_ROSE;

        ItemLike warpCore = ModItems.WARP_CORE.get();
        ItemLike swordCore = ModItems.SWORD_CORE.get();

        // Mod Blocks
        setupRecipe(consumer, List.of(
                List.of(air, witherRose, air),
                List.of(witherRose, Items.DIRT, witherRose),
                List.of(air, witherRose, air)
        ), ModBlocks.DEATHROOT_DIRT.get(), witherRose);

        setupRecipe(consumer, List.of(
                List.of(air, ironIngot, air),
                List.of(ironIngot, pointed, ironIngot),
                List.of(ironBlock, dripstoneBlock, ironBlock)
        ), ModBlocks.MOB_SPIKE.get(), Items.IRON_INGOT);

        setupRecipe(consumer, List.of(
                List.of(air, air, air),
                List.of(polishedBlackstonePlate, warpCore, polishedBlackstonePlate),
                List.of(polishedBlackstone, polishedBlackstone, polishedBlackstone)
        ), ModBlocks.WARP_PLATE.get(), warpCore);

        setupRecipe(consumer, List.of(
                List.of(air, air, air),
                List.of(polishedBlackstonePlate, eyeOfEnder, polishedBlackstonePlate),
                List.of(polishedBlackstone, polishedBlackstone, polishedBlackstone)
        ), ModBlocks.DESTINATION_PLATE.get(), eyeOfEnder);

        setupRecipe(consumer, List.of(
                List.of(polishedBlackstoneSlab, polishedBlackstoneSlab, polishedBlackstoneSlab),
                List.of(ironSword, swordCore, ironSword),
                List.of(polishedBlackstone, polishedBlackstone, polishedBlackstone)
        ), ModBlocks.MOB_BLENDER.get(), swordCore);

        setupRecipe(consumer, List.of(
                List.of(smoothBasalt, warpCore, smoothBasalt),
                List.of(smoothBasalt, chest, smoothBasalt),
                List.of(polishedBlackstone, polishedBlackstone, polishedBlackstone)
        ), ModBlocks.TRANSFER_NODE.get(), warpCore);


        // Mod Items
        setupRecipe(consumer, List.of(
                List.of(air, rawIron, air),
                List.of(rawIron, Items.SLIME_BALL, rawIron),
                List.of(air, rawIron, air)
        ), ModItems.UPGRADE_BASE.get(), Items.SLIME_BALL);

        setupRecipe(consumer, List.of(
                List.of(Items.WOODEN_SWORD, ironIngot, Items.GOLDEN_SWORD),
                List.of(Items.STONE_SWORD, ironBlock, Items.DIAMOND_SWORD),
                List.of(ironSword, ironIngot, Items.NETHERITE_SWORD)
        ), ModItems.SWORD_CORE.get(), Items.IRON_INGOT);

        setupRecipe(consumer, List.of(
                List.of(ironIngot, enderpearl, ironIngot),
                List.of(enderpearl, heartOfTheSea, enderpearl),
                List.of(ironIngot, enderpearl, ironIngot)
        ), ModItems.WARP_CORE.get(), heartOfTheSea);

        setupRecipe(consumer, List.of(
                List.of(air, ironNugget, air),
                List.of(air, warpCore, ironNugget),
                List.of(stick, air, air)
        ), ModItems.LINKING_WAND, ModItems.WARP_CORE);


        setupSmithingRecipe(consumer, ModItems.SWORD_UPGRADE, List.of(
                Ingredient.EMPTY,
                Ingredient.of(ModItems.UPGRADE_BASE),
                Ingredient.of(ModItems.SWORD_CORE)
        ), ModItems.UPGRADE_BASE);

        setupSmithingRecipe(consumer, ModItems.SPEED_UPGRADE, List.of(
                Ingredient.EMPTY,
                Ingredient.of(ModItems.UPGRADE_BASE),
                Ingredient.of(Items.SUGAR)
        ), ModItems.UPGRADE_BASE);

        setupSmithingRecipe(consumer, ModItems.LOOTING_UPGRADE, List.of(
                Ingredient.EMPTY,
                Ingredient.of(ModItems.UPGRADE_BASE),
                Ingredient.of(Items.LAPIS_BLOCK)
        ), ModItems.UPGRADE_BASE);
    }

    private void setupSmithingRecipe(RecipeOutput consumer, ItemLike output, List<Ingredient> input, ItemLike unlockedBy) {
        Ingredient template = input.getFirst();
        Ingredient base = input.get(1);
        Ingredient addition = input.getLast();

        SmithingTransformRecipeBuilder smithing = SmithingTransformRecipeBuilder.smithing(
                template,
                base,
                addition,
                RecipeCategory.MISC, output.asItem());
        smithing.unlocks("has_"+unlockedBy.asItem().getDescriptionId().substring(17), InventoryChangeTrigger.TriggerInstance.hasItems(unlockedBy));
        smithing.save(consumer, base.getItems()[0].getDescriptionId().substring(17) + "_to_"+output.asItem().getDescriptionId().substring(17));
    }

    private void setupRecipe (RecipeOutput consumer, List<List<ItemLike>> input, ItemLike output, ItemLike unlockedBy) {
        Map<ItemLike, Character> keyMap = new HashMap<>();
        AtomicInteger index = new AtomicInteger();
        input.forEach(items -> {
            for (ItemLike item : items) {
                if (item.asItem() == Items.AIR) continue;
                if (keyMap.containsKey(item)) continue;

                Character character = CHARACTER.charAt(index.getAndIncrement());
                keyMap.putIfAbsent(item, character);
            }
        });

        ShapedRecipeBuilder recipe = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output);
        recipe.group("mob_farming");
        input.forEach(items -> {
            StringBuilder key = new StringBuilder();
            for (ItemLike item : items) {
                if (item.asItem() == Items.AIR) {
                    key.append(" ");
                    continue;
                }
                key.append(keyMap.get(item));
            }

            recipe.pattern(key.toString());
        });
        keyMap.forEach((item, character) -> recipe.define(character, item));
        recipe.unlockedBy("has_"+unlockedBy.asItem().getDescriptionId().substring(15), InventoryChangeTrigger.TriggerInstance.hasItems(unlockedBy));
        recipe.save(consumer);
    }
}