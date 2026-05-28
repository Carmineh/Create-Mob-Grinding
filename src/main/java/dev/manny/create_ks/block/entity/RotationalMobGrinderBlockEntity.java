package dev.manny.create_ks.block.entity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import java.util.List;

public class RotationalMobGrinderBlockEntity extends KineticBlockEntity {

    private ItemStack internalWeapon = new ItemStack(net.minecraft.world.item.Items.DIAMOND_SWORD);
    private float attackTimer = 0;
    private static final float ATTACK_THRESHOLD = 2000f;

    public RotationalMobGrinderBlockEntity(BlockPos pos, BlockState state) {
        super(dev.manny.create_ks.registry.ModBlockEntities.ROTATIONAL_MOB_GRINDER.get(), pos, state);
    }

    public boolean applyEnchantedBook(ItemStack book) {
        if (!book.is(net.minecraft.world.item.Items.ENCHANTED_BOOK)) return false;
        
        ItemEnchantments bookEnchants = book.getOrDefault(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (bookEnchants.isEmpty()) return false;
        
        ItemEnchantments existingEnchants = internalWeapon.getOrDefault(net.minecraft.core.component.DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(existingEnchants);
        
        bookEnchants.keySet().forEach(ench -> {
            int current = mutable.getLevel(ench);
            int added = bookEnchants.getLevel(ench);
            if (current == added) mutable.set(ench, current + 1);
            else mutable.set(ench, Math.max(current, added));
        });
        
        EnchantmentHelper.setEnchantments(internalWeapon, mutable.toImmutable());
        setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        return true;
    }
    
    public ItemStack extractEnchantments() {
        ItemEnchantments enchants = internalWeapon.getOrDefault(net.minecraft.core.component.DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (enchants.isEmpty()) return ItemStack.EMPTY;
        
        ItemStack book = new ItemStack(net.minecraft.world.item.Items.ENCHANTED_BOOK);
        book.set(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS, enchants);
        
        internalWeapon = new ItemStack(net.minecraft.world.item.Items.DIAMOND_SWORD);
        setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        
        return book;
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) return;

        float speed = Math.abs(getSpeed());
        if (speed == 0) return;

        attackTimer += speed;

        if (attackTimer >= ATTACK_THRESHOLD) {
            attackTimer -= ATTACK_THRESHOLD;
            performAttack();
        }
    }

    private void performAttack() {
        ServerLevel serverLevel = (ServerLevel) level;
        AABB killZone = new AABB(worldPosition).inflate(1.5);
        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, killZone, e -> !(e instanceof Player) && e.isAlive());

        if (targets.isEmpty()) return;

        FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(serverLevel);
        fakePlayer.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
        fakePlayer.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, internalWeapon);

        for (LivingEntity target : targets) {
            fakePlayer.attack(target);
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        if (compound.contains("Weapon")) {
            internalWeapon = ItemStack.parseOptional(registries, compound.getCompound("Weapon"));
        }
        attackTimer = compound.getFloat("AttackTimer");
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("Weapon", internalWeapon.saveOptional(registries));
        compound.putFloat("AttackTimer", attackTimer);
    }
}
