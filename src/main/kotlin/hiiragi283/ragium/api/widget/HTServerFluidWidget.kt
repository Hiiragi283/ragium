package hiiragi283.ragium.api.widget

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage

open class HTServerFluidWidget(storage: SlottedStorage<FluidVariant>, index: Int) : HTFluidWidget(storage, index) {
    override var variant: FluidVariant
        get() = storage.getSlot(index).resource
        set(value) {}
    override var amount: Long
        get() = storage.getSlot(index).amount
        set(value) {}

    override fun canResize(): Boolean = true
}
