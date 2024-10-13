package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.world.energyNetwork
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage

object RagiumApiLookupInit {
    /*@JvmField
    val HEAT: BlockApiLookup<Boolean, Direction?> = create("heat")

    @JvmField
    val BLAZING_HEAT: BlockApiLookup<Boolean, Direction?> = create("blazing_heat")*/

    @JvmField
    val ENERGY: BlockApiLookup<EnergyStorage, Direction?> = EnergyStorage.SIDED

    /*@JvmStatic
    private fun create(name: String): BlockApiLookup<Boolean, Direction?> = BlockApiLookup.get(
        RagiumAPI.id(name),
        Boolean::class.java,
        Direction::class.java,
    )*/

    @JvmStatic
    fun init() {
        initItemStorage()

        // initHeat()
        // initBlazingHeat()
        initElectric()
    }

    @JvmStatic
    fun initItemStorage() {
        ItemStorage.SIDED.registerForBlockEntity({ blockEntity: HTMetaMachineBlockEntity, direction: Direction? ->
            blockEntity.machineEntity?.let { InventoryStorage.of(it, direction) }
        }, RagiumBlockEntityTypes.META_MACHINE)
    }

    /*@JvmStatic
    private fun initHeat() {
        HEAT.registerForBlocks(provideStatic(true), RagiumContents.CREATIVE_SOURCE)

        HEAT.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP
        }, Blocks.FIRE)

        HEAT.registerForBlocks({ _: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP && state.getOrDefault(Properties.LIT, false)
        }, Blocks.CAMPFIRE)
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
    }*/

    @JvmStatic
    private fun initElectric() {
        ENERGY.registerForBlocks(
            provideStatic(InfiniteEnergyStorage.INSTANCE),
            RagiumBlocks.CREATIVE_SOURCE,
        )

        ENERGY.registerForBlocks({ world: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            world.energyNetwork
        }, RagiumBlocks.NETWORK_INTERFACE)

        /*ENERGY.registerForBlocks({ world: World, pos: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
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
        }, RagiumContents.GEAR_BOX)*/
    }

    @JvmStatic
    private fun <A, C> provideStatic(value: A): BlockApiLookup.BlockApiProvider<A, C> =
        BlockApiLookup.BlockApiProvider { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: C? -> value }
}
