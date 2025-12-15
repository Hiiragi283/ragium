package hiiragi283.ragium.client.gui.screen.processor

import hiiragi283.ragium.common.block.entity.processor.HTAdvancedMixerBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTAdvancedMixerScreen(menu: HTBlockEntityContainerMenu<HTAdvancedMixerBlockEntity>, inventory: Inventory, title: Component) :
    HTProcessorScreen<HTAdvancedMixerBlockEntity>(menu, inventory, title) {
    override fun init() {
        super.init()
        createFluidSlot(blockEntity.firstInputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
        createFluidSlot(blockEntity.secondInputTank, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(2))
        createFluidSlot(blockEntity.outputTank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(2))
    }
}
