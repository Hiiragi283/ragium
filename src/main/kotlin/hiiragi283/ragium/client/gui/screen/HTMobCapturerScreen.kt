package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.entity.device.HTMobCapturerBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTMobCapturerScreen(menu: HTBlockEntityContainerMenu<HTMobCapturerBlockEntity>, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTMobCapturerBlockEntity>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/item_collector.png")
}
