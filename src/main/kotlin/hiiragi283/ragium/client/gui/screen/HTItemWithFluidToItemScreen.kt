package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotHelper
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTItemWithFluidToItemScreen(
    texture: ResourceLocation,
    menu: HTDefinitionContainerMenu,
    inventory: Inventory,
    title: Component,
) : HTBasicMachineScreen(texture, menu, inventory, title),
    HTFluidScreen {
    companion object {
        @JvmStatic
        fun infuser(menu: HTDefinitionContainerMenu, inventory: Inventory, title: Component): HTItemWithFluidToItemScreen =
            HTItemWithFluidToItemScreen(
                RagiumAPI.id("textures/gui/container/infuser.png"),
                menu,
                inventory,
                title,
            )

        @JvmStatic
        fun solidifier(menu: HTDefinitionContainerMenu, inventory: Inventory, title: Component): HTItemWithFluidToItemScreen =
            HTItemWithFluidToItemScreen(
                RagiumAPI.id("textures/gui/container/solidifier.png"),
                menu,
                inventory,
                title,
            )
    }

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
