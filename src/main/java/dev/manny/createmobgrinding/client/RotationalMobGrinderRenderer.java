package dev.manny.createmobgrinding.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.manny.createmobgrinding.CreateMobGrinding;
import dev.manny.createmobgrinding.block.entity.RotationalMobGrinderBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class RotationalMobGrinderRenderer extends KineticBlockEntityRenderer<RotationalMobGrinderBlockEntity> {

    public RotationalMobGrinderRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(RotationalMobGrinderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        // Render base kinetic model (the default shaft)
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        // Fetch our custom blade model registered in ModClientEvents
        BakedModel bladeModel = Minecraft.getInstance().getModelManager().getModel(
            new net.minecraft.client.resources.model.ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(CreateMobGrinding.MOD_ID, "block/rotational_mob_grinder_blade"), "standalone")
        );

        ms.pushPose();

        float speed = Math.abs(be.getSpeed());
        float angle;
        if (speed > 0) {
            angle = (be.getLevel().getGameTime() + partialTicks) * (speed / 10f);
        } else {
            angle = 0;
        }

        // Rotate the blade around the center Y axis
        ms.translate(0.5D, 0.5D, 0.5D);
        ms.mulPose(com.mojang.math.Axis.YP.rotationDegrees(angle));
        ms.translate(-0.5D, -0.5D, -0.5D);

        // Render the BakedModel
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
            ms.last(), buffer.getBuffer(RenderType.cutoutMipped()), be.getBlockState(), bladeModel, 1.0F, 1.0F, 1.0F, light, overlay
        );

        ms.popPose();
    }
}

