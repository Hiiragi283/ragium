package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.screen.HTMachineScreen
import hiiragi283.ragium.common.inventory.HTSolidifierMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTSolidifierScreen(menu: HTSolidifierMenu, inventory: Inventory, title: Component) :
    HTMachineScreen<HTSolidifierMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/solidifier.png")

    override val progressPosX: Int = HTSlotPos.getSlotPosX(3)
    override val progressPosY: Int = HTSlotPos.getSlotPosY(1)

    override val progressSizeX: Int = 52
    override val progressTex: ResourceLocation = RagiumAPI.id("progress_catalyst")
}
