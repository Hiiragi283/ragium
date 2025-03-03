package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.screen.HTMachineScreen
import hiiragi283.ragium.common.inventory.HTInfuserMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTInfuserScreen(menu: HTInfuserMenu, inventory: Inventory, title: Component) :
    HTMachineScreen<HTInfuserMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/infuser.png")

    override val progressX: Int = HTSlotPos.getSlotPosX(4)
    override val progressY: Int = HTSlotPos.getSlotPosY(1)
}
