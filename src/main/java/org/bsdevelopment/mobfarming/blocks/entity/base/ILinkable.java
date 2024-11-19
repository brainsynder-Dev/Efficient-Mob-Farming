package org.bsdevelopment.mobfarming.blocks.entity.base;

import net.minecraft.core.BlockPos;

public interface ILinkable {
    void linkPosition (BlockPos pos);

    boolean isLinked();
}
