package dev.manny.create_ks.item;

import dev.manny.create_ks.registry.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MobSpawnerChunkItem extends Item {

    public MobSpawnerChunkItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ResourceLocation entityId = stack.get(ModDataComponents.SPAWNER_ENTITY.get());
        if (entityId != null) {
            tooltipComponents.add(Component.translatable("tooltip.create_ks.mob_chunk.entity_type", Component.translatable(net.minecraft.Util.makeDescriptionId("entity", entityId))).withStyle(net.minecraft.ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
