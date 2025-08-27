package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.gui.screen.HTContainerScreen
import hiiragi283.ragium.common.inventory.container.HTUniversalBundleMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class HTUniversalBundleScreen(menu: HTUniversalBundleMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTUniversalBundleMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = vanillaId("textures/gui/container/shulker_box.png")
}
