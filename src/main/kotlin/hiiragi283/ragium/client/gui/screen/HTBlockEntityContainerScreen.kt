package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.screen.HTPositionScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.client.gui.component.HTEnergyBatteryWidget
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
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

    private fun getTank(index: Int): HTFluidTank.Mutable =
        blockEntity.getFluidTanks(blockEntity.getFluidSideFor())[index] as? HTFluidTank.Mutable
            ?: error("Fluid tank at $index is not mutable.")

    fun createFluidTank(index: Int, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createTank(getTank(index), startX + x, startY + y).apply(::addRenderableWidget)

    fun createFluidSlot(index: Int, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createSlot(getTank(index), startX + x, startY + y).apply(::addRenderableWidget)

    fun createEnergyWidget(
        key: ResourceKey<Level>,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyBatteryWidget = HTEnergyBatteryWidget
        .createNetwork(
            key,
            startX + x,
            startY + y,
        ).apply(::addRenderableWidget)

    //    Impl    //

    private class Impl<BE : HTBlockEntity>(
        override val texture: ResourceLocation?,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : HTBlockEntityContainerScreen<BE>(menu, inventory, title)
}
