package dev.manny.create_ks.registry;

import dev.manny.create_ks.CreateKineticSpawning;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateKineticSpawning.MOD_ID);

    public static final Supplier<Item> MOB_SPAWNER_CHUNK = ITEMS.register("mob_spawner_chunk", 
        () -> new dev.manny.create_ks.item.MobSpawnerChunkItem(new Item.Properties()));
        
    public static final Supplier<Item> ROTATIONAL_MOB_SPAWNER = ITEMS.register("rotational_mob_spawner",
        () -> new net.minecraft.world.item.BlockItem(ModBlocks.ROTATIONAL_MOB_SPAWNER.get(), new Item.Properties()));

    public static final Supplier<Item> ROTATIONAL_MOB_GRINDER = ITEMS.register("rotational_mob_grinder",
        () -> new net.minecraft.world.item.BlockItem(ModBlocks.ROTATIONAL_MOB_GRINDER.get(), new Item.Properties()));
}
