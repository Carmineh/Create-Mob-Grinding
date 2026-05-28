package dev.manny.create_ks.event;

import dev.manny.create_ks.CreateKineticSpawning;
import dev.manny.create_ks.registry.ModDataComponents;
import dev.manny.create_ks.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = CreateKineticSpawning.MOD_ID)
public class SpawnerBreakEventHandler {

    public static final TagKey<EntityType<?>> BLACKLISTED_SPAWNERS = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(CreateKineticSpawning.MOD_ID, "blacklisted_spawners"));

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide() || event.isCanceled()) {
            return;
        }

        if (event.getState().is(Blocks.SPAWNER)) {
            BlockEntity be = event.getLevel().getBlockEntity(event.getPos());
            if (be instanceof SpawnerBlockEntity spawnerBE) {
                CompoundTag tag = spawnerBE.saveWithFullMetadata(event.getLevel().registryAccess());
                if (tag.contains("SpawnData", 10)) {
                    CompoundTag spawnData = tag.getCompound("SpawnData");
                    if (spawnData.contains("entity", 10)) {
                        CompoundTag entity = spawnData.getCompound("entity");
                        if (entity.contains("id", 8)) {
                            String id = entity.getString("id");
                            ResourceLocation entityLoc = ResourceLocation.parse(id);
                            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityLoc);
                            
                            if (type != null && !type.is(BLACKLISTED_SPAWNERS)) {
                                ItemStack chunk = new ItemStack(ModItems.MOB_SPAWNER_CHUNK.get());
                                chunk.set(ModDataComponents.SPAWNER_ENTITY.get(), entityLoc);
                                
                                if (event.getLevel() instanceof net.minecraft.world.level.Level level) {
                                    net.minecraft.world.level.block.Block.popResource(level, event.getPos(), chunk);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
