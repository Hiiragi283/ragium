package hiiragi283.ragium.api.widget

import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant

open class HTServerFluidWidget(storage: HTMachineFluidStorage, index: Int) : HTFluidWidget(storage, index) {
    override var variant: FluidVariant
        get() = storage[index].resource
        set(value) {}
    override var amount: Long
        get() = storage[index].amount
        set(value) {}

    override fun canResize(): Boolean = true
}
