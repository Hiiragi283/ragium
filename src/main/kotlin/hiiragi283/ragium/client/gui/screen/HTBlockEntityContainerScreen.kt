package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.gui.screen.HTPositionScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.fluid.HTEmptyFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.client.gui.component.HTEnergyNetworkWidget
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level

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
    val blockEntity: BE get() = menu.context

    final override fun checkPosition(blockPos: BlockPos): Boolean = blockEntity.blockPos == blockPos

    //    Extensions    //

    fun getTank(index: Int): HTFluidTank = blockEntity.getFluidTanks(blockEntity.getFluidSideFor()).getOrNull(index) ?: HTEmptyFluidTank

    fun createFluidTank(index: Int, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createTank(getTank(index), x, y).apply(::addRenderableWidget)

    fun createFluidSlot(index: Int, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createSlot(getTank(index), x, y).apply(::addRenderableWidget)

    fun createEnergyWidget(
        key: ResourceKey<Level>,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyNetworkWidget = HTEnergyNetworkWidget(
        key,
        startX + x,
        startY + y,
    ).apply(::addRenderableWidget)
}
