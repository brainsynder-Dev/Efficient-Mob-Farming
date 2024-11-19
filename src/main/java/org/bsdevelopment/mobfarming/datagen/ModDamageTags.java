package org.bsdevelopment.mobfarming.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.bsdevelopment.mobfarming.ModConstants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModDamageTags extends DamageTypeTagsProvider {
    public ModDamageTags(PackOutput output, CompletableFuture<HolderLookup.Provider> stupid, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, stupid, ModConstants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider useless) {
        tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "mob_spike"));
    }
}