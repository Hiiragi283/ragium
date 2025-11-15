package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTEnergyNetworkAccessScreen(
    texture: ResourceLocation,
    menu: HTBlockEntityContainerMenu<HTEnergyNetworkAccessBlockEntity>,
    inventory: Inventory,
    title: Component,
) : HTBlockEntityContainerScreen<HTEnergyNetworkAccessBlockEntity>(texture, menu, inventory, title) {
    override fun init() {
        super.init()
        // Energy Widget
        createEnergyWidget(blockEntity.battery, {}, HTSlotHelper.getSlotPosX(4))
    }
}
