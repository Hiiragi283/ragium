package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTDefinitionContainerScreen<T : HTDefinitionContainerMenu>(menu: T, inventory: Inventory, title: Component) :
    HTContainerScreen<T>(menu, inventory, title) {
    abstract val progressBar: HTProgressBar

    override fun init() {
        super.init()
        // Energy Widget
        addRenderableWidget(createEnergyWidget(menu.dimension))
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        // background
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY)
        // progress bar
        renderProgress(guiGraphics)
    }

    protected open fun renderProgress(guiGraphics: GuiGraphics) {
        progressBar.render(guiGraphics, this, menu.progress)
    }
}
