package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.HTEnergyStorageHolder
import hiiragi283.ragium.common.block.entity.generator.HTBlazingBoxBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTHeatGeneratorBlockEntity
import hiiragi283.ragium.common.util.getOrDefault
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage

object RagiumEnergyProviders {
    @JvmField
    val HEAT: BlockApiLookup<Boolean, Direction?> = create("heat")

    @JvmField
    val BLAZING_HEAT: BlockApiLookup<Boolean, Direction?> = create("blazing_heat")

    @JvmField
    val ENERGY: BlockApiLookup<EnergyStorage, Direction?> = EnergyStorage.SIDED

    @JvmStatic
    private fun create(name: String): BlockApiLookup<Boolean, Direction?> = BlockApiLookup.get(
        Ragium.id(name),
        Boolean::class.java,
        Direction::class.java,
    )

    @JvmStatic
    fun init() {
        initHeat()
        initBlazingHeat()
        initElectric()
    }

    @JvmStatic
    private fun initHeat() {
        HEAT.registerForBlocks(provideStatic(true), RagiumContents.CREATIVE_SOURCE)

        HEAT.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP
        }, Blocks.FIRE)

        HEAT.registerForBlocks({ _: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP && state.getOrDefault(Properties.LIT, false)
        }, Blocks.CAMPFIRE)

        HEAT.registerForBlockEntities({ blockEntity: BlockEntity, direction: Direction? ->
            direction == Direction.UP && (blockEntity as? HTHeatGeneratorBlockEntity)?.isBurning ?: false
        }, RagiumBlockEntityTypes.BURNING_BOX, RagiumBlockEntityTypes.BLAZING_BOX)
    }

    @JvmStatic
    private fun initBlazingHeat() {
        BLAZING_HEAT.registerForBlocks(provideStatic(true), RagiumContents.CREATIVE_SOURCE)

        BLAZING_HEAT.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP
        }, Blocks.SOUL_FIRE)

        BLAZING_HEAT.registerForBlocks({ _: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP && state.getOrDefault(Properties.LIT, false)
        }, Blocks.SOUL_CAMPFIRE)

        BLAZING_HEAT.registerForBlockEntity({ blockEntity: HTBlazingBoxBlockEntity, direction: Direction? ->
            direction == Direction.UP && blockEntity.isBurning && blockEntity.isBlazing
        }, RagiumBlockEntityTypes.BLAZING_BOX)
    }

    @JvmStatic
    private fun initElectric() {
        ENERGY.registerForBlocks(
            provideStatic(InfiniteEnergyStorage.INSTANCE),
            RagiumContents.CREATIVE_SOURCE,
        )

        ENERGY.registerFallback { _: World, _: BlockPos, _: BlockState, blockEntity: BlockEntity?, direction: Direction? ->
            (blockEntity as? HTEnergyStorageHolder)?.getEnergyStorage(direction)
        }

        ENERGY.registerForBlocks({ world: World, pos: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            if (direction != null) {
                val axis: Direction.Axis = state.get(Properties.AXIS)
                if (direction.axis == axis) {
                    val posTo: BlockPos = pos.offset(direction.opposite)
                    val stateTo: BlockState = world.getBlockState(posTo)
                    val blockEntityTo: BlockEntity? = world.getBlockEntity(posTo)
                    ENERGY.find(world, posTo, stateTo, blockEntityTo, direction)
                }
            }
            null
        }, RagiumContents.CABLE)

        ENERGY.registerForBlocks({ world: World, pos: BlockPos, state: BlockState, _: BlockEntity?, _: Direction? ->
            val facing: Direction = state.get(Properties.FACING)
            val posTo: BlockPos = pos.offset(facing)
            val stateTo: BlockState = world.getBlockState(posTo)
            val blockEntityTo: BlockEntity? = world.getBlockEntity(posTo)
            ENERGY.find(world, posTo, stateTo, blockEntityTo, facing.opposite)
        }, RagiumContents.GEAR_BOX)
    }

    @JvmStatic
    private fun <A, C> provideStatic(value: A): BlockApiLookup.BlockApiProvider<A, C> =
        BlockApiLookup.BlockApiProvider { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: C? -> value }
}
