package dev.manny.create_ks.registry;

import dev.manny.create_ks.CreateKineticSpawning;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateKineticSpawning.MOD_ID);

    public static final Supplier<Block> ROTATIONAL_MOB_SPAWNER = BLOCKS.register("rotational_mob_spawner",
        () -> new dev.manny.create_ks.block.RotationalMobSpawnerBlock(
                net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
                        .mapColor(net.minecraft.world.level.material.MapColor.METAL)
                        .requiresCorrectToolForDrops()
                        .strength(3.0F, 6.0F)
                        .noOcclusion()
        )
    );

    public static final Supplier<Block> ROTATIONAL_MOB_GRINDER = BLOCKS.register("rotational_mob_grinder",
        () -> new dev.manny.create_ks.block.RotationalMobGrinderBlock(
                net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
                        .mapColor(net.minecraft.world.level.material.MapColor.METAL)
                        .requiresCorrectToolForDrops()
                        .strength(3.0F, 6.0F)
                        .noOcclusion()
        )
    );
}
