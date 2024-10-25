package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.energy.HTEnergyType
import hiiragi283.ragium.api.extension.energyNetwork
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.world.HTEnergyNetwork
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.enums.EnumEntries

object RagiumMachineConditions {
    @JvmField
    val ELECTRIC_CONDITION: (World, BlockPos, HTMachineType, HTMachineTier) -> Boolean =
        { world: World, _: BlockPos, _: HTMachineType, tier: HTMachineTier ->
            world.energyNetwork
                ?.amount
                ?.let { it >= tier.recipeCost }
                ?: false
        }

    @JvmField
    val ELECTRIC_SUCCEEDED: (World, BlockPos, HTMachineType, HTMachineTier) -> Unit =
        { world: World, _: BlockPos, _: HTMachineType, tier: HTMachineTier ->
            world.energyNetwork?.let { network: HTEnergyNetwork ->
                useTransaction { transaction: Transaction ->
                    val extracted: Long = network.extract(HTEnergyType.ELECTRICITY, tier.recipeCost, transaction)
                    when {
                        extracted > 0 -> transaction.commit()
                        else -> transaction.abort()
                    }
                }
            }
        }

    @JvmField
    val ROCK_SUCCEEDED: (World, BlockPos, HTMachineType, HTMachineTier) -> Boolean =
        { world: World, pos: BlockPos, _: HTMachineType, _: HTMachineTier ->
            val directions: EnumEntries<Direction> = Direction.entries
            if (directions.any { world.getBlockState(pos.offset(it)).isOf(Blocks.WATER) }) {
                directions.any { world.getBlockState(pos.offset(it)).isOf(Blocks.LAVA) }
            } else {
                false
            }
        }
}
