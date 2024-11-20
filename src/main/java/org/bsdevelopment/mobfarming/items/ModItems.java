package org.bsdevelopment.mobfarming.items;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.items.base.UpgradeItem;
import org.bsdevelopment.mobfarming.items.custom.LinkingWandItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModConstants.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModConstants.MOD_ID);

    public static DeferredItem<Item> BLENDER_BLADE1 = ITEMS.register("blender_blade1", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> BLENDER_BLADE2 = ITEMS.register("blender_blade2", () -> new Item(new Item.Properties()));

    public static DeferredItem<Item> LINKING_WAND = ITEMS.register("linking_wand", () -> new LinkingWandItem(new Item.Properties().stacksTo(1)));

    // Items used for crafting
    public static DeferredItem<Item> SWORD_CORE = ITEMS.register("sword_core", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> WARP_CORE = ITEMS.register("warp_core", () -> new Item(new Item.Properties()));

    // Upgrades
    public static DeferredItem<Item> UPGRADE_BASE = ITEMS.register("upgrade_base", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> CREATIVE_SWORD_UPGRADE = ITEMS.register("creative_sword_upgrade", () -> new UpgradeItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static DeferredItem<Item> SWORD_UPGRADE = ITEMS.register("sword_upgrade", () -> new UpgradeItem(new Item.Properties()));
    public static DeferredItem<Item> SPEED_UPGRADE = ITEMS.register("speed_upgrade", () -> new UpgradeItem(new Item.Properties()));
    public static DeferredItem<Item> LOOTING_UPGRADE = ITEMS.register("looting_upgrade", () -> new UpgradeItem(new Item.Properties()));


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_CREATIVE_TAB = CREATIVE_MODE_TABS.register(ModConstants.MOD_ID,
            () -> CreativeModeTab.builder().title(Component.literal("Mob Farming"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModBlocks.MOB_BLENDER.getItem().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        if ((Minecraft.getInstance().player != null)
                                && Minecraft.getInstance().player.getDisplayName().getString().equals("Dev")) {
                            // Only show these items if in a dev environment
                            output.accept(ModBlocks.CREATIVE_POWER.getItem());
                        }
                        output.accept(ModBlocks.MOB_SPIKE.getItem());
                        output.accept(ModBlocks.MOB_BLENDER.getItem());
                        output.accept(ModBlocks.DEATHROOT_DIRT.getItem());
                        output.accept(ModBlocks.TRANSFER_NODE.getItem());
                        output.accept(LINKING_WAND.get());
                        output.accept(ModBlocks.GLIDE_PLATE.getItem());
                        output.accept(ModBlocks.WARP_PLATE.getItem());
                        output.accept(ModBlocks.DESTINATION_PLATE.getItem());

                        output.accept(WARP_CORE.get());
                        output.accept(SWORD_CORE.get());
                        output.accept(UPGRADE_BASE.get());
                        output.accept(SWORD_UPGRADE.get());
                        output.accept(SPEED_UPGRADE.get());
                        output.accept(LOOTING_UPGRADE.get());
                        output.accept(CREATIVE_SWORD_UPGRADE.get());
                    }).build());


    public static void register(IEventBus evt) {
        ITEMS.register(evt);
        CREATIVE_MODE_TABS.register(evt);
    }
}
