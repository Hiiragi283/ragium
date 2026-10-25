package hiiragi283.lib.transfer.holder

import hiiragi283.lib.transfer.energy.HTEnergyHandler
import net.minecraft.core.Direction

/**
 * [HTEnergyHandler]向けの[HTCapabilityHolder]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTEnergyHandlerHolder : HTCapabilityHolder {
    fun getHandler(side: Direction?): HTEnergyHandler?
}
