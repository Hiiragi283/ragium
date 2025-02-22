package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.screen.HTMachineContainerScreen
import hiiragi283.ragium.common.inventory.HTSolidifierContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTSolidifierContainer(menu: HTSolidifierContainerMenu, inventory: Inventory, title: Component) :
    HTMachineContainerScreen<HTSolidifierContainerMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/solidifier.png")

    override val progressX: Int = HTSlotPos.getSlotPosX(4)
    override val progressY: Int = HTSlotPos.getSlotPosY(1)
}
