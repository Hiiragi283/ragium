package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.level.block.state.BlockState

abstract class HTDeviceBlockEntity(variant: HTDeviceVariant, pos: BlockPos, state: BlockState) :
    HTBlockEntity(
        variant.blockEntityHolder,
        pos,
        state,
    ),
    HTHandlerBlockEntity {
    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = if (ticks >= 20) {
        ticks = 0
        actionServer(level, pos, state)
    } else {
        false
    }

    protected abstract fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

    val progress: Float
        get() {
            val totalTick: Int = ticks
            val maxTicks = 20
            val fixedTotalTicks: Int = totalTick % maxTicks
            return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
        }
}
