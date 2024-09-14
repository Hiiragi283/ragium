package hiiragi283.ragium.common.fluid

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

sealed class HTFlowableFluid(settings: Settings) : FlowableFluid() {

    private val still: () -> Fluid = settings::still
    private val flowing: () -> Fluid = settings::flowing
    private val block: () -> Block = settings::block
    private val bucketItem: () -> Item = settings::bucketItem

    private val tickRate: Int = settings.tickRate
    private val blastResistance: Float = settings.blastResistance
    private val isInfinite: Boolean = settings.isInfinite
    private val maxFlowDistance: Int = settings.maxFlowDistance
    private val decreasePerBlock: Int = settings.decreasePerBlock

    //    Settings    //

    class Settings {

        lateinit var still: Fluid
        lateinit var flowing: Fluid
        lateinit var block: Block
        lateinit var bucketItem: Item

        val hasBlock: Boolean
            get() = ::block.isInitialized
        val hasBucket: Boolean
            get() = ::bucketItem.isInitialized

        var tickRate: Int = 5
        var blastResistance: Float = 100.0f
        var isInfinite: Boolean = false
        var maxFlowDistance: Int = 4
        var decreasePerBlock: Int = 1

    }

    //    FlowableFluid    //

    override fun getBucketItem(): Item = bucketItem()

    override fun canBeReplacedWith(
        state: FluidState,
        world: BlockView,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction,
    ): Boolean = false

    override fun getTickRate(world: WorldView): Int = tickRate

    override fun getBlastResistance(): Float = blastResistance

    override fun toBlockState(state: FluidState): BlockState =
        block().defaultState.with(FluidBlock.LEVEL, getBlockStateLevel(state))

    override fun getFlowing(): Fluid = flowing()

    override fun getStill(): Fluid = still()

    override fun isInfinite(world: World): Boolean = isInfinite

    override fun beforeBreakingBlock(world: WorldAccess, pos: BlockPos, state: BlockState) {
        Block.dropStacks(state, world, pos, world.getBlockEntity(pos))
    }

    override fun getMaxFlowDistance(world: WorldView): Int = maxFlowDistance

    override fun getLevelDecreasePerBlock(world: WorldView): Int = decreasePerBlock

    override fun matchesType(fluid: Fluid): Boolean = fluid == getStill() || fluid == getFlowing()

    //    Flowing    //

    class Flowing(settings: Settings) : HTFlowableFluid(settings) {
        override fun appendProperties(builder: StateManager.Builder<Fluid, FluidState>) {
            super.appendProperties(builder)
            builder.add(LEVEL)
        }

        override fun getLevel(state: FluidState): Int = state.get(LEVEL)

        override fun isStill(state: FluidState): Boolean = false
    }

    //    Still    //

    class Still(settings: Settings) : HTFlowableFluid(settings) {
        override fun getLevel(state: FluidState): Int = 8

        override fun isStill(state: FluidState): Boolean = true
    }

}