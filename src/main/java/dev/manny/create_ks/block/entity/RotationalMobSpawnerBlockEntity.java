package dev.manny.create_ks.block.entity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;

public class RotationalMobSpawnerBlockEntity extends KineticBlockEntity {

    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    };

    private float spawnProgress = 0;
    private static final float SPAWN_THRESHOLD = 5000f; // Configurable later

    public RotationalMobSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(dev.manny.create_ks.registry.ModBlockEntities.ROTATIONAL_MOB_SPAWNER.get(), pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) return;

        float speed = Math.abs(getSpeed());
        if (speed == 0) return;

        ItemStack spawnerChunk = inventory.getStackInSlot(0);
        if (spawnerChunk.isEmpty()) return;

        ResourceLocation entityLoc = spawnerChunk.get(dev.manny.create_ks.registry.ModDataComponents.SPAWNER_ENTITY.get());
        if (entityLoc == null) return;

        spawnProgress += speed;

        if (spawnProgress >= SPAWN_THRESHOLD) {
            spawnProgress -= SPAWN_THRESHOLD;
            spawnMob(entityLoc);
        }
    }

    private void spawnMob(ResourceLocation entityLoc) {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityLoc);
        if (type != null) {
            ServerLevel serverLevel = (ServerLevel) level;
            
            // Basic area check to prevent too many entities
            long count = serverLevel.getEntities(type, new AABB(worldPosition).inflate(4), Entity::isAlive).size();
            if (count >= 16) {
                return; // Cap reached
            }

            double x = worldPosition.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 2;
            double y = worldPosition.getY() + 1;
            double z = worldPosition.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 2;

            Entity entity = type.create(serverLevel);
            if (entity instanceof Mob mob) {
                mob.moveTo(x, y, z, level.random.nextFloat() * 360F, 0.0F);
                mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(worldPosition), MobSpawnType.SPAWNER, null);
                serverLevel.addFreshEntityWithPassengers(entity);
                serverLevel.levelEvent(2004, worldPosition, 0); // Spawn particles
            }
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
        }
        spawnProgress = compound.getFloat("SpawnProgress");
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("Inventory", inventory.serializeNBT(registries));
        compound.putFloat("SpawnProgress", spawnProgress);
    }
}
