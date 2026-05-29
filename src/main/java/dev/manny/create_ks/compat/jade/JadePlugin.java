package dev.manny.create_ks.compat.jade;

import dev.manny.create_ks.block.RotationalMobGrinderBlock;
import dev.manny.create_ks.block.RotationalMobSpawnerBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(SpawnerComponentProvider.INSTANCE, dev.manny.create_ks.block.entity.RotationalMobSpawnerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(SpawnerComponentProvider.INSTANCE, RotationalMobSpawnerBlock.class);
        registration.registerBlockComponent(GrinderComponentProvider.INSTANCE, RotationalMobGrinderBlock.class);
    }
}
