package org.bsdevelopment.mobfarming;

import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ModConstants {
    public static final String MOD_ID = "mob_farming";
    public static final Path CONFIG_DIR = FMLPaths.CONFIGDIR.get().resolve(MOD_ID);
}
