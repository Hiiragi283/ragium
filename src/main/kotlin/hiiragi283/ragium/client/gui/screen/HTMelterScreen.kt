package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.screen.HTDefinitionContainerScreen
import hiiragi283.ragium.common.inventory.HTMelterMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class HTMelterScreen(menu: HTMelterMenu, inventory: Inventory, title: Component) :
    HTDefinitionContainerScreen<HTMelterMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/melter.png")

    override val progressPosX: Int = HTSlotHelper.getSlotPosX(3.5)
    override val progressPosY: Int = HTSlotHelper.getSlotPosY(1)
}
