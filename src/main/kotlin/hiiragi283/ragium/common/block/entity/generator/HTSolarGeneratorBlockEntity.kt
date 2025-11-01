package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.registry.HTSolarPower
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.roundToInt

class HTSolarGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTGeneratorBlockEntity(RagiumBlocks.SOLAR_PANEL_CONTROLLER, pos, state) {
    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val multiplier: Float = getGenerationMultiplier(level, pos)
        if (multiplier < 0f) return false
        return battery.insert((energyUsage * multiplier).roundToInt(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL) > 0
    }

    private fun getGenerationMultiplier(level: ServerLevel, pos: BlockPos): Float {
        // 太陽光を供給できる場合は，その倍率を返す
        val power: Float? = level
            .registryAccess()
            .lookupOrThrow(RagiumAPI.SOLAR_POWER_KEY)
            .let { lookup: HolderLookup<HTSolarPower> -> HTSolarPower.getSolarPower(lookup, level, pos.above()) }
            ?.takeIf { it > 0f }
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
