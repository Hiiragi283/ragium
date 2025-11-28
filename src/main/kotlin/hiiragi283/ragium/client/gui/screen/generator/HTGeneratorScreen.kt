package hiiragi283.ragium.client.gui.screen.generator

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.screen.HTBlockEntityContainerScreen
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class HTGeneratorScreen<BE : HTGeneratorBlockEntity>(menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<BE>(menu, inventory, title) {
    override fun init() {
        super.init()
        // Energy Widget
        createEnergyWidget(blockEntity.battery, HTSlotHelper.getSlotPosX(4))
    }
}
