package org.bsdevelopment.mobfarming.client.screen.widgets;

import net.minecraft.resources.ResourceLocation;
import org.bsdevelopment.mobfarming.ModConstants;

public interface Widgets {
    ResourceLocation WIDGETS_TEXTURE = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/widgets.png");

    // Slots
    WidgetInfo EMPTY_SLOT = new WidgetInfo(238, 0, 255, 17);
    WidgetInfo DISABLED_SLOT = new WidgetInfo(219, 0, 236, 17);
    WidgetInfo SWORD_SLOT = new WidgetInfo(219, 19, 236, 36);
    WidgetInfo UPGRADE_SLOT = new WidgetInfo(238, 19, 255, 36);

    // FE Power Bar
    WidgetInfo POWER_BACKGROUND = new WidgetInfo(110, 0, 127, 51);
    WidgetInfo POWER_FILLED = new WidgetInfo(93, 1, 108, 50);

    // Horizontal Progress Bar
    WidgetInfo PROGRESS_BAR_BACKGROUND = new WidgetInfo(0, 0, 51, 3);
    WidgetInfo PROGRESS_BAR_FILLED = new WidgetInfo(0, 5, 51, 8);

    // Flame Progress Bar
    WidgetInfo FUEL_BACKGROUND = new WidgetInfo(78, 0, 91, 13);
    WidgetInfo FUEL_BURNED = new WidgetInfo(78, 15, 91, 28);

    // Right Arrow Progress Bar
    WidgetInfo RIGHT_ARROW_BACKGROUND = new WidgetInfo(53, 0, 76, 16);
    WidgetInfo RIGHT_ARROW_FILLED = new WidgetInfo(53, 19, 76, 35);

    // Left Arrow Progress Bar
    WidgetInfo LEFT_ARROW_BACKGROUND = new WidgetInfo(53, 37, 76, 53);
    WidgetInfo LEFT_ARROW_FILLED = new WidgetInfo(53, 56, 76, 71);

    // Up Arrow Progress Bar
    WidgetInfo UP_ARROW_BACKGROUND = new WidgetInfo(1, 10, 17, 33);
    WidgetInfo UP_ARROW_FILLED = new WidgetInfo(20, 10, 36, 33);

    // Down Arrow Progress Bar
    WidgetInfo DOWN_ARROW_BACKGROUND = new WidgetInfo(1, 35, 17, 58);
    WidgetInfo DOWN_ARROW_FILLED = new WidgetInfo(20, 35, 36, 58);


    WidgetInfo SMALL_RIGHT_ACTIVE = new WidgetInfo(134, 0, 143, 14);
    WidgetInfo SMALL_RIGHT_HOVER = new WidgetInfo(146, 0, 155, 14);
    WidgetInfo SMALL_RIGHT_DISABLED = new WidgetInfo(158, 0, 167, 14);

    WidgetInfo SMALL_LEFT_ACTIVE = new WidgetInfo(134, 16, 143, 30);
    WidgetInfo SMALL_LEFT_HOVER = new WidgetInfo(146, 16, 155, 30);
    WidgetInfo SMALL_LEFT_DISABLED = new WidgetInfo(158, 16, 167, 30);

    WidgetInfo FILLED_EXPERIENCE_BAR = new WidgetInfo(78, 59, 179, 63);


    WidgetInfo PLUS_ONE = new WidgetInfo(122, 65, 135, 78);
    WidgetInfo PLUS_FIVE = new WidgetInfo(107, 65, 120, 78);
    WidgetInfo PLUS_TEN = new WidgetInfo(92, 65, 105, 78);

    WidgetInfo PLUS_ONE_HOVER = new WidgetInfo(122, 80, 135, 93);
    WidgetInfo PLUS_FIVE_HOVER = new WidgetInfo(107, 80, 120, 93);
    WidgetInfo PLUS_TEN_HOVER = new WidgetInfo(92, 80, 105, 93);

    record WidgetInfo(int topX, int topY, int bottomX, int bottomY) {
        public int getWidth() {
            return bottomX - topX;
        }

        public int getHeight() {
            return bottomY - topY;
        }
        public int getWidth(int offset) {
            return bottomX - (topX-offset);
        }

        public int getHeight(int offset) {
            return bottomY - (topY-offset);
        }
    }
}
