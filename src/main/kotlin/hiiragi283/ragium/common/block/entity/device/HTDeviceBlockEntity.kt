package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTTickAwareBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState

abstract class HTDeviceBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(
        type,
        pos,
        state,
    ),
    MenuProvider {
    protected var currentTicks: Int = 0
        private set

    protected fun createData(): ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> currentTicks
            1 -> 20
            else -> -1
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> currentTicks = value
            }
        }

        override fun getCount(): Int = 2
    }

    final override fun serverTickPre(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        currentTicks++
        if (currentTicks < 20) return TriState.DEFAULT
        currentTicks = 0
        return serverTick(level, pos, state)
    }

    protected abstract fun serverTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState

    final override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        result: TriState,
    ) {
        if (result.isFalse) return
        exportItems(level, pos)
        exportFluids(level, pos)
    }

    //    Menu    //

    final override fun getDisplayName(): Component = blockState.block.name
}
