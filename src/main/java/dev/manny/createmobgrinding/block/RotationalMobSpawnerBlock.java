package dev.manny.createmobgrinding.block;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.manny.createmobgrinding.block.entity.RotationalMobSpawnerBlockEntity;
import dev.manny.createmobgrinding.registry.ModBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;

public class RotationalMobSpawnerBlock extends DirectionalKineticBlock implements IBE<RotationalMobSpawnerBlockEntity> {

    public RotationalMobSpawnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, net.minecraft.core.BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public Class<RotationalMobSpawnerBlockEntity> getBlockEntityClass() {
        return RotationalMobSpawnerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RotationalMobSpawnerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.ROTATIONAL_MOB_SPAWNER.get();
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(net.minecraft.world.item.ItemStack stack, BlockState state, net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hitResult) {
        if (stack.is(dev.manny.createmobgrinding.registry.ModItems.MOB_SPAWNER_CHUNK.get())) {
            if (!level.isClientSide) {
                net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof RotationalMobSpawnerBlockEntity spawnerBE) {
                    net.minecraft.world.item.ItemStack current = spawnerBE.inventory.getStackInSlot(0);
                    if (current.isEmpty()) {
                        spawnerBE.inventory.setStackInSlot(0, stack.copyWithCount(1));
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return net.minecraft.world.ItemInteractionResult.SUCCESS;
                    }
                }
            }
            return net.minecraft.world.ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public net.minecraft.world.InteractionResult onWrenched(BlockState state, net.minecraft.world.item.context.UseOnContext context) {
        net.minecraft.world.level.block.entity.BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
        if (be instanceof RotationalMobSpawnerBlockEntity spawnerBE) {
            if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
                net.minecraft.world.item.ItemStack chunk = spawnerBE.inventory.getStackInSlot(0);
                if (!chunk.isEmpty()) {
                    if (!context.getLevel().isClientSide) {
                        net.minecraft.world.level.block.Block.popResource(context.getLevel(), context.getClickedPos(), chunk);
                        spawnerBE.inventory.setStackInSlot(0, net.minecraft.world.item.ItemStack.EMPTY);
                    }
                    return net.minecraft.world.InteractionResult.SUCCESS;
                } else {
                    // Se è vuoto, delega al super (che rompe il blocco e lo droppa per Create)
                    return super.onWrenched(state, context);
                }
            } else {
                // Non in shift: ruota la faccia di spawn
                if (!context.getLevel().isClientSide) {
                    spawnerBE.cycleSpawnFace();
                    if (context.getPlayer() != null) {
                        context.getPlayer().displayClientMessage(net.minecraft.network.chat.Component.literal("Spawn Face: " + spawnerBE.getSpawnFace().getName()), true);
                    }
                }
                return net.minecraft.world.InteractionResult.SUCCESS;
            }
        }
        return super.onWrenched(state, context);
    }
}

