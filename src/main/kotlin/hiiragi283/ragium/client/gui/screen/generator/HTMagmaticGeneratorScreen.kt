package hiiragi283.ragium.client.gui.screen.generator

import hiiragi283.ragium.common.block.entity.generator.HTMagmaticGeneratorBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTMagmaticGeneratorScreen(menu: HTBlockEntityContainerMenu<HTMagmaticGeneratorBlockEntity>, inventory: Inventory, title: Component) :
    HTGeneratorScreen<HTMagmaticGeneratorBlockEntity>(menu, inventory, title) {
    override fun init() {
        super.init()
        createFluidTank(blockEntity.fuelTank, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0))
    }
}
