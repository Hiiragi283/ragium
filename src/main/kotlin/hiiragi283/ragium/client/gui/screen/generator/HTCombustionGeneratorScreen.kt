package hiiragi283.ragium.client.gui.screen.generator

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.common.block.entity.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTCombustionGeneratorScreen(
    menu: HTBlockEntityContainerMenu<HTCombustionGeneratorBlockEntity>,
    inventory: Inventory,
    title: Component,
) : HTGeneratorScreen<HTCombustionGeneratorBlockEntity>(menu, inventory, title) {
    private lateinit var fluidWidget: HTFluidWidget
    private lateinit var fluidWidget1: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget = createFluidTank(blockEntity.coolantTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        fluidWidget1 = createFluidTank(blockEntity.fuelTank, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0))
    }

    override fun getFluidWidgets(): List<HTFluidWidget> = listOf(fluidWidget, fluidWidget1)
}
