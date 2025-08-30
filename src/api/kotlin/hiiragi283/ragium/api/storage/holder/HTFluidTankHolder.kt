package hiiragi283.ragium.api.storage.holder

import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import net.minecraft.core.Direction

/**
 * [HTFluidTank]向けの[HTCapabilityHolder]の拡張インターフェース
 * @see [mekanism.common.capabilities.holder.fluid.IFluidTankHolder]
 */
interface HTFluidTankHolder : HTCapabilityHolder {
    fun getFluidTank(side: Direction?): List<HTFluidTank>
}
