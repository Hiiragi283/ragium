package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.screen.HTDefinitionContainerScreen
import hiiragi283.ragium.common.inventory.HTBlockBreakerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class HTBlockBreakerScreen(menu: HTBlockBreakerMenu, inventory: Inventory, title: Component) :
    HTDefinitionContainerScreen<HTBlockBreakerMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/block_breaker.png")

    override val progressPosX: Int = HTSlotHelper.getSlotPosX(5.5)
    override val progressPosY: Int = HTSlotHelper.getSlotPosY(1)
}
