package org.bsdevelopment.mobfarming.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.items.ModItems;

import java.util.List;


public class ModLanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, ModConstants.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add(ModItems.CREATIVE_SWORD_UPGRADE.get(), "Creative Sword Upgrade",
                new Translation("description", "Swords will be unbreakable while equipped")
        );

        add(ModItems.LOOTING_UPGRADE.get(), "Looting Upgrade",
                new Translation("description", "Adds an additional looting level")
        );
        add(ModItems.SPEED_UPGRADE.get(), "Speed Upgrade",
                new Translation("description", "Increases the speed of attacking mobs")
        );
        add(ModItems.SWORD_UPGRADE.get(), "Sword Upgrade",
                new Translation("description", "Unlocks the sword slot allowing you to equip custom swords")
        );

        add(ModItems.SWORD_CORE.get(), "Sword Core");
        add(ModItems.WARP_CORE.get(), "Warp Core");


        add(ModBlocks.MOB_BLENDER.get(), "Mob Blender",
                new Translation("fake_player", "Mob Blender")
        );
        add(ModBlocks.MOB_SPIKE.get(), "Mob Spike",
                new Translation("description.1", "Mobs will take damage when they fall on it"),
                new Translation("description.2", "They will also take damage when standing on it")
        );

        add(ModBlocks.DEATHROOT_DIRT.get(), "Deathroot Dirt");
        add(ModBlocks.GLIDE_PLATE.get(), "Glide Plate");
        add(ModBlocks.TRANSFER_NODE.get(), "Transfer Node");
        add(ModBlocks.CREATIVE_POWER.get(), "Creative Power (DEV)");
        add(ModBlocks.WARP_PLATE.get(), "Warp Plate");
        add(ModBlocks.DESTINATION_PLATE.get(), "Destination Plate");

        // Linking Wand
        add(ModItems.LINKING_WAND.get(), "Linking Wand",
                new Translation("linked", "Linked to %1$s in %2$s at (%3$d, %4$d, %5$d)"),

                new Translation("description.1", "Click on the block you want to link"),
                new Translation("description.2", "Then select the next destination"),
                new Translation("description.3", " "),
                new Translation("description.4", "Sneak and right click to cancel")
        );

        // Jade
        add("config.jade.plugin_mob_farming.mob_blender", "Mob Blender");
        add("config.jade.plugin_mob_farming.mob_spike", "Mob Spike");
    }

    public void add(Item key, String name, Translation... translations) {
        add(key, name);
        List.of(translations).forEach(translation -> add(key.getDescriptionId()+"."+translation.key, translation.value));
    }
    public void add(Block key, String name, Translation... translations) {
        add(key, name);
        List.of(translations).forEach(translation -> add(key.getDescriptionId()+"."+translation.key, translation.value));
    }

    private record Translation(String key, String value) {}
}