package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.gui.component.HTEnergyNetworkWidget
import hiiragi283.ragium.api.gui.component.HTProgressWidget
import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTDefinitionContainerScreen<T : HTDefinitionContainerMenu>(menu: T, inventory: Inventory, title: Component) :
    HTContainerScreen<T>(menu, inventory, title) {
    protected lateinit var energyWidget: HTEnergyNetworkWidget
        private set

    override fun init() {
        super.init()
        // Progress Widget
        addProgressBar(::addRenderableOnly)
        // Energy Widget
        energyWidget = addRenderableWidget(createEnergyWidget(menu.dimension))
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        // background
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY)
    }

    protected abstract fun addProgressBar(consumer: (HTProgressWidget) -> Unit)
}
