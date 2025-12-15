package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.energy.IEnergyStorage

class HTEnergyCache {
    private val cacheMap: MutableMap<Direction, BlockCapabilityCache<IEnergyStorage, Direction?>> = hashMapOf()

    fun getStorage(level: ServerLevel, pos: BlockPos, side: Direction?): BlockCapabilityCache<IEnergyStorage, Direction?>? {
        if (side == null) return null
        return cacheMap.computeIfAbsent(side) {
            HTEnergyCapabilities.createCache(level, pos.relative(side), side.opposite)
        }
    }

    fun getBattery(level: ServerLevel, pos: BlockPos, side: Direction?): HTEnergyBattery? {
        val storage: IEnergyStorage = getStorage(level, pos, side)?.capability ?: return null
        return HTEnergyCapabilities.wrapStorage(storage, side)
    }
}
