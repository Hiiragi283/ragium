package hiiragi283.ragium.api.storage.holder

import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * [IEnergyStorage]向けの[HTCapabilityHolder]の拡張インターフェース
 */
interface HTEnergyStorageHolder : HTCapabilityHolder {
    fun getEnergyHandler(side: Direction?): IEnergyStorage?
}
