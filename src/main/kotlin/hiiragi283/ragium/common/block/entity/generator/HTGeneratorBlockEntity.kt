package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.storage.energy.battery.HTMachineEnergyBattery
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * 電力を生産する設備に使用される[HTMachineBlockEntity]の拡張クラス
 */
abstract class HTGeneratorBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(blockHolder, pos, state) {
    final override fun createBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener): HTMachineEnergyBattery<*> =
        builder.addSlot(HTAccessConfig.OUTPUT_ONLY, HTMachineEnergyBattery.output(listener, this))
}
