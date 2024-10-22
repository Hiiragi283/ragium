package hiiragi283.ragium.api.widget

import io.github.cottonmc.cotton.gui.widget.WWidget
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage
import net.minecraft.fluid.Fluid

open class HTFluidVariantWidget(val storage: SlottedStorage<FluidVariant>, val index: Int) : WWidget() {
    val variant: FluidVariant
        get() = storage.getSlot(index).resource
    val fluid: Fluid
        get() = variant.fluid
    val amount: Long
        get() = storage.getSlot(index).amount

    override fun canResize(): Boolean = true
}
