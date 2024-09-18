package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.entity.generator.HTBlazingBoxBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTHeatGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTKineticGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSingleMachineBlockEntity
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.util.getOrDefault
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object RagiumEnergyProviders {
    @JvmField
    val HEAT: BlockApiLookup<Boolean, Direction?> = create("heat")

    @JvmField
    val BLAZING_HEAT: BlockApiLookup<Boolean, Direction?> = create("blazing_heat")

    @JvmField
    val KINETIC: BlockApiLookup<Boolean, Direction?> = create("kinetic")

    @JvmField
    val ELECTRIC: BlockApiLookup<Boolean, Direction?> = create("electric")

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
        initKinetic()
        initElectric()
    }

    @JvmStatic
    private fun initHeat() {
        HEAT.registerForBlocks(ALWAYS_TRUE, RagiumBlocks.CREATIVE_SOURCE)

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
        BLAZING_HEAT.registerForBlocks(ALWAYS_TRUE, RagiumBlocks.CREATIVE_SOURCE)

        HEAT.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP
        }, Blocks.SOUL_FIRE)

        HEAT.registerForBlocks({ _: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP && state.getOrDefault(Properties.LIT, false)
        }, Blocks.SOUL_CAMPFIRE)

        HEAT.registerForBlockEntity({ blockEntity: HTBlazingBoxBlockEntity, direction: Direction? ->
            direction == Direction.UP && blockEntity.isBurning && blockEntity.isBlazing
        }, RagiumBlockEntityTypes.BLAZING_BOX)
    }

    @JvmStatic
    private fun initKinetic() {
        KINETIC.registerForBlocks(ALWAYS_TRUE, RagiumBlocks.CREATIVE_SOURCE)

        HTMachineType.Single.entries
            .filter { it.tier == HTMachineTier.KINETIC }
            .forEach { type: HTMachineType.Single ->
                KINETIC.registerForBlockEntity({ blockEntity: HTSingleMachineBlockEntity, direction: Direction? ->
                    val world: World = blockEntity.world ?: return@registerForBlockEntity false
                    val pos: BlockPos = blockEntity.pos
                    val toPos: BlockPos =
                        HTSingleMachineBlockEntity.findProcessor(world, pos) ?: return@registerForBlockEntity false
                    val toState: BlockState = world.getBlockState(pos)
                    (world.getBlockEntity(pos) as? HTKineticGeneratorBlockEntity)
                        ?.canProvidePower(world, toPos, toState) ?: false
                }, type.blockEntityType)
            }
    }

    @JvmStatic
    private fun initElectric() {
        ELECTRIC.registerForBlocks(ALWAYS_TRUE, RagiumBlocks.CREATIVE_SOURCE)
    }

    private val ALWAYS_TRUE: BlockApiLookup.BlockApiProvider<Boolean, Direction?> =
        BlockApiLookup.BlockApiProvider { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> true }
}
