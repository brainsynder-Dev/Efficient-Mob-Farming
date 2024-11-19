package org.bsdevelopment.mobfarming.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.bsdevelopment.mobfarming.ModConstants;
import org.bsdevelopment.mobfarming.blocks.BlockMobBlender;
import org.bsdevelopment.mobfarming.blocks.ModBlocks;
import org.bsdevelopment.mobfarming.blocks.entity.base.IStorage;
import org.bsdevelopment.mobfarming.blocks.entity.base.InventoryBlockEntity;
import org.bsdevelopment.mobfarming.client.screen.widgets.Widgets;
import org.bsdevelopment.mobfarming.container.SlotPosition;
import org.bsdevelopment.mobfarming.items.ModItems;
import org.bsdevelopment.mobfarming.utilities.ModUtilities;
import org.bsdevelopment.mobfarming.utilities.fakeplayer.CustomFakePlayer;
import org.bsdevelopment.mobfarming.utilities.storage.BulkStorage;

import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MobBlenderBase extends InventoryBlockEntity implements IStorage {
    private WeakReference<CustomFakePlayer> fakeReference = new WeakReference<>(null);
    public boolean active, disabledSwordSlot = true;
    public int animationTicks, prevAnimationTicks;
    private BulkStorage bulkStorage = new BulkStorage();
    private BigInteger storedExperience = BigInteger.valueOf(0);

    private final ItemStack defaultSwordItem = Items.NETHERITE_SWORD.getDefaultInstance();
    private ItemStack swordItem = defaultSwordItem;
    private boolean hasCustomSword = false;

    public final int SWORD_SLOT = 0;
    public final SlotPosition SWORD_SLOT_POSITION = new SlotPosition(152, 6, Widgets.SWORD_SLOT);

    public final Map<Integer, SlotPosition> SLOTS = Map.of(
            SWORD_SLOT, SWORD_SLOT_POSITION,
            1, new SlotPosition(152, 24, Widgets.UPGRADE_SLOT),
            2, new SlotPosition(152, 42, Widgets.UPGRADE_SLOT),
            3, new SlotPosition(152, 60, Widgets.UPGRADE_SLOT)
    );
    private final int minAttackCooldown = 1;
    private final int maxBaseAttackCooldown = 11;
    private int maxAttackCooldown = maxBaseAttackCooldown;

    private final Map<UUID, Integer> entityCooldowns = new HashMap<>();

    public MobBlenderBase(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.MOB_BLENDER.getTileEntityType(), 4, blockPos, blockState);
        defaultSwordItem.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
    }

    public void setActive(boolean isActive) {
        active = isActive;
        getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
    }

    public void addStoredExperience(long experience) {
        this.storedExperience = this.storedExperience.add(BigInteger.valueOf(experience));
        setChanged();
    }

    public BigInteger getStoredExperience() {
        return storedExperience;
    }

    public ItemStack getSwordItem() {
        if (!hasCustomSword) return ItemStack.EMPTY;
        return swordItem;
    }

    public void setSwordItem(ItemStack stack) {
        hasCustomSword = !stack.isEmpty();
        swordItem = (hasCustomSword) ? stack : defaultSwordItem;
        setChanged();
    }

    public void addUpgrade(ItemStack upgrade) {
        // If we need to add checks we can do it here
        updateUpgrades();
    }

    private void updateUpgrades() {
        List<Map.Entry<Integer, SlotPosition>> slots = getSlotsFromWidget(Widgets.UPGRADE_SLOT);
        disabledSwordSlot = (slots.stream().noneMatch(entry -> (getItem(entry.getKey()).is(ModItems.SWORD_UPGRADE.get()) || getItem(entry.getKey()).is(ModItems.CREATIVE_SWORD_UPGRADE.get()))));

        slots.forEach(entry -> {
            int index = entry.getKey();
            ItemStack item = getItem(index);

            // Update the maxAttackCooldown based on how many speed upgrades are in the slot
            if (item.is(ModItems.SPEED_UPGRADE.get())) this.maxAttackCooldown = ( this.maxBaseAttackCooldown - item.getCount() );
        });
    }

    public List<Map.Entry<Integer, SlotPosition>> getSlotsFromWidget(Widgets.WidgetInfo widget) {
        List<Map.Entry<Integer, SlotPosition>> list = new ArrayList<>();
        SLOTS.forEach((integer, slotPosition) -> {
            if (slotPosition.widgetInfo() == widget) {
                list.add(Map.entry(integer, slotPosition));
            }
        });
        return list;
    }

    protected void activateBlock() {
        List<LivingEntity> list = getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(
                worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                worldPosition.getX() + 1D, worldPosition.getY() + 1D, worldPosition.getZ() + 1D).inflate(0.0625D, 0.0625D, 0.0625D));

        if (!entityCooldowns.isEmpty()) {
            // Decrement individual entity cooldowns
            entityCooldowns.replaceAll((uuid, cooldown) -> cooldown - 1);
            // Remove entities that have reached their cooldown
            entityCooldowns.entrySet().removeIf(entry -> entry.getValue() <= 0);
        }

        int cooldown = (level.random.nextInt((maxAttackCooldown - minAttackCooldown) + 1) + minAttackCooldown);

        CustomFakePlayer fakePlayer = fakeReference.get();
        // Verify that there is only one fake player reference
        if (fakePlayer == null) {
            fakePlayer = CustomFakePlayer.get((ServerLevel) level, worldPosition).get();
            fakePlayer.getPersistentData().putString(ModConstants.MOD_ID, worldPosition.toShortString());
            fakePlayer.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(0);
            fakePlayer.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(20);
            fakePlayer.setPos(this.worldPosition.getX(), -200D, this.worldPosition.getZ());

            fakeReference = new WeakReference<>(fakePlayer);
        }

        ItemStack swordItem = this.swordItem.copy(); // Clone the sword item
        List<Map.Entry<Integer, SlotPosition>> slots = getSlotsFromWidget(Widgets.UPGRADE_SLOT);
        boolean hasCreativeSword = slots.stream().anyMatch(entry -> getItem(entry.getKey()).is(ModItems.CREATIVE_SWORD_UPGRADE.get()));
        if (hasCreativeSword) {
            // Clone the sword to make it unbreakable without making the actual item unbreakable
            swordItem.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, swordItem);
        }

        slots.forEach(entry -> {
            int index = entry.getKey();
            ItemStack item = getItem(index);

            // Update the maxAttackCooldown based on how many speed upgrades are in the slot
            if (item.is(ModItems.LOOTING_UPGRADE.get())) {
                int defaultLevel = this.swordItem.getEnchantmentLevel(ModUtilities.toHolder(Enchantments.LOOTING, level));
                swordItem.enchant(ModUtilities.toHolder(Enchantments.LOOTING, level), defaultLevel + item.getCount());
            }
        });


        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, swordItem);


        for (LivingEntity entity : list) {
            if (entity == null) continue;
            if (entityCooldowns.containsKey(entity.getUUID())) continue;

            fakePlayer.attack(entity);
            fakePlayer.getCooldowns().removeCooldown(swordItem.getItem());
            fakePlayer.resetAttackStrengthTicker();
            // set the cooldown for the entity
            entityCooldowns.put(entity.getUUID(), cooldown);
        }

        if (!hasCreativeSword) {
            ItemStack handItem = fakePlayer.getItemInHand(InteractionHand.MAIN_HAND);
            this.swordItem.setDamageValue(handItem.getDamageValue());
            setSwordItem(this.swordItem);
        }

        // set the item in the player's hand to empty
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }

    @Override
    public void saveStorage() {
        setChanged();
    }

    @Override
    public BulkStorage getBulkStorage() {
        return bulkStorage;
    }

    @Override
    public void loadData(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadData(nbt, registries);
        active = nbt.getBoolean("active");
        storedExperience = BigInteger.valueOf(nbt.getLong("experience"));
        bulkStorage = BulkStorage.CODEC.codec().parse(NbtOps.INSTANCE, nbt.get("storage")).result().orElseGet(BulkStorage::new);

        ItemStack weapon = getItem(SWORD_SLOT);
        if (!weapon.isEmpty()) this.swordItem = weapon;

        updateUpgrades();
    }

    @Override
    public void saveData(CompoundTag nbt, HolderLookup.Provider registries) {
        populateCompound(nbt);
        super.saveData(nbt, registries);
    }

    public void populateCompound(CompoundTag nbt) {
        nbt.putBoolean("active", active);
        nbt.putLong("experience", storedExperience.longValue());
        nbt.put("storage", BulkStorage.CODEC.codec().encodeStart(NbtOps.INSTANCE, bulkStorage).getOrThrow());
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if (t instanceof MobBlenderBase tile) {
            if (level.getGameTime() % 10 == 0 && level.getBlockState(blockPos).getBlock() instanceof BlockMobBlender)
                if (level.getBlockState(blockPos).getValue(BlockMobBlender.POWERED)) tile.activateBlock();
        }
    }

    public static <T extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if (t instanceof MobBlenderBase tile) {
            if (tile.active) {
                tile.prevAnimationTicks = tile.animationTicks;
                if (tile.animationTicks < 360)
                    tile.animationTicks += 18;
                if (tile.animationTicks >= 360) {
                    tile.animationTicks -= 360;
                    tile.prevAnimationTicks -= 360;
                }
            } else
                tile.prevAnimationTicks = tile.animationTicks = 0;
        }
    }
}
