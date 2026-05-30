package dev.manny.createmobgrinding.registry;

import dev.manny.createmobgrinding.CreateMobGrinding;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModTags {
    public static final TagKey<EntityType<?>> TIER_1 = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(CreateMobGrinding.MOD_ID, "tier_1"));
    public static final TagKey<EntityType<?>> TIER_2 = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(CreateMobGrinding.MOD_ID, "tier_2"));
    public static final TagKey<EntityType<?>> TIER_3 = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(CreateMobGrinding.MOD_ID, "tier_3"));
    public static final TagKey<EntityType<?>> TIER_4 = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(CreateMobGrinding.MOD_ID, "tier_4"));
    public static final TagKey<EntityType<?>> TIER_5 = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(CreateMobGrinding.MOD_ID, "tier_5"));
}
