package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.storage.energy.HTBasicEnergyStorage
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState

/**
 * 電力を生産する設備に使用される[HTMachineBlockEntity]の拡張クラス
 */
abstract class HTGeneratorBlockEntity(val variant: HTGeneratorVariant<*, *>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(variant.blockEntityHolder, pos, state) {
    //    Ticking    //

    override val energyUsage: Int = variant.energyRate

    //    Energy Storage    //

    protected val energyStorage: HTBasicEnergyStorage = HTBasicEnergyStorage.output(::setOnlySave, 16_000)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        energyStorage.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        energyStorage.deserialize(input)
    }

    final override fun getEnergyStorage(direction: Direction?): HTBasicEnergyStorage = energyStorage
}
