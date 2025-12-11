package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTSolarPanelControllerBlockEntity(pos: BlockPos, state: BlockState) :
    HTGeneratorBlockEntity(RagiumBlocks.SOLAR_PANEL_CONTROLLER, pos, state) {
    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {}

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (this.ticks % 20 != 0) return false
        val panels: Int = BlockPos
            .betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))
            .filter { posIn: BlockPos ->
                val stateIn: BlockState = level.getBlockState(posIn)
                checkSolarPanel(level, posIn, stateIn)
            }.size
        if (panels == 0) return false

        battery.currentEnergyPerTick =
            modifyValue(HTMachineUpgrade.Key.ENERGY_GENERATION) { battery.baseEnergyPerTick * (panels / 4) * it }
        return battery.generate() > 0
    }

    private fun checkSolarPanel(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = when {
        !state.`is`(RagiumBlocks.SOLAR_PANEL_UNIT) -> false
        // 空のない次元では停止
        !level.dimensionType().hasSkyLight -> false
        // 日中出ない場合はスキップ
        !level.isDay -> false
        // 現在地から空が見えない場合はスキップ
        !level.canSeeSky(pos.above()) -> false
        else -> true
    }
}
