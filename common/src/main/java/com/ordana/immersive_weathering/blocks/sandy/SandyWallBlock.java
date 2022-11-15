package com.ordana.immersive_weathering.blocks.sandy;

import com.ordana.immersive_weathering.util.WeatheringHelper;
import com.ordana.immersive_weathering.blocks.ModBlockProperties;
import com.ordana.immersive_weathering.configs.CommonConfigs;
import com.ordana.immersive_weathering.reg.ModBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class SandyWallBlock extends WallBlock implements Sandy {

    public SandyWallBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ModBlockProperties.SANDINESS, 0).setValue(ModBlockProperties.SAND_AGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getShape(state.setValue(ModBlockProperties.SANDINESS, 0).setValue(ModBlockProperties.SAND_AGE, 0), level, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getCollisionShape(state.setValue(ModBlockProperties.SANDINESS, 0).setValue(ModBlockProperties.SAND_AGE, 0), level, pos, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(ModBlockProperties.SANDINESS, SAND_AGE);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        this.spawnParticles(state, level, pos, random);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Sandy.super.randomTick(state, level, pos, random);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(interactWithPlayer(state, level, pos, player, hand, hit)){
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        Sandy.super.neighborChanged(state, level, pos, block, neighborPos, isMoving);
    }
}