package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.storage.energy.HTEnergyBatteryWrapper
import hiiragi283.ragium.common.storage.holder.HTSimpleEnergyStorageHolder
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.state.BlockState

/**
 * 電力を消費する設備に使用される[HTMachineBlockEntity]の拡張クラス
 */
abstract class HTConsumerBlockEntity(protected val variant: HTMachineVariant, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(variant.blockEntityHolder, pos, state) {
    //    Ticking    //

    override val energyUsage: Int = variant.energyUsage

    protected var requiredEnergy: Int = 0
    protected var usedEnergy: Int = 0

    val progress: Float
        get() {
            val totalTick: Int = usedEnergy
            val maxTicks: Int = requiredEnergy
            if (maxTicks <= 0) return 0f
            val fixedTotalTicks: Int = totalTick % maxTicks
            return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
        }

    //    Energy Storage    //

    final override fun initializeEnergyStorage(listener: HTContentListener): HTEnergyStorageHolder =
        HTSimpleEnergyStorageHolder.input(this, HTEnergyBatteryWrapper { getter(level) })

    //    Slot    //

    val containerData: ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> usedEnergy
            1 -> requiredEnergy
            else -> -1
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> usedEnergy = value
                1 -> requiredEnergy = value
            }
        }

        override fun getCount(): Int = 2
    }
}
