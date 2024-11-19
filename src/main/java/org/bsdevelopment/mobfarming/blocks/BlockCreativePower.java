package org.bsdevelopment.mobfarming.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.bsdevelopment.mobfarming.blocks.entity.CreativePowerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockCreativePower extends Block implements EntityBlock {

    public BlockCreativePower() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .pushReaction(PushReaction.BLOCK)
                .strength(1f, 1f)
                .sound(SoundType.STONE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CreativePowerBlockEntity(pos, state);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("Only for debug purposes").withStyle(ChatFormatting.RED));
    }
}
