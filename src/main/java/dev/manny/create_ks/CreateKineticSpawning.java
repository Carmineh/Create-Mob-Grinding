package dev.manny.create_ks;

import com.mojang.logging.LogUtils;
import dev.manny.create_ks.registry.ModBlockEntities;
import dev.manny.create_ks.registry.ModBlocks;
import dev.manny.create_ks.registry.ModDataComponents;
import dev.manny.create_ks.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CreateKineticSpawning.MOD_ID)
public class CreateKineticSpawning {
    public static final String MOD_ID = "create_ks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateKineticSpawning(IEventBus modEventBus) {
        ModDataComponents.COMPONENTS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
    }
}
