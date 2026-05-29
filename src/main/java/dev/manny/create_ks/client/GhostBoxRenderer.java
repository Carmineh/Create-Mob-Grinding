package dev.manny.create_ks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.manny.create_ks.CreateKineticSpawning;
import dev.manny.create_ks.block.entity.RotationalMobGrinderBlockEntity;
import dev.manny.create_ks.block.entity.RotationalMobSpawnerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

@EventBusSubscriber(modid = CreateKineticSpawning.MOD_ID, value = Dist.CLIENT)
public class GhostBoxRenderer {

    @SubscribeEvent
    public static void onRenderHighlight(RenderHighlightEvent.Block event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean mainHandWrench = BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).getPath().contains("wrench");
        boolean offHandWrench = BuiltInRegistries.ITEM.getKey(player.getOffhandItem().getItem()).getPath().contains("wrench");

        if (!mainHandWrench && !offHandWrench) return;

        BlockHitResult hitResult = event.getTarget();
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            BlockEntity be = player.level().getBlockEntity(pos);

            AABB boxToDraw = null;
            if (be instanceof RotationalMobSpawnerBlockEntity spawnerBE) {
                net.minecraft.core.Direction face = spawnerBE.getSpawnFace();
                double cx = pos.getX() + 0.5 + face.getStepX() * 1.5;
                double cy = pos.getY() + 0.5 + face.getStepY() * 1.5;
                double cz = pos.getZ() + 0.5 + face.getStepZ() * 1.5;
                boxToDraw = new AABB(cx - 1, cy - 0.5, cz - 1, cx + 1, cy + 0.5, cz + 1);
            } else if (be instanceof RotationalMobGrinderBlockEntity) {
                boxToDraw = new AABB(pos).inflate(0.25);
            }

            if (boxToDraw != null) {
                PoseStack poseStack = event.getPoseStack();
                double camX = event.getCamera().getPosition().x;
                double camY = event.getCamera().getPosition().y;
                double camZ = event.getCamera().getPosition().z;

                AABB offsetBox = boxToDraw.move(-camX, -camY, -camZ);
                
                VertexConsumer vertexConsumer = event.getMultiBufferSource().getBuffer(RenderType.lines());
                // ARGB for NeoForge 1.21.1: alpha, red, green, blue (or the 4 float variant)
                // renderLineBox in 1.21 takes floats (AABB, red, green, blue, alpha)
                LevelRenderer.renderLineBox(poseStack, vertexConsumer, offsetBox, 1.0F, 0.2F, 0.2F, 1.0F);
            }
        }
    }
}
