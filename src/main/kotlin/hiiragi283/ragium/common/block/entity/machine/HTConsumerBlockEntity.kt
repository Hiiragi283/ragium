package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.attribute.HTEnergyBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttributeOrThrow
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.storage.energy.HTBasicEnergyStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.util.Mth
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * 電力を消費する設備に使用される[HTMachineBlockEntity]の拡張クラス
 */
abstract class HTConsumerBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(blockHolder, pos, state) {
    //    Ticking    //

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

    protected val energyStorage: HTBasicEnergyStorage = HTBasicEnergyStorage.input(
        ::setOnlySave,
        blockHolder.getAttributeOrThrow<HTEnergyBlockAttribute>().getCapacity(),
    )

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        energyStorage.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        energyStorage.deserialize(input)
    }

    final override fun getEnergyStorage(direction: Direction?): HTBasicEnergyStorage = energyStorage

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
