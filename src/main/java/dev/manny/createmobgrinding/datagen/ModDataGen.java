package dev.manny.createmobgrinding.datagen;

import dev.manny.createmobgrinding.CreateMobGrinding;
import dev.manny.createmobgrinding.event.SpawnerBreakEventHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = CreateMobGrinding.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModEntityTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
    }

    public static class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {

        public ModEntityTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, ExistingFileHelper existingFileHelper) {
            super(pOutput, pProvider, CreateMobGrinding.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(SpawnerBreakEventHandler.BLACKLISTED_SPAWNERS)
                    .add(EntityType.ENDER_DRAGON)
                    .add(EntityType.WITHER)
                    .add(EntityType.WARDEN);
        }
    }
}

