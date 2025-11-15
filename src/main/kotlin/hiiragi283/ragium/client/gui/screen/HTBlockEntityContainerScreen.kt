package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTAmountSetter
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import hiiragi283.ragium.client.gui.component.HTEnergyWidget
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.storage.energy.battery.HTBasicEnergyBattery
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

open class HTBlockEntityContainerScreen<BE : HTBlockEntity>(
    texture: ResourceLocation,
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTContainerScreen<HTBlockEntityContainerMenu<BE>>(
        texture,
        menu,
        inventory,
        title,
    ) {
    val blockEntity: BE get() = menu.context

    //    Extensions    //

    fun createFluidTank(view: HTFluidView, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createTank(view, startX + x, startY + y).apply(::addRenderableWidget)

    fun createFluidSlot(view: HTFluidView, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createSlot(view, startX + x, startY + y).apply(::addRenderableWidget)

    fun createEnergyWidget(
        battery: HTEnergyBattery,
        amountSetter: HTAmountSetter.IntSized,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyWidget = HTEnergyWidget(
        battery,
        amountSetter,
        startX + x,
        startY + y,
    ).apply(::addRenderableWidget)

    fun createEnergyWidget(
        battery: HTBasicEnergyBattery,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyWidget = createEnergyWidget(battery, battery::setAmountUnchecked, x, y)
}
