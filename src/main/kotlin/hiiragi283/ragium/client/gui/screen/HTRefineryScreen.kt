package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.gui.screen.HTProgressBar
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.HTFluidOnlyMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTRefineryScreen(menu: HTFluidOnlyMenu, inventory: Inventory, title: Component) :
    HTMachineScreen<HTFluidOnlyMenu>(RagiumAPI.id("textures/gui/container/refinery.png"), menu, inventory, title),
    HTFluidScreen {
    override val progressBar: HTProgressBar = HTProgressBar.burn(HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosX(1))

    private lateinit var fluidWidget: HTFluidWidget
    private lateinit var fluidWidget1: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget =
            addRenderableWidget(createFluidTankWidget(0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0)))
        fluidWidget1 =
            addRenderableWidget(createFluidTankWidget(1, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0)))
    }

    //    HTFluidScreen    //

    override fun setFluidStack(index: Int, stack: FluidStack) {
        when (index) {
            0 -> fluidWidget
            1 -> fluidWidget1
            else -> return
        }.stack = stack
    }

    override fun iterator(): Iterator<HTFluidWidget> = listOf(fluidWidget, fluidWidget1).iterator()
}
