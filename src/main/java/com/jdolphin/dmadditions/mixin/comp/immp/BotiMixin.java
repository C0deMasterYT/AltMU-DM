package com.jdolphin.dmadditions.mixin.comp.immp;

import com.swdteam.client.tardis.data.ExteriorModels;
import com.swdteam.common.init.DMBlockEntities;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tardis.TardisState;
import com.swdteam.common.tileentity.ExtraRotationTileEntityBase;
import com.swdteam.common.tileentity.TardisTileEntity;
import com.swdteam.model.javajson.JSONModel;
import com.swdteam.model.javajson.ModelRendererWrapper;
import com.swdteam.model.javajson.ModelWrapper;
import com.swdteam.util.SWDMathUtils;
import com.swdteam.util.math.Position;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(TardisTileEntity.class)
public abstract class BotiMixin extends ExtraRotationTileEntityBase implements ITickableTileEntity {

	@Shadow(remap = false)
	public TardisData tardisData;

	@Shadow(remap = false)
	boolean demat;

	@Unique
	boolean dmadditions_116$isPortalSpawned = false;

	@Shadow(remap = false)
	protected abstract void doorAnimation();

	@Unique
	public com.qouteall.immersive_portals.portal.Portal dmadditions_116$portal = null;

	@Unique
	private static AxisAlignedBB dmadditions_116$defaultAABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);

	@Unique
	public RegistryKey<World> dmadditions_116$TARDIS = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("dalekmod:tardis"));

	public BotiMixin() {
		super(DMBlockEntities.TILE_TARDIS.get());
	}

	@Unique private String PORTAL = "Portal";


	/**
	 * @author Originally made by BobDude, finished by JamesLeDolphin
	 * @reason BOTI
	 */
	@Overwrite
	public void tick() {
		TardisTileEntity tile = (TardisTileEntity) ((Object) this);
		this.doorAnimation();
		long tickTime = System.currentTimeMillis() - tile.lastTickTime;
		tile.lastTickTime = System.currentTimeMillis();
		if (tile.state == TardisState.DEMAT) {
			this.demat = true;
			if (tile.animStartTime == 0L) {
				tile.animStartTime = System.currentTimeMillis();
			}
			if (tickTime > 100L) {
				tile.animStartTime += tickTime;
			}
			tile.dematTime = (float) ((double) (System.currentTimeMillis() - tile.animStartTime) / 10000.0);
			if (tile.dematTime >= 1.0F) {
				tile.dematTime = 1.0F;
			}
			if (tile.dematTime == 1.0F) {
				this.getLevel().setBlockAndUpdate(tile.getBlockPos(), Blocks.AIR.defaultBlockState());
				tile.animStartTime = 0L;
			}
		} else if (tile.state == TardisState.REMAT) {
			this.demat = false;
			if (tile.animStartTime == 0L) {
				tile.animStartTime = System.currentTimeMillis();
			}
			if (tickTime > 100L) {
				tile.animStartTime += tickTime;
			}
			if (System.currentTimeMillis() - tile.animStartTime > 9000L) {
				tile.dematTime = 1.0F - (float) ((double) (System.currentTimeMillis() - (tile.animStartTime + 9000L)) / 10000.0);
			}
			if (tile.dematTime <= 0.0F) {
				tile.dematTime = 0.0F;
			}
			if (tile.dematTime == 0.0F) {
				tile.setState(TardisState.NEUTRAL);
				tile.animStartTime = 0L;
			}
		}

		tile.pulses = 1.0F - tile.dematTime + MathHelper.cos(tile.dematTime * 3.141592F * 10.0F) * 0.25F * MathHelper.sin(tile.dematTime * 3.141592F);
		if (this.getLevel().getBlockState(tile.getBlockPos().offset(0, -1, 0)).getMaterial() == Material.AIR) {
			++tile.bobTime;
			++this.rotation;
			if (dmadditions_116$portal != null && dmadditions_116$portal.isAlive() && !level.isClientSide()) {
				dmadditions_116$portal.reloadAndSyncToClient();
				dmadditions_116$portal.kill();
				dmadditions_116$portal.remove(false);
				level.getChunk(this.worldPosition.getX(), this.worldPosition.getZ()).removeEntity(dmadditions_116$portal);
				dmadditions_116$portal.onRemovedFromWorld();
				dmadditions_116$portal = null;
				dmadditions_116$isPortalSpawned = false;
			}
		} else {
			tile.bobTime = 0;
			this.rotation = SWDMathUtils.SnapRotationToCardinal(this.rotation);
		}


		if (!this.level.isClientSide) {
			tile.tardisData = DMTardis.getTardis(tile.globalID);
			if (tile.tardisData != null) {

				if (tile.tardisData.getInteriorSpawnPosition() != null) {
					Position vec = tile.tardisData.getInteriorSpawnPosition();
					Vector3d pos = new Vector3d(vec.x(), vec.y() + 1.05, vec.z());
					dmadditions_116$defaultAABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);
					//size of the portal
					//handles how far out the portal is from the tardis

					ResourceLocation rl = tile.tardisData.getTardisExterior().getData().getModel(tile.tardisData.getSkinID());
					JSONModel model = ExteriorModels.getModel(rl);
					ModelWrapper modelWrapper = model.getModelData().getModel();
					ModelRendererWrapper mdl = modelWrapper.getPart("portal");

					AxisAlignedBB bounds = dmadditions_116$defaultAABB.move(this.getBlockPos()).inflate(mdl == null ? -0.14200001192092896 : mdl.x / 200,
						mdl == null ? 0.0 : mdl.y / 200, mdl == null ? -0.14200001192092896 : mdl.z / 200); //These aren't accurate but it somewhat works

					bounds = bounds.move(Math.sin(Math.toRadians(this.rotation)) * 0.05,
						0.02, -Math.cos(Math.toRadians(this.rotation)) * 0.05);

					Direction tDir = Direction.byName(SWDMathUtils.rotationToCardinal(tile.rotation));
					if (((tile.state == TardisState.DEMAT || tile.state.equals(TardisState.REMAT)) || (tile.bobTime != 0) || (!tile.doorOpenRight))
						&& (dmadditions_116$portal != null && dmadditions_116$portal.isAlive() && dmadditions_116$isPortalSpawned)) {
						dmadditions_116$portal.reloadAndSyncToClient();
						dmadditions_116$portal.kill();
						dmadditions_116$portal.remove(false);
						level.getChunk(this.worldPosition.getX(), this.worldPosition.getZ()).removeEntity(dmadditions_116$portal);
						dmadditions_116$portal.onRemovedFromWorld();
						dmadditions_116$portal = null;
						dmadditions_116$isPortalSpawned = false;
					}

					/*
					 * List of exterior registry names
					 * dalekmod:tardis_capsule
					 * dalekmod:police_box
					 * dalekmod:fridge
					 * dalekmod:block_stack
					 * dalekmod:phone_booth
					 * dalekmod:pagoda
					 * dalekmod:dalek_mod_2013
					 * dalekmod:sidrat_capsule
					 */

					if (tile != null && level != null) {
						if ((tile.doorOpenLeft || tile.doorOpenRight) && !dmadditions_116$isPortalSpawned && tDir != null) {
							dmadditions_116$portal = com.qouteall.immersive_portals.portal.PortalManipulation.createOrthodoxPortal(
								com.qouteall.immersive_portals.portal.Portal.entityType,
								com.qouteall.immersive_portals.McHelper.getServerWorld(tile.tardisData.getCurrentLocation().dimensionWorldKey()),
								com.qouteall.immersive_portals.McHelper.getServerWorld(dmadditions_116$TARDIS),
								tDir,
								bounds,
								pos
							);
							if (tDir == Direction.NORTH) {
								dmadditions_116$portal.setRotationTransformation(new Quaternion(0, 1, 0, 0));
							} else if (tDir == Direction.WEST) {
								dmadditions_116$portal.setRotationTransformation(new Quaternion(0, 0.7071f, 0, 0.7071f));
							} else if (tDir == Direction.EAST) {
								dmadditions_116$portal.setRotationTransformation(new Quaternion(0, -0.7071f, 0, 0.7071f));
							}

							com.qouteall.immersive_portals.McHelper.spawnServerEntity(dmadditions_116$portal);
							dmadditions_116$isPortalSpawned = true;
						}

						if (dmadditions_116$portal != null && dmadditions_116$portal.isAlive()) {
							dmadditions_116$portal.renderingMergable = true;
							Position position = tardisData.getInteriorSpawnPosition();
							Vector3d vec3d = new Vector3d(position.x(), position.y(), position.z());
							if (!Objects.equals(dmadditions_116$portal.destination, vec3d)) {
								dmadditions_116$portal.setDestination(vec3d);
							}
							if (tile.doorOpenLeft || tile.doorOpenRight) {
								dmadditions_116$portal.setDestination(pos);
							}
						}
					}
				}
			}
		}
	}
}
