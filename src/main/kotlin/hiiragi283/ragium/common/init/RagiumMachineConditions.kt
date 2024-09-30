package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.machine.HTMachineCondition
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.util.useTransaction
import hiiragi283.ragium.common.world.HTEnergyNetwork
import hiiragi283.ragium.common.world.energyNetwork
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.enums.EnumEntries

object RagiumMachineConditions {
    @JvmField
    val NONE: HTMachineCondition = HTMachineCondition(
        HTMachineCondition.Condition.FALSE,
        HTMachineCondition.Succeeded.EMPTY,
    )

    //    Processor    //

    @JvmField
    val ELECTRIC_CONSUMER: HTMachineCondition = HTMachineCondition(
        { world: World, _: BlockPos, _: HTMachineType, tier: HTMachineTier ->
            world.energyNetwork
                ?.amount
                ?.let { it >= tier.recipeCost }
                ?: false
        },
        { world: World, _: BlockPos, _: HTMachineType, tier: HTMachineTier ->
            world.energyNetwork?.let { network: HTEnergyNetwork ->
                useTransaction { transaction: Transaction ->
                    val extracted: Long = network.extract(tier.recipeCost, transaction)
                    when {
                        extracted > 0 -> transaction.commit()
                        else -> transaction.abort()
                    }
                }
            }
        },
    )

    @JvmField
    val ROCK_GENERATOR = HTMachineCondition(
        { world: World, pos: BlockPos, _: HTMachineType, _: HTMachineTier ->
            val directions: EnumEntries<Direction> = Direction.entries
            if (directions.any { world.getBlockState(pos.offset(it)).isOf(Blocks.WATER) }) {
                directions.any { world.getBlockState(pos.offset(it)).isOf(Blocks.LAVA) }
            } else {
                false
            }
        },
        HTMachineCondition.Succeeded.EMPTY,
    )
}
