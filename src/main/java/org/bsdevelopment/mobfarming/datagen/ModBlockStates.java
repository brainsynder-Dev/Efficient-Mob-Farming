package org.bsdevelopment.mobfarming.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;

public class ModBlockStates extends BlockStateProvider {
    public ModBlockStates(PackOutput output, ExistingFileHelper helper) {
        super(output, ModConstants.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.CREATIVE_POWER.getBlock());
    }
}