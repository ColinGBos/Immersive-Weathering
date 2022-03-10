package com.ordana.immersive_weathering.registry.blocks.mossable;

import com.ordana.immersive_weathering.registry.ModTags;
import com.ordana.immersive_weathering.registry.blocks.WeatherableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class MossableWallBlock extends WallBlock implements Mossable {

    public MossableWallBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(WEATHERABLE, false));
    }


    @Override
    public boolean hasRandomTicks(BlockState state) {
        //this is how we make only some of them random tick
        return isWeatherable(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld serverLevel, BlockPos pos, Random random) {
        float weatherChance = 0.1f;
        if (random.nextFloat() < weatherChance) {
            var opt = this.getDegradationResult(state);
            opt.ifPresent(b -> serverLevel.setBlockState(pos, b, 3));
        }
    }

    @Override
    public float getInterestForDirection() {
        return 0.5f;
    }

    @Override
    public float getHighInterestChance() {
        return 0.5f;
    }

    @Override
    public boolean isWeatherable(BlockState state) {
        return state.get(WEATHERABLE);
    }

    @Override
    public WeatherableBlock.WeatheringAgent getWeatheringEffect(BlockState state, World world, BlockPos pos) {
        if (world.getBlockState(pos).isIn(ModTags.CRACK_SOURCE)) {
            return WeatherableBlock.WeatheringAgent.WEATHER;
        }
        return WeatherableBlock.WeatheringAgent.NONE;
    }

    @Override
    public float getDegradationChanceMultiplier() {
        return 0;
    }

    @Override
    public Mossable.MossLevel getDegradationLevel() {
        return Mossable.MossLevel.UNAFFECTED;
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        super.appendProperties(stateManager);
        stateManager.add(WEATHERABLE);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        if (world instanceof ServerWorld serverWorld) {
            boolean weathering = this.shouldStartWeathering(state, pos, serverWorld);
            if (state.get(WEATHERABLE) != weathering) {
                //update weathering state
                serverWorld.setBlockState(pos, state.with(WEATHERABLE, weathering), 3);
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state != null) {
            boolean weathering = this.shouldStartWeathering(state, ctx.getBlockPos(), ctx.getWorld());
            state.with(WEATHERABLE, weathering);
        }
        return state;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return super.getOutlineShape(state.with(WEATHERABLE, true), view, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return super.getCollisionShape(state.with(WEATHERABLE, true), view, pos, context);
    }
}