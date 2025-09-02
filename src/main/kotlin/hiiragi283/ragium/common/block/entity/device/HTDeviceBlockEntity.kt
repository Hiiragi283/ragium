package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

abstract class HTDeviceBlockEntity(
    private val tickRate: IntSupplier,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTBlockEntity(type, pos, state) {
    constructor(variant: HTDeviceVariant, pos: BlockPos, state: BlockState) : this(
        variant.tickRate,
        variant.blockEntityHolder,
        pos,
        state,
    )

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = if (ticks >= tickRate.asInt) {
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
