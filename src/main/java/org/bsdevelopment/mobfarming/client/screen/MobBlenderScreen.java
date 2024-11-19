package org.bsdevelopment.mobfarming.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.entity.MobBlenderBase;
import org.bsdevelopment.mobfarming.client.screen.widgets.ScreenPosition;
import org.bsdevelopment.mobfarming.client.screen.widgets.Widgets;
import org.bsdevelopment.mobfarming.container.MobBlenderContainer;
import org.bsdevelopment.mobfarming.utilities.ExperienceHandler;
import org.bsdevelopment.mobfarming.utilities.ModUtilities;

import java.awt.*;

public class MobBlenderScreen extends AbstractContainerScreen<MobBlenderContainer> {
    private final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/mob_blender_gui.png");
    private final MobBlenderBase mobBlender;
    private ScreenPosition EXPERIENCE_BAR;

    public MobBlenderScreen(MobBlenderContainer menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;

        mobBlender = menu.blockEntity;
    }

    @Override
    protected void init() {
        super.init();

        EXPERIENCE_BAR = new ScreenPosition(36, 30, Widgets.FILLED_EXPERIENCE_BAR);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        // Will render the sword slot when it has the upgrade
        mobBlender.SLOTS.forEach((integer, slotPosition) -> {
            Widgets.WidgetInfo slot = slotPosition.widgetInfo();
            if ((slot == Widgets.SWORD_SLOT) && menu.isSwordSlotDisabled()) {
                slot = Widgets.DISABLED_SLOT;
            }

            guiGraphics.blit(Widgets.WIDGETS_TEXTURE,
                    getGuiLeft() + (slotPosition.x()-1), getGuiTop() + (slotPosition.y() - 1),
                    slot.topX(), slot.topY(), slot.getWidth(1), slot.getHeight(1)
            );
        });

        renderXPBar(guiGraphics, partialTick, mouseX, mouseY);
    }

    public void renderXPBar(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int partialAmount = (int) (ExperienceHandler.getProgressToNextLevel(menu.getExperience()) * 183.0F);
        if (partialAmount > 0) {
            Widgets.WidgetInfo exp = Widgets.FILLED_EXPERIENCE_BAR;
            guiGraphics.blit(Widgets.WIDGETS_TEXTURE,
                    getGuiLeft() + EXPERIENCE_BAR.x(), getGuiTop() + EXPERIENCE_BAR.y(),
                    exp.topX(), exp.topY(), partialAmount, 5);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int pX, int pY) {
        Widgets.WidgetInfo swordSlot = Widgets.SWORD_SLOT;
        // graphics.renderOutline(SWORD_SLOT.x(), SWORD_SLOT.y(), slot.getWidth(1), slot.getHeight(1), Color.RED.getRGB());
        if (ModUtilities.inBounds(getGuiLeft() + mobBlender.SWORD_SLOT_POSITION.x(), getGuiTop() + mobBlender.SWORD_SLOT_POSITION.y(), swordSlot.getWidth(1), swordSlot.getHeight(1), pX, pY) && menu.isSwordSlotDisabled()) {
            graphics.renderTooltip(this.font, Component.literal("Slot Disabled").withColor(Color.RED.getRGB()), pX-45, pY-5);
        }


        Widgets.WidgetInfo experience = Widgets.FILLED_EXPERIENCE_BAR;
        // graphics.renderOutline(getGuiLeft() + EXPERIENCE_BAR.x(), getGuiTop() + EXPERIENCE_BAR.y(), experience.getWidth(2), experience.getHeight(1), Color.RED.getRGB());
        if (ModUtilities.inBounds(getGuiLeft() + EXPERIENCE_BAR.x(), getGuiTop() + EXPERIENCE_BAR.y(), experience.getWidth(), experience.getHeight(), pX, pY)) {
            String s = String.valueOf(ExperienceHandler.getLevelFromTotalExperience(menu.getExperience()));
            graphics.renderTooltip(this.font, Component.literal(s).withColor(Color.GREEN.getRGB()), pX, pY);
        }

        // Skip renderint the debug renders...
        if (true) {
            super.renderTooltip(graphics, pX, pY);
            return;
        }

        String s1 = ".";//String.valueOf(ExperienceHandler.getLevelFromTotalExperience(menu.getExperience()));
        int posX = (width / 2) - font.width(s1) / 2;
        graphics.renderOutline(posX, getGuiTop(), font.width(s1), imageHeight, Color.RED.getRGB());
        graphics.drawString(font, s1, posX, getGuiTop()+20, Color.RED.getRGB(), false);
        graphics.renderTooltip(this.font, Component.literal("posx:"+posX+" - px: "+(pX)+" - py:"+pY).withColor(Color.CYAN.getRGB()), pX, pY);
        super.renderTooltip(graphics, pX, pY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
