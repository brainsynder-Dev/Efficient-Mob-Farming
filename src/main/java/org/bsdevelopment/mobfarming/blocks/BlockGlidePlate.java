package org.bsdevelopment.mobfarming.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.bsdevelopment.mobfarming.blocks.base.BlockBasePlate;
import org.bsdevelopment.mobfarming.config.Config;

public class BlockGlidePlate extends BlockBasePlate {
    public BlockGlidePlate(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.isCrouching()) return;
        if (entity.getY() <= (double) pos.getY() + 0.4d) {
            double velocity = Config.glidePlateVelocity;
            System.out.println("GlidePlate Velocity: " + velocity);

            Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            double deltaX = (velocity * (direction.getStepX() * 1.5));
            double deltaZ = (velocity * (direction.getStepZ() * 1.5));

            entity.setDeltaMovement(entity.getDeltaMovement().add(deltaX, 0, deltaZ));
        }
    }
}
