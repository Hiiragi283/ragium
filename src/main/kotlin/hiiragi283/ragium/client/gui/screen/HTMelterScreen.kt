package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTMelterScreen(menu: HTBlockEntityContainerMenu<HTMelterBlockEntity>, inventory: Inventory, title: Component) :
    HTMachineScreen<HTMelterBlockEntity>(RagiumAPI.id("textures/gui/container/melter.png"), menu, inventory, title),
    HTFluidScreen {
    private lateinit var fluidWidget: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget =
            addRenderableWidget(createFluidWidget(0, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0)))
    }

    //    HTFluidScreen    //

    override fun setFluidStack(index: Int, stack: FluidStack) {
        fluidWidget.stack = stack
    }

    override fun iterator(): Iterator<HTFluidWidget> = listOf(fluidWidget).iterator()
}
