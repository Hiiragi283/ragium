package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.fluid.HTFluidTank

/**
 * @see [mekanism.common.inventory.slot.IFluidHandlerSlot]
 */
interface HTFluidItemSlot : HTItemSlot {
    fun getFluidTank(): HTFluidTank

    var isDraining: Boolean
    var isFilling: Boolean
}
