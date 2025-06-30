package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.screen.HTDefinitionContainerScreen
import hiiragi283.ragium.common.inventory.HTFluidCollectorMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class HTFluidCollectorScreen(menu: HTFluidCollectorMenu, inventory: Inventory, title: Component) :
    HTDefinitionContainerScreen<HTFluidCollectorMenu>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/fluid_collector.png")
    override val progressPosX: Int = 0
    override val progressPosY: Int = 0

    override fun renderProgress(guiGraphics: GuiGraphics) {
    }
}
