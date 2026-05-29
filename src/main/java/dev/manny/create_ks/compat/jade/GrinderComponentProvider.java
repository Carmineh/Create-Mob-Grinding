package dev.manny.create_ks.compat.jade;

import dev.manny.create_ks.block.entity.RotationalMobGrinderBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum GrinderComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof RotationalMobGrinderBlockEntity grinderBE) {
            ItemStack weapon = grinderBE.getInternalWeapon();
            ItemEnchantments enchants = weapon.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

            if (enchants.isEmpty()) {
                tooltip.add(Component.translatable("jade.create_ks.grinder.no_enchants"));
                return;
            }

            if (accessor.getPlayer().isShiftKeyDown()) {
                enchants.keySet().forEach(ench -> {
                    tooltip.add(Component.translatable(ench.unwrapKey().get().location().toLanguageKey("enchantment")).append(" " + enchants.getLevel(ench)));
                });
            } else {
                tooltip.add(Component.translatable("jade.create_ks.grinder.shift_for_enchants"));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath("create_ks", "grinder_provider");
    }
}
