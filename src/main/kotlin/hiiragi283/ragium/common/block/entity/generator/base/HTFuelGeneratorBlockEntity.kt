package hiiragi283.ragium.common.block.entity.generator.base

import hiiragi283.ragium.api.block.attribute.getAttributeFront
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.util.HTEnergyHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * @see com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity
 */
abstract class HTFuelGeneratorBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTGeneratorBlockEntity(blockHolder, pos, state) {
    var remainingBurnTime: Int = 0
        private set
    var maxBurnTime: Int = 0
        private set

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // バッテリー内の電力を正面に自動搬出させる
        val frontBattery: HTEnergyBattery? = energyCache.getBattery(level, pos, state.getAttributeFront())
        HTEnergyHelper.moveEnergy(this.battery, frontBattery, this.battery::onContentsChanged)
        // 燃料がある場合，それを消費する
        if (remainingBurnTime > 0) {
            remainingBurnTime--
            battery.currentEnergyPerTick = modifyValue(HTMachineUpgrade.Key.ENERGY_GENERATION) { battery.baseEnergyPerTick * it }
            battery.generate()
            return true
        } else {
            // 新しく燃焼時間を計算する
            val newBurnTime: Int = getNewBurnTime(level, pos)
            if (newBurnTime > 0) {
                remainingBurnTime = newBurnTime
                onFuelUpdated(level, pos, true)
            } else {
                onFuelUpdated(level, pos, false)
            }
            maxBurnTime = newBurnTime
            return newBurnTime > 0
        }
    }

    protected abstract fun getNewBurnTime(level: ServerLevel, pos: BlockPos): Int

    protected abstract fun onFuelUpdated(level: ServerLevel, pos: BlockPos, isSucceeded: Boolean)
}
