package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
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
    menu: HTBlockEntityContainerMenu<HTEnergyNetworkAccessBlockEntity>,
    inventory: Inventory,
    title: Component,
) : HTBlockEntityContainerScreen<HTEnergyNetworkAccessBlockEntity>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/energy_network_access.png")

    override fun init() {
        super.init()
        // Energy Widget
        createEnergyWidget(menu.context.getDimension(), HTSlotHelper.getSlotPosX(4))
    }
}
