package dev.manny.createmobgrinding.block;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.manny.createmobgrinding.block.entity.RotationalMobGrinderBlockEntity;
import dev.manny.createmobgrinding.registry.ModBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;

public class RotationalMobGrinderBlock extends KineticBlock implements IBE<RotationalMobGrinderBlockEntity> {

    public RotationalMobGrinderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, net.minecraft.core.BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == Direction.Axis.Y; // Accepts from UP and DOWN
    }

    @Override
    public Class<RotationalMobGrinderBlockEntity> getBlockEntityClass() {
        return RotationalMobGrinderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RotationalMobGrinderBlockEntity> getBlockEntityType() {
        return ModBlockEntities.ROTATIONAL_MOB_GRINDER.get();
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(net.minecraft.world.item.ItemStack stack, BlockState state, net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hitResult) {
        if (stack.is(net.minecraft.world.item.Items.ENCHANTED_BOOK)) {
            if (!level.isClientSide) {
                net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof RotationalMobGrinderBlockEntity grinderBE) {
                    if (grinderBE.applyEnchantedBook(stack)) {
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
    public net.minecraft.world.InteractionResult onSneakWrenched(BlockState state, net.minecraft.world.item.context.UseOnContext context) {
        net.minecraft.world.level.Level level = context.getLevel();
        net.minecraft.core.BlockPos pos = context.getClickedPos();
        if (!level.isClientSide) {
            level.destroyBlock(pos, false);
            net.minecraft.world.level.block.Block.popResource(level, pos, new net.minecraft.world.item.ItemStack(dev.manny.createmobgrinding.registry.ModBlocks.ROTATIONAL_MOB_GRINDER.get().asItem()));
        }
        return net.minecraft.world.InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RotationalMobGrinderBlockEntity grinderBE) {
                net.minecraft.world.item.ItemStack book = grinderBE.extractEnchantments();
                if (!book.isEmpty()) {
                    net.minecraft.world.level.block.Block.popResource(level, pos, book);
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}

