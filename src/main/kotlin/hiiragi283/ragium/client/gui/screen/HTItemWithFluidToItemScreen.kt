package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.gui.screen.HTProgressBar
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.HTItemWithFluidToItemMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTItemWithFluidToItemScreen(
    texture: ResourceLocation,
    factory: (Int, Int) -> HTProgressBar,
    menu: HTItemWithFluidToItemMenu,
    inventory: Inventory,
    title: Component,
) : HTMachineScreen<HTItemWithFluidToItemMenu>(texture, menu, inventory, title),
    HTFluidScreen {
    companion object {
        @JvmStatic
        fun infuser(menu: HTItemWithFluidToItemMenu, inventory: Inventory, title: Component): HTItemWithFluidToItemScreen =
            HTItemWithFluidToItemScreen(
                RagiumAPI.id("textures/gui/container/infuser.png"),
                HTProgressBar::infuse,
                menu,
                inventory,
                title,
            )

        @JvmStatic
        fun solidifier(menu: HTItemWithFluidToItemMenu, inventory: Inventory, title: Component): HTItemWithFluidToItemScreen =
            HTItemWithFluidToItemScreen(
                RagiumAPI.id("textures/gui/container/solidifier.png"),
                HTProgressBar::arrow,
                menu,
                inventory,
                title,
            )
    }

    override val progressBar: HTProgressBar = factory(HTSlotHelper.getSlotPosX(4.5), HTSlotHelper.getSlotPosY(1))

    private lateinit var fluidWidget: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget =
            addRenderableWidget(createFluidTankWidget(0, HTSlotHelper.getSlotPosX(1.5), HTSlotHelper.getSlotPosY(0)))
    }

    //    HTFluidScreen    //

    override fun setFluidStack(index: Int, stack: FluidStack) {
        fluidWidget.stack = stack
    }

    override fun iterator(): Iterator<HTFluidWidget> = listOf(fluidWidget).iterator()
}
