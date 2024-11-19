package org.bsdevelopment.mobfarming.items.base;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.bsdevelopment.mobfarming.utilities.ModUtilities;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class BaseItem extends Item {
    public BaseItem(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.appendItemInformation(stack, context, components::add, flag.isAdvanced());
        super.appendHoverText(stack, context, components, flag);
    }

    public void appendItemInformation(ItemStack stack, Item.TooltipContext level, Consumer<Component> info, boolean advanced) {
        String key = getDescriptionId()+".description";
        LinkedList<Component> multiLine = ModUtilities.formatMultiLine(key);
        if (!multiLine.isEmpty()) {
            multiLine.forEach(component -> info.accept(component.copy().withStyle(ChatFormatting.GRAY)));
            return;
        }

        MutableComponent component = Component.translatable(key);
        if (component.getString().equals(key)) return;
        info.accept(component.copy().withStyle(ChatFormatting.GRAY));
    }
}
