package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.gui.screen.HTFluidScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.generator.HTFuelGeneratorBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
class HTFuelGeneratorScreen(menu: HTBlockEntityContainerMenu<HTFuelGeneratorBlockEntity>, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTFuelGeneratorBlockEntity>(menu, inventory, title),
    HTFluidScreen {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/fuel_generator.png")
    private lateinit var fluidWidget: HTFluidWidget

    override fun init() {
        super.init()
        // Energy Widget
        createEnergyWidget(blockEntity.getDimension(), HTSlotHelper.getSlotPosX(4))
        // Fluid Widget
        fluidWidget = createFluidTank(0, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0))
    }

    //    HTFluidScreen    //

    override fun setFluidStack(index: Int, stack: FluidStack) {
        fluidWidget.stack = stack
    }

    override fun iterator(): Iterator<HTFluidWidget> = listOf(fluidWidget).iterator()
}
