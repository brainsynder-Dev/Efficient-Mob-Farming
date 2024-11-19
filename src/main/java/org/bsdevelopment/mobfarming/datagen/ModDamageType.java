package org.bsdevelopment.mobfarming.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.bsdevelopment.mobfarming.ModConstants;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDamageType extends DatapackBuiltinEntriesProvider {
    public ModDamageType(PackOutput output, CompletableFuture<HolderLookup.Provider> lookUpThingIHate) {
        super(output, lookUpThingIHate, new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, context -> {
            context.register(ModConstants.SPIKE_TYPE, new DamageType("mob_spike", 1.0f));
        }), Set.of(ModConstants.MOD_ID));
    }
}