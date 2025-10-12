package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.roundToInt

class HTSolarGeneratorBlockEntity(pos: BlockPos, state: BlockState) : HTGeneratorBlockEntity(HTGeneratorVariant.SOLAR, pos, state) {
    override fun openGui(player: Player, title: Component): InteractionResult = InteractionResult.PASS

    override fun onUpdateServer(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: HTEnergyBattery,
    ): Boolean {
        val multiplier: Float = getGenerationMultiplier(level, pos)
        if (multiplier < 0f) return false
        return network.insertEnergy((energyUsage * multiplier).roundToInt(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL) > 0
    }

    private fun getGenerationMultiplier(level: ServerLevel, pos: BlockPos): Float {
        // 太陽光を供給できる場合は，その倍率を返す
        val state: BlockState = level.getBlockState(pos.above())
        val power: Float? = RagiumDataMaps.INSTANCE.getSolarPower(level.registryAccess(), state.blockHolder)
        if (power != null) return power
        return when {
            // 空のない次元では停止
            !level.dimensionType().hasSkyLight -> 0f
            // 日中出ない場合はスキップ
            !level.isDay -> 0f
            // 現在地から空が見えない場合はスキップ
            level.canSeeSky(pos.above()) -> 1f
            else -> 0f
        }
    }
}
