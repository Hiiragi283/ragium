package hiiragi283.ragium.api.storage.holder

import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import net.minecraft.core.Direction

/**
 * [HTEnergyBattery]向けの[HTCapabilityHolder]の拡張インターフェース
 */
interface HTEnergyStorageHolder : HTCapabilityHolder {
    fun getEnergyBattery(side: Direction?): HTEnergyBattery?
}
