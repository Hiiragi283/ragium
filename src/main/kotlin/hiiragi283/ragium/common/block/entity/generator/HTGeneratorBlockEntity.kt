package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.block.attribute.HTEnergyBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttributeOrThrow
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.storage.energy.HTBasicEnergyStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * 電力を生産する設備に使用される[HTMachineBlockEntity]の拡張クラス
 */
abstract class HTGeneratorBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(blockHolder, pos, state) {
    //    Energy Storage    //

    val energyStorage: HTBasicEnergyStorage = HTBasicEnergyStorage.output(
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
}
