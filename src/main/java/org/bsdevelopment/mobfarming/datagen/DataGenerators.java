package org.bsdevelopment.mobfarming.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.bsdevelopment.mobfarming.ModConstants;

@EventBusSubscriber(modid = ModConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(event.includeClient(), new ModBlockStates(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ModItemModelData(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, "en_us"));

        generator.addProvider(event.includeClient(), new ModDamageType(packOutput, event.getLookupProvider()));
        generator.addProvider(event.includeClient(), new ModDamageTags(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModRecipes(packOutput, event.getLookupProvider()));
    }
}