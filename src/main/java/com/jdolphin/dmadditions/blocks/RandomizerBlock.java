package com.jdolphin.dmadditions.blocks;

import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.Location;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tardis.TardisFlightData;
import com.swdteam.common.tardis.data.TardisFlightPool;
import com.swdteam.util.ChatUtil;
import com.swdteam.util.ChatUtil.MessageType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class RandomizerBlock extends HorizontalBlock {
	private String dimensionKey;

	protected static final VoxelShape SHAPE_N = VoxelShapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 8.0D), Block.box(0.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D));
	protected static final VoxelShape SHAPE_E = VoxelShapes.or(Block.box(8.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 16.0D));
	protected static final VoxelShape SHAPE_S = VoxelShapes.or(Block.box(0.0D, 0.0D, 8.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D));
	protected static final VoxelShape SHAPE_W = VoxelShapes.or(Block.box(0.0D, 0.0D, 0.0D, 8.0D, 2.0D, 16.0D), Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D));

	public RandomizerBlock(AbstractBlock.Properties properties) {
		super(properties);

		this.registerDefaultState(this.stateDefinition.any()
			.setValue(FACING, Direction.NORTH)
		);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext selectionContext) {
		Direction facing = state.getValue(FACING);
		switch (facing) {
			case NORTH:
			default:
				return SHAPE_N;
			case EAST:
				return SHAPE_E;
			case SOUTH:
				return SHAPE_S;
			case WEST:
				return SHAPE_W;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	public RegistryKey<World> dimensionWorldKey() {
		return RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(this.dimensionKey));
	}

	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
	                            BlockRayTraceResult rayTraceResult) {
		if (worldIn.isClientSide)
			return ActionResultType.PASS;

		if (handIn == Hand.MAIN_HAND) {
			if (ServerLifecycleHooks.getCurrentServer() == null)
				return ActionResultType.CONSUME;

			ServerWorld level = (ServerWorld) worldIn;
			WorldBorder border = level.getWorldBorder();
			TardisData tardis = DMTardis.getTardisFromInteriorPos(pos);

			Location currentLocation = tardis.getCurrentLocation();
			BlockPos currentPos = currentLocation.getBlockPosition();

			double maxDistance = 10_000; // TODO: put this in server config file or something idk

			double maxX = Math.min(border.getMaxX(), currentPos.getX() + maxDistance);
			double minX = Math.max(border.getMinX(), currentPos.getX() - maxDistance);

			double maxZ = Math.min(border.getMaxZ(), currentPos.getZ() + maxDistance);
			double minZ = Math.max(border.getMinZ(), currentPos.getZ() - maxDistance);

			double xPos = Math.floor(Math.random() * (maxX - minX + 1) + minX);
			double zPos = Math.floor(Math.random() * (maxZ - minZ + 1) + minZ);
			double yPos = currentPos.getY();

			System.out.printf("Randomizer going to %s %s %s%n", xPos, yPos, zPos);

			BlockPos newPos = new BlockPos(xPos, yPos, zPos);

			TardisFlightData flight = TardisFlightPool.getFlightData(tardis);

			flight.setPos(newPos);
			flight.recalculateLanding(true);
			TardisFlightPool.updateFlight(tardis, new Location(newPos, flight.dimensionWorldKey()));

			ChatUtil.sendCompletedMsg(player,
				new TranslationTextComponent("notice.dalekmod.tardis.randomizer_set", newPos.getX(), newPos.getZ()),
				MessageType.STATUS_BAR);
		}
		return ActionResultType.SUCCESS;
	}
}
