package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.screen.HTMachineScreen
import hiiragi283.ragium.common.inventory.HTMelterMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class HTMelterScreen(menu: HTMelterMenu, inventory: Inventory, title: Component) :
    HTMachineScreen<HTMelterMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/melter.png")

    override val progressPosX: Int = HTSlotPos.getSlotPosX(3.5)
    override val progressPosY: Int = HTSlotPos.getSlotPosY(1)
}
