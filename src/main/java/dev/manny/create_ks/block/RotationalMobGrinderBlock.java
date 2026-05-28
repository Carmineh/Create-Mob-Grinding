package dev.manny.create_ks.block;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.manny.create_ks.block.entity.RotationalMobGrinderBlockEntity;
import dev.manny.create_ks.registry.ModBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;

public class RotationalMobGrinderBlock extends DirectionalKineticBlock implements IBE<RotationalMobGrinderBlockEntity> {

    public RotationalMobGrinderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, net.minecraft.core.BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING).getOpposite();
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
    public net.minecraft.world.InteractionResult onWrenched(BlockState state, net.minecraft.world.item.context.UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            net.minecraft.world.level.block.entity.BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
            if (be instanceof RotationalMobGrinderBlockEntity grinderBE) {
                if (!context.getLevel().isClientSide) {
                    net.minecraft.world.item.ItemStack book = grinderBE.extractEnchantments();
                    if (!book.isEmpty()) {
                        net.minecraft.world.level.block.Block.popResource(context.getLevel(), context.getClickedPos(), book);
                    }
                }
                return net.minecraft.world.InteractionResult.SUCCESS;
            }
        }
        return super.onWrenched(state, context);
    }
}
