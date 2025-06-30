package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.screen.HTDefinitionContainerScreen
import hiiragi283.ragium.common.inventory.HTExtractorMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class HTExtractorScreen(menu: HTExtractorMenu, inventory: Inventory, title: Component) :
    HTDefinitionContainerScreen<HTExtractorMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/extractor.png")

    override val progressPosX: Int = HTSlotHelper.getSlotPosX(3.5)
    override val progressPosY: Int = HTSlotHelper.getSlotPosY(1)
}
