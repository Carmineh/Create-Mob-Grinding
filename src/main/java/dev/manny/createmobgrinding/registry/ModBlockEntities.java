package dev.manny.createmobgrinding.registry;

import dev.manny.createmobgrinding.CreateMobGrinding;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateMobGrinding.MOD_ID);

    public static final Supplier<BlockEntityType<dev.manny.createmobgrinding.block.entity.RotationalMobSpawnerBlockEntity>> ROTATIONAL_MOB_SPAWNER = BLOCK_ENTITIES.register("rotational_mob_spawner",
            () -> BlockEntityType.Builder.of(dev.manny.createmobgrinding.block.entity.RotationalMobSpawnerBlockEntity::new, ModBlocks.ROTATIONAL_MOB_SPAWNER.get()).build(null));

    public static final Supplier<BlockEntityType<dev.manny.createmobgrinding.block.entity.RotationalMobGrinderBlockEntity>> ROTATIONAL_MOB_GRINDER = BLOCK_ENTITIES.register("rotational_mob_grinder",
            () -> BlockEntityType.Builder.of(dev.manny.createmobgrinding.block.entity.RotationalMobGrinderBlockEntity::new, ModBlocks.ROTATIONAL_MOB_GRINDER.get()).build(null));
}

