package org.bsdevelopment.mobfarming.utilities.fakeplayer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.bsdevelopment.mobfarming.ModConstants;

import java.lang.ref.WeakReference;

public class CustomFakePlayer extends FakePlayer {
    private static CustomFakePlayer INSTANCE;

    protected BlockPos blockPos = new BlockPos(0, 0, 0);

    public CustomFakePlayer(ServerLevel level) {
        super(level, ModConstants.FAKE_PROFILE);
    }

    public static WeakReference<CustomFakePlayer> get(ServerLevel level) {
        if (INSTANCE == null) INSTANCE = new CustomFakePlayer(level);

        INSTANCE.setLevel(level);
        return new WeakReference<>(INSTANCE);
    }

    public static WeakReference<CustomFakePlayer> get(ServerLevel level, BlockPos blockPos) {
        if (INSTANCE == null) INSTANCE = new CustomFakePlayer(level);

        INSTANCE.setLevel(level);
        INSTANCE.blockPos = blockPos;
        return new WeakReference<>(INSTANCE);
    }

    public static WeakReference<CustomFakePlayer> get(ServerLevel level, BlockPos blockPos, double x, double y, double z) {
        if (INSTANCE == null) {
            INSTANCE = new CustomFakePlayer(level);
        }

        INSTANCE.setLevel(level);
        INSTANCE.setPos(x,y,z);
        INSTANCE.blockPos = blockPos;
        return new WeakReference<>(INSTANCE);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return false;
    }

    public static void unload(LevelAccessor world) {
        if ((INSTANCE != null) && (INSTANCE.level() == world)) INSTANCE = null;
    }

    @Override
    public BlockPos blockPosition() {
        return this.blockPos;
    }
}
