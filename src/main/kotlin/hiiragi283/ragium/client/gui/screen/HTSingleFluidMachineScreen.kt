package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTChancedItemOutputBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTSingleFluidMachineScreen<BE : HTMachineBlockEntity>(
    private val x: Int,
    private val y: Int,
    texture: ResourceLocation,
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTMachineScreen<BE>(texture, menu, inventory, title),
    HTFluidScreen {
    companion object {
        @JvmStatic
        fun chancedItemOutput(
            menu: HTBlockEntityContainerMenu<HTChancedItemOutputBlockEntity<*, *>>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidMachineScreen<HTChancedItemOutputBlockEntity<*, *>> = HTSingleFluidMachineScreen(
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(2),
            RagiumAPI.id("textures/gui/container/crusher.png"),
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun melter(
            menu: HTBlockEntityContainerMenu<HTMelterBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidMachineScreen<HTMelterBlockEntity> = HTSingleFluidMachineScreen(
            HTSlotHelper.getSlotPosX(5.5),
            HTSlotHelper.getSlotPosY(0),
            RagiumAPI.id("textures/gui/container/melter.png"),
            menu,
            inventory,
            title,
        )
    }

    private lateinit var fluidWidget: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget = createFluidSlot(0, x, y)
    }

    //    HTFluidScreen    //

    override fun setFluidStack(index: Int, stack: FluidStack) {
        fluidWidget.stack = stack
    }

    override fun getFluidWidgets(): Iterable<HTFluidWidget> = listOf(fluidWidget)
}
