package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.screen.HTMachineScreen
import hiiragi283.ragium.common.inventory.HTAssemblerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTAssemblerScreen(menu: HTAssemblerMenu, inventory: Inventory, title: Component) :
    HTMachineScreen<HTAssemblerMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/assembler.png")

    override val progressPosX: Int = HTSlotPos.getSlotPosX(7)
    override val progressPosY: Int = HTSlotPos.getSlotPosY(1)

    override val progressTex: ResourceLocation = RagiumAPI.id("progress_assembler")
}
