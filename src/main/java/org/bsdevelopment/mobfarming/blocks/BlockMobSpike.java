package org.bsdevelopment.mobfarming.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.NeoForge;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.entity.MobSpikeBlockEntity;
import org.bsdevelopment.mobfarming.event.SpikeDamageEntityEvent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class BlockMobSpike extends Block implements EntityBlock {
    public static final float FALL_DAMAGE_MULTIPLIER = 2.5F;

    public BlockMobSpike(Properties properties) {
        super(properties);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!(entity instanceof LivingEntity livingEntity)) return;
        SpikeDamageEntityEvent event = new SpikeDamageEntityEvent(livingEntity, this, pos);
        NeoForge.EVENT_BUS.post(event);
        entity.causeFallDamage(fallDistance + 3.0F, FALL_DAMAGE_MULTIPLIER, ModConstants.getSpikeDamage(level));
    }

    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Stream.of(
                Block.box(1, 0, 1, 15, 3, 15),
                Block.box(2, 3, 2, 14, 5, 14),
                Block.box(3, 5, 3, 13, 7, 13),
                Block.box(4, 7, 4, 12, 9, 12),
                Block.box(5, 9, 5, 11, 11, 11),
                Block.box(6, 11, 6, 10, 13, 10),
                Block.box(7, 13, 7, 9, 16, 9)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MobSpikeBlockEntity(pos, state);
    }
}
