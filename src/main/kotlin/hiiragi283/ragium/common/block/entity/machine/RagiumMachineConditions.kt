package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumEnergyProviders
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object RagiumMachineConditions {
    @JvmField
    val NONE: (World, BlockPos) -> Boolean = { _: World, _: BlockPos -> false }

    @JvmField
    val HEAT: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
        val downPos: BlockPos = pos.down()
        RagiumEnergyProviders.HEAT.find(
            world,
            downPos,
            world.getBlockState(downPos),
            world.getBlockEntity(downPos),
            Direction.UP,
        ) ?: false
    }

    @JvmField
    val BLAZING_HEAT: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
        val downPos: BlockPos = pos.down()
        RagiumEnergyProviders.BLAZING_HEAT.find(
            world,
            downPos,
            world.getBlockState(downPos),
            world.getBlockEntity(downPos),
            Direction.UP,
        ) ?: false
    }

    @JvmField
    val ELECTRIC: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
        RagiumEnergyProviders.ENERGY
            .find(world, pos, world.getBlockState(pos), world.getBlockEntity(pos), null)
            ?.amount
            ?.let { it >= Ragium.RECIPE_COST }
            ?: false
    }
}
