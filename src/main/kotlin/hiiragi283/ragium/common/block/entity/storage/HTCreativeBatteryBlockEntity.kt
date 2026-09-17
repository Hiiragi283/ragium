package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler

class HTCreativeBatteryBlockEntity(worldPosition: BlockPos, blockState: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.CREATIVE_BATTERY.get(), worldPosition, blockState) {

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        for (direction: Direction in Direction.entries) {
            EnergyHandlerUtil.move(
                InfiniteEnergyHandler.INSTANCE,
                HTEnergyCapabilities.getCapability(level, pos.relative(direction), direction.opposite),
                Int.MAX_VALUE,
                null
            )
        }
        return false
    }
}
