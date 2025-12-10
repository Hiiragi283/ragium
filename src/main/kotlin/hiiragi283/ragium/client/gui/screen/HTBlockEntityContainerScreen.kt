package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import hiiragi283.ragium.client.gui.component.HTEnergyWidget
import hiiragi283.ragium.client.gui.component.HTFakeSlotWidget
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.storage.energy.battery.HTBasicEnergyBattery
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import java.util.function.IntConsumer
import java.util.function.Supplier

open class HTBlockEntityContainerScreen<BE : HTBlockEntity> : HTContainerScreen<HTBlockEntityContainerMenu<BE>> {
    companion object {
        @JvmStatic
        fun createTexture(texture: String): ResourceLocation = RagiumAPI.id("textures", "gui", "container", "$texture.png")

        @JvmStatic
        fun <BE : HTBlockEntity> getMenuTexture(menu: HTBlockEntityContainerMenu<BE>): ResourceLocation = HTBlockEntity
            .getBlockEntityType(menu.context.blockHolder)
            .id
            .withPath { "textures/gui/container/$it.png" }
    }

    constructor(
        texture: ResourceLocation,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : super(
        texture,
        menu,
        inventory,
        title,
    )

    constructor(menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, title: Component) : super(
        getMenuTexture(menu),
        menu,
        inventory,
        title,
    )

    val blockEntity: BE get() = menu.context

    //    Extensions    //

    fun createFakeSlot(getter: Supplier<ItemStack>, x: Int, y: Int): HTFakeSlotWidget =
        HTFakeSlotWidget(getter, startX + x, startY + y).apply(::addRenderableWidget)

    fun createFluidTank(view: HTFluidView, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createTank(view, startX + x, startY + y).apply(::addRenderableWidget)

    fun createFluidSlot(view: HTFluidView, x: Int, y: Int): HTFluidTankWidget =
        HTFluidTankWidget.createSlot(view, startX + x, startY + y).apply(::addRenderableWidget)

    fun createEnergyWidget(
        battery: HTEnergyBattery,
        amountSetter: IntConsumer,
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
