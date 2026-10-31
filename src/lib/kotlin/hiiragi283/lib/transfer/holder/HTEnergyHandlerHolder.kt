package hiiragi283.lib.transfer.holder

import hiiragi283.lib.transfer.energy.HTEnergyStorage
import net.minecraft.core.Direction

/**
 * [HTEnergyStorage]向けの[HTCapabilityHolder]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTEnergyHandlerHolder : HTCapabilityHolder {
    fun getHandler(side: Direction?): HTEnergyStorage?
}
