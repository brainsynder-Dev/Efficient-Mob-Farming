package org.bsdevelopment.mobfarming.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.config.Config;

import javax.annotation.Nonnull;

public class BlockDeathrootDirt extends Block {
    public BlockDeathrootDirt(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!checkMobSpawning(level, pos)) return;

        level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20, 60));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (level.getDifficulty() == Difficulty.PEACEFUL) return;
        if (!Config.deathrootRedstoneUpdates) return;
        if (!checkMobSpawning(level, pos)) return;

        level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20, 60));
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (!checkMobSpawning(level, pos)) return;

        AABB areaToCheck = new AABB(pos).inflate(5, 2, 5);
        int entityCount = level.getEntitiesOfClass(LivingEntity.class, areaToCheck, entity -> ((entity instanceof Enemy) && (!(entity instanceof Player)))).size();

        if (entityCount > 8) return;

        ModConstants.MOB_SPAWNER.spawnMobs(level, pos);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        if (!Config.deathrootParticles) return;

        for (int i = 0; i < 4; ++i) {
            spawnParticleAbove(level, pos, rand, ParticleTypes.SMOKE);
        }
    }


    private void spawnParticleAbove(Level level, BlockPos pos, RandomSource random, ParticleOptions particle) {
        double d0 = (double)pos.getX() + random.nextDouble();
        double d1 = (double)pos.getY() + 0.05;
        double d2 = (double)pos.getZ() + random.nextDouble();
        level.addParticle(particle, d0, d1, d2, 0.0, 0.0, 0.0);
    }


    private boolean checkMobSpawning(Level level, BlockPos pos) {
        return level.getMaxLocalRawBrightness(pos.above()) < 5;
    }
}
