package org.bsdevelopment.mobfarming.items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.bsdevelopment.mobfarming.ModComponents;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.blocks.entity.base.ILinkable;
import org.bsdevelopment.mobfarming.component.LinkingMode;
import org.bsdevelopment.mobfarming.component.LinkingRecord;
import org.bsdevelopment.mobfarming.items.base.BaseItem;
import org.bsdevelopment.mobfarming.utilities.ModUtilities;

public class LinkingWandItem extends BaseItem {
    public LinkingWandItem(Properties properties) {
        super(properties);
    }

    public boolean isFirstSelectionTarget (BlockState blockState) {
        return blockState.is(ModBlocks.TRANSFER_NODE.get()) || blockState.is(ModBlocks.DESTINATION_PLATE.get());
    }

    public boolean isSecondSelectionTarget (BlockState targetState, BlockState currentState) {
        if (currentState.is(ModBlocks.TRANSFER_NODE.get()) && targetState.is(ModBlocks.MOB_BLENDER.get())) return true;
        if (currentState.is(ModBlocks.TRANSFER_NODE.get()) && targetState.is(ModBlocks.MOB_SPIKE.get())) return true;
        return currentState.is(ModBlocks.DESTINATION_PLATE.get()) && targetState.is(ModBlocks.WARP_PLATE.get());
    }

    public static LinkingRecord getLinkingWandComponent(ItemStack stack) {
        if (!stack.has(ModComponents.LINKAGE_DATA)) return null;
        return stack.get(ModComponents.LINKAGE_DATA);
    }

    public static void storeConnectionPos(ItemStack wand, Level level, BlockPos pos) {
        wand.set(ModComponents.LINKAGE_DATA, new LinkingRecord(GlobalPos.of(level.dimension(), pos), LinkingMode.CURRENT_POS));
    }

    public static GlobalPos getConnectionPos(ItemStack wand) {
        LinkingRecord record = getLinkingWandComponent(wand);
        return record == null ? null : record.storedPos();
    }

    public static void setMode(ItemStack wand, LinkingMode mode) {
        LinkingRecord record = getLinkingWandComponent(wand);
        if (record == null) return;
        record.setMode(mode);
    }

    public static LinkingMode getMode(ItemStack wand) {
        LinkingRecord record = getLinkingWandComponent(wand);
        return record == null ? LinkingMode.CURRENT_POS : record.mode();
    }

    private void playSound(Level level, BlockPos pos, float volume, float pitch) {
        level.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, volume, pitch);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack wand = context.getItemInHand();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (level.isClientSide()) return InteractionResult.SUCCESS;

        int range = 10;
        BlockHitResult lookingAt = ModUtilities.getLookingAt(player, ClipContext.Fluid.NONE, range);
        if (lookingAt == null) return InteractionResult.FAIL;

        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);

        LinkingRecord record = getLinkingWandComponent(wand);
        GlobalPos storedPos = getConnectionPos(wand);
        LinkingMode mode = getMode(wand);

        if ((storedPos == null) || (record == null)) {
            if (isFirstSelectionTarget(blockState)) {
                storeConnectionPos(wand, level, pos);
                if (blockState.is(ModBlocks.DESTINATION_PLATE.get())) setMode(wand, LinkingMode.CURRENT_POS);
                if (blockState.is(ModBlocks.TRANSFER_NODE.get())) setMode(wand, LinkingMode.INITIAL_POS);

                playSound(level, pos, 0.5F, 0.8F);
                player.displayClientMessage(Component.literal("First block set!"), true);
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(Component.literal("This block cannot be linked."), true);
                return InteractionResult.FAIL;
            }
        } else {
            // Stored position exists, attempt to link
            Level storedLevel = level.getServer().getLevel(storedPos.dimension());
            if (storedLevel == null) {
                player.displayClientMessage(Component.literal("Invalid stored dimension."), true);
                record.setStoredPos(null);
                return InteractionResult.FAIL;
            }
            BlockPos storedBlockPos = storedPos.pos();
            BlockState storedBlockState = storedLevel.getBlockState(storedBlockPos);

            if (isSecondSelectionTarget(blockState, storedBlockState)) {
                BlockEntity storedEntity = storedLevel.getBlockEntity(storedBlockPos);
                BlockEntity currentEntity = level.getBlockEntity(pos);

                if ((storedEntity == null || currentEntity == null) && (record.mode() == LinkingMode.INITIAL_POS)) {
                    player.displayClientMessage(Component.literal("Initially linked block is not found."), true);
                    record.setStoredPos(null);
                    return InteractionResult.FAIL;
                }

                if (mode == LinkingMode.CURRENT_POS) {
                    if (currentEntity instanceof ILinkable linkable) {
                        linkable.linkPosition(storedBlockPos);
                        playSound(level, pos, 0.5F, 1.2F);
                        player.displayClientMessage(Component.literal("Blocks linked! Data saved to source block."), true);
                    } else {
                        player.displayClientMessage(Component.literal("Source block cannot be linked."), true);
                        record.setStoredPos(null);
                        return InteractionResult.FAIL;
                    }
                } else if (mode == LinkingMode.INITIAL_POS) {
                    if (storedEntity instanceof ILinkable linkable) {
                        linkable.linkPosition(pos);
                        player.displayClientMessage(Component.literal("Blocks linked! Data saved to destination block."), true);
                    } else {
                        player.displayClientMessage(Component.literal("Destination block cannot be linked."), true);
                        record.setStoredPos(null);
                        return InteractionResult.FAIL;
                    }
                } else {
                    player.displayClientMessage(Component.literal("Invalid mode."), true);
                    record.setStoredPos(null);
                    return InteractionResult.FAIL;
                }

                record.setStoredPos(null);
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(Component.literal("These blocks cannot be linked."), true);
                record.setStoredPos(null);
                return InteractionResult.FAIL;
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack wand = player.getItemInHand(usedHand);

        if (player.isShiftKeyDown()) {
            // Toggle the mode
            LinkingRecord record = getLinkingWandComponent(wand);
            if (record != null) record.setStoredPos(null);
            player.displayClientMessage(Component.literal("Linking Wand selection cleared."), true);
            return InteractionResultHolder.success(wand);
        }

        return InteractionResultHolder.pass(wand);
    }
}


