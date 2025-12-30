package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.client.gui.component.HTArrowProgressWidget
import hiiragi283.core.client.gui.component.HTBurnProgressWidget
import hiiragi283.core.client.gui.screen.HTBlockEntityContainerScreen
import hiiragi283.core.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.processing.HTProcessorBlockEntity
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

abstract class HTProcessorScreen<BE : HTProcessorBlockEntity<*, *>, MENU : HTBlockEntityContainerMenu<BE>> :
    HTBlockEntityContainerScreen<BE, MENU> {
    constructor(texture: ResourceLocation, menu: MENU, inventory: Inventory, title: Component) : super(
        texture,
        menu,
        inventory,
        title,
    )

    constructor(menu: MENU, inventory: Inventory, title: Component) : super(
        menu,
        inventory,
        title,
    )

    //    Extensions    //

    protected fun addFluidTank(x: Int, y: Int, view: HTFluidView): HTFluidTankWidget =
        addFluidWidget(x, y, ::HTFluidTankWidget.partially1(view))

    protected fun addProgress(x: Int, y: Int): HTArrowProgressWidget =
        addWidget(x, y, ::HTArrowProgressWidget.partially1(blockEntity::getProgress))

    protected fun addBurning(x: Int, y: Int): HTBurnProgressWidget =
        addWidget(x, y + 2, ::HTBurnProgressWidget.partially1(blockEntity::getProgress))
}
