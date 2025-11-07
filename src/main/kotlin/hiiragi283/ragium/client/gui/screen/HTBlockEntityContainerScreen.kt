package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTAmountSetter
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.client.gui.component.HTEnergyStorageWidget
import hiiragi283.ragium.client.gui.component.HTExperienceStorageWidget
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.storage.energy.battery.HTBasicEnergyBattery
import hiiragi283.ragium.common.storage.experience.tank.HTBasicExperienceTank
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

abstract class HTBlockEntityContainerScreen<BE : HTBlockEntity>(
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTContainerScreen<HTBlockEntityContainerMenu<BE>>(
        menu,
        inventory,
        title,
    ) {
    companion object {
        @JvmStatic
        fun <BE : HTBlockEntity> createSimple(
            name: String,
        ): MenuScreens.ScreenConstructor<HTBlockEntityContainerMenu<BE>, HTBlockEntityContainerScreen<BE>> =
            createSimple(RagiumAPI.id("textures/gui/container/$name.png"))

        @JvmStatic
        fun <BE : HTBlockEntity> createSimple(
            texture: ResourceLocation,
        ): MenuScreens.ScreenConstructor<HTBlockEntityContainerMenu<BE>, HTBlockEntityContainerScreen<BE>> =
            MenuScreens.ScreenConstructor { menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, component: Component ->
                Impl(texture, menu, inventory, component)
            }
    }

    val blockEntity: BE get() = menu.context

    //    Extensions    //

    fun createFluidTank(tank: HTFluidStackTank, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createTank(tank, tank::setStackUnchecked, startX + x, startY + y).apply(::addRenderableWidget)

    fun createFluidSlot(tank: HTFluidStackTank, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createSlot(tank, tank::setStackUnchecked, startX + x, startY + y).apply(::addRenderableWidget)

    fun createEnergyWidget(
        battery: HTEnergyBattery,
        amountSetter: HTAmountSetter.IntSized,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyStorageWidget = HTEnergyStorageWidget(
        battery,
        amountSetter,
        startX + x,
        startY + y,
    ).apply(::addRenderableWidget)

    fun createEnergyWidget(
        battery: HTBasicEnergyBattery,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyStorageWidget = createEnergyWidget(battery, battery::setAmountUnchecked, x, y)

    fun createExperienceTank(tank: HTBasicExperienceTank, x: Int, y: Int): HTExperienceStorageWidget =
        HTExperienceStorageWidget.createTank(tank, tank::setAmountUnchecked, startX + x, startY + y).apply(::addRenderableWidget)

    fun createExperienceSlot(tank: HTBasicExperienceTank, x: Int, y: Int): HTExperienceStorageWidget =
        HTExperienceStorageWidget.createSlot(tank, tank::setAmountUnchecked, startX + x, startY + y).apply(::addRenderableWidget)

    //    Impl    //

    private class Impl<BE : HTBlockEntity>(
        override val texture: ResourceLocation?,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : HTBlockEntityContainerScreen<BE>(menu, inventory, title)
}
