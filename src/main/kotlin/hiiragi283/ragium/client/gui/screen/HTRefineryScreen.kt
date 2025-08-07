package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotHelper
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTRefineryScreen(menu: HTDefinitionContainerMenu, inventory: Inventory, title: Component) :
    HTBasicMachineScreen(RagiumAPI.id("textures/gui/container/refinery.png"), menu, inventory, title),
    HTFluidScreen {
    private lateinit var fluidWidget: HTFluidWidget
    private lateinit var fluidWidget1: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget =
            addRenderableWidget(
                createFluidTankWidget(
                    0,
                    HTSlotHelper.getSlotPosX(2),
                    HTSlotHelper.getSlotPosY(0),
                    height = 18 * 2 - 2,
                ),
            )
        fluidWidget1 =
            addRenderableWidget(createFluidTankWidget(1, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0)))
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
