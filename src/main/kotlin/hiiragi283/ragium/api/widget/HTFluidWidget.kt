package hiiragi283.ragium.api.widget

import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids

open class HTFluidWidget() : WWidget() {
    constructor(fluid: Fluid) : this() {
        this.fluid = fluid
    }

    constructor(fluid: Fluid, amount: Long) : this() {
        this.fluid = fluid
        this.amount = amount
    }

    var fluid: Fluid = Fluids.EMPTY
    var amount: Long = -1

    override fun canResize(): Boolean = true
}
