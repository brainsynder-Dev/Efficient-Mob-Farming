package org.bsdevelopment.mobfarming.client.screen.widgets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.bsdevelopment.mobfarming.utilities.ModUtilities;

public class BaseButton extends Button {
    protected Component localization = Component.empty();

    protected BaseButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
    }

    public Component getLocalization() {
        return localization;
    }

    public Component getLocalization(int mouseX, int mouseY) {
        if (ModUtilities.inBounds(getX(), getY(), getWidth(), getHeight(), mouseX, mouseY))
            return getLocalization();
        return Component.empty();
    }
}