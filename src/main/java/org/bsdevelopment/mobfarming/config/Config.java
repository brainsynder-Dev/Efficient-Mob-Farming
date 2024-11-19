package org.bsdevelopment.mobfarming.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.bsdevelopment.mobfarming.ModConstants;

import java.util.UUID;

@EventBusSubscriber(modid = ModConstants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final String DEFAULT_UUID = "0081B470-52EF-4B1C-B8C1-711123DF669A";
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static UUID fakePlayerUUID = UUID.fromString(DEFAULT_UUID);

    public static boolean deathrootRedstoneUpdates;
    public static boolean deathrootParticles;

    public static double glidePlateVelocity;

    public static int maxLootingUpgrade = 10;
    public static int maxItemsPerLine;

    // Mob Blender
    private static final ModConfigSpec.ConfigValue<String> MOB_BLENDER_UUID = BUILDER
            .comment("This is the UUID of the FakePlayer who will be killing entities\n!!!! Do not change this unless you know what you are doing !!!!")
            .define("mob_blender.fake_player_uuid", DEFAULT_UUID, o -> {
                try {
                    UUID.fromString(String.valueOf(o));
                    return true;
                }catch (Exception e) {
                    return false;
                }
            });

    // Deathroot Dirt
    private static final ModConfigSpec.BooleanValue DEATHROOT_REDSTONE_UPDATES = BUILDER
            .comment("Should blocks getting updated near deathroot dirt cause mobs to spawn faster\nExample: Redstone clock")
            .define("deathroot.redstone_updates", true);

    private static final ModConfigSpec.BooleanValue DEATHROOT_PARTICLES = BUILDER
            .comment("Deathroot Dirt has some particle effects do you want them enabled?")
            .define("deathroot.particles", true);


    // Glide Plate
    private static final ModConfigSpec.DoubleValue GLIDE_PLATE_VELOCITY = BUILDER
            .comment("The GlidePlate will move mobs based on the velocity set here")
            .defineInRange("glide_plate.velocity", 0.2, 0.0, 5.0);

    // Upgrades
    private static final ModConfigSpec.IntValue MAX_LOOTING_UPGRADE = BUILDER
            .comment("The maximum amount of looting upgrade you can have")
            .defineInRange("upgrades.max_looting", 10, 0, 100);


    private static final ModConfigSpec.IntValue MAX_ITEMS_PER_LINE = BUILDER
            .comment("The maximum amount of items that can be displayed in a single line when jade is installed")
            .defineInRange("jade-support.items-per-line", 10, 0, 20);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    public static UUID getBlenderFakePlayerUUID() {
        String uuidString = MOB_BLENDER_UUID.get();
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            return UUID.fromString(DEFAULT_UUID);
        }
    }

    public static void bakeConfig() {
        fakePlayerUUID = getBlenderFakePlayerUUID();

        deathrootRedstoneUpdates = DEATHROOT_REDSTONE_UPDATES.get();
        deathrootParticles = DEATHROOT_PARTICLES.get();

        glidePlateVelocity = GLIDE_PLATE_VELOCITY.get();

        maxLootingUpgrade = MAX_LOOTING_UPGRADE.get();

        maxItemsPerLine = MAX_ITEMS_PER_LINE.get();
    }


    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
        if (!configEvent.getConfig().getModId().equals(ModConstants.MOD_ID)) return;
        bakeConfig();
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) {
        if (!configEvent.getConfig().getModId().equals(ModConstants.MOD_ID)) return;
        bakeConfig();
    }
}
