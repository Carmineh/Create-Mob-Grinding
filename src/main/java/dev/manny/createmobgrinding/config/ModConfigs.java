package dev.manny.createmobgrinding.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigs {
    public static final CommonConfig COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static void register(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
    }
}
