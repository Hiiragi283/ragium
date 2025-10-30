package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.screen.HTPositionScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTAmountSetter
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
import hiiragi283.ragium.client.gui.component.HTEnergyStorageWidget
import hiiragi283.ragium.client.gui.component.HTExperienceStorageWidget
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.storage.energy.HTBasicEnergyStorage
import hiiragi283.ragium.common.storage.experience.HTBasicExperienceStorage
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.core.BlockPos
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
    ),
    HTPositionScreen {
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

    final override fun checkPosition(blockPos: BlockPos): Boolean = blockEntity.blockPos == blockPos

    //    Extensions    //

    fun createFluidTank(tank: HTFluidStackTank, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createTank(tank, tank::setStackUnchecked, startX + x, startY + y).apply(::addRenderableWidget)

    fun createFluidSlot(tank: HTFluidStackTank, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createSlot(tank, tank::setStackUnchecked, startX + x, startY + y).apply(::addRenderableWidget)

    fun createEnergyWidget(
        storage: HTEnergyStorage,
        amountSetter: HTAmountSetter.IntSized,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyStorageWidget = HTEnergyStorageWidget(
        storage,
        amountSetter,
        startX + x,
        startY + y,
    ).apply(::addRenderableWidget)

    fun createEnergyWidget(
        storage: HTBasicEnergyStorage,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyStorageWidget = createEnergyWidget(storage, storage::setAmountUnchecked, x, y)

    fun createExperienceTank(storage: HTBasicExperienceStorage, x: Int, y: Int): HTExperienceStorageWidget =
        HTExperienceStorageWidget.createTank(storage, storage::setAmountUnchecked, startX + x, startY + y).apply(::addRenderableWidget)

    fun createExperienceSlot(storage: HTBasicExperienceStorage, x: Int, y: Int): HTExperienceStorageWidget =
        HTExperienceStorageWidget.createSlot(storage, storage::setAmountUnchecked, startX + x, startY + y).apply(::addRenderableWidget)

    //    Impl    //

    private class Impl<BE : HTBlockEntity>(
        override val texture: ResourceLocation?,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : HTBlockEntityContainerScreen<BE>(menu, inventory, title)
}
