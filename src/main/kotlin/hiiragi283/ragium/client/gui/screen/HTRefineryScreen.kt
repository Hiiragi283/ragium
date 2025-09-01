package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTProgressWidget
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTRefineryScreen(menu: HTBlockEntityContainerMenu<HTRefineryBlockEntity>, inventory: Inventory, title: Component) :
    HTMachineScreen<HTRefineryBlockEntity>(
        RagiumAPI.id("textures/gui/container/refinery.png"),
        menu,
        inventory,
        title,
    ),
    HTFluidScreen {
    private lateinit var fluidWidget: HTFluidWidget
    private lateinit var fluidWidget1: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget = createFluidWidget(0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        fluidWidget1 = createFluidWidget(1, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0))
    }

    override fun addProgressBar(consumer: (HTProgressWidget) -> Unit) {
        consumer(
            HTProgressWidget.burn(
                menu.context::progress,
                startX + HTSlotHelper.getSlotPosX(4),
                startY + HTSlotHelper.getSlotPosX(2),
            ),
        )
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
