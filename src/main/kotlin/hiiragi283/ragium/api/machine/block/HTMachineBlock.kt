package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.machineKeyOrNull
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.HTBlockWithEntity
import hiiragi283.ragium.common.init.RagiumBlockProperties
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.particle.ParticleUtil
import net.minecraft.particle.SimpleParticleType
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class HTMachineBlock(val key: HTMachineKey) :
    HTBlockWithEntity(blockSettings(Blocks.SMOOTH_STONE).nonOpaque()),
    InventoryProvider {
    init {
        defaultState = stateManager
            .defaultState
            .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
            .with(RagiumBlockProperties.ACTIVE, false)
            .with(HTMachineTier.PROPERTY, HTMachineTier.PRIMITIVE)
    }

    fun getTierState(tier: HTMachineTier): BlockState = defaultState.with(HTMachineTier.PROPERTY, tier)

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext,
    ): VoxelShape = key.entry[HTMachinePropertyKeys.VOXEL_SHAPE] ?: super.getOutlineShape(
        state,
        world,
        pos,
        context,
    )

    override fun getName(): MutableText = key.text

    override fun randomDisplayTick(
        state: BlockState,
        world: World,
        pos: BlockPos,
        random: Random,
    ) {
        if (state.get(RagiumBlockProperties.ACTIVE)) {
            key.entry.ifPresent(HTMachinePropertyKeys.PARTICLE) { particleType: SimpleParticleType ->
                ParticleUtil.spawnParticlesAround(world, pos, 20, particleType)
            }
        }
    }

    override fun appendTooltip(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipType,
    ) {
        stack.machineKeyOrNull?.appendTooltip(tooltip::add, stack.machineTier)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = defaultState
        .with(Properties.HORIZONTAL_FACING, ctx.horizontalPlayerFacing.opposite)
        .with(HTMachineTier.PROPERTY, ctx.stack.machineTier)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.HORIZONTAL_FACING, RagiumBlockProperties.ACTIVE, HTMachineTier.PROPERTY)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)))

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        key.entry[HTMachinePropertyKeys.MACHINE_FACTORY]?.create(pos, state, key)

    //    InventoryProvider    //

    override fun getInventory(state: BlockState, world: WorldAccess, pos: BlockPos): SidedInventory? =
        world.getMachineEntity(pos)?.asInventory()
}
