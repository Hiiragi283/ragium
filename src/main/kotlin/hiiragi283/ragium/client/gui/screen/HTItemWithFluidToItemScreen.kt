package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.component.HTProgressWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTInfuserBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSolidifierBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTItemWithFluidToItemScreen<BE : HTMachineBlockEntity>(
    texture: ResourceLocation,
    private val factory: (() -> Float, Int, Int) -> HTProgressWidget,
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTMachineScreen<BE>(texture, menu, inventory, title),
    HTFluidScreen {
    companion object {
        @JvmStatic
        fun infuser(
            menu: HTBlockEntityContainerMenu<HTInfuserBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTItemWithFluidToItemScreen<HTInfuserBlockEntity> = HTItemWithFluidToItemScreen(
            RagiumAPI.id("textures/gui/container/infuser.png"),
            HTProgressWidget::infuse,
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun solidifier(
            menu: HTBlockEntityContainerMenu<HTSolidifierBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTItemWithFluidToItemScreen<HTSolidifierBlockEntity> = HTItemWithFluidToItemScreen(
            RagiumAPI.id("textures/gui/container/solidifier.png"),
            HTProgressWidget::arrow,
            menu,
            inventory,
            title,
        )
    }

    private lateinit var fluidWidget: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget =
            addRenderableWidget(createFluidWidget(0, HTSlotHelper.getSlotPosX(1.5), HTSlotHelper.getSlotPosY(0)))
    }

    override fun addProgressBar(consumer: (HTProgressWidget) -> Unit) {
        consumer(factory(menu.context::progress, startX + HTSlotHelper.getSlotPosX(4.5), startY + HTSlotHelper.getSlotPosY(1)))
    }

    //    HTFluidScreen    //

    override fun setFluidStack(index: Int, stack: FluidStack) {
        fluidWidget.stack = stack
    }

    override fun iterator(): Iterator<HTFluidWidget> = listOf(fluidWidget).iterator()
}
