package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.gui.component.HTBackgroundRenderable
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.common.inventory.container.HTContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTContainerScreen<MENU : HTContainerMenu>(
    val texture: ResourceLocation?,
    menu: MENU,
    inventory: Inventory,
    title: Component,
) : AbstractContainerScreen<MENU>(menu, inventory, title) {
    override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        texture?.let {
            guiGraphics.blit(it, startX, startY, 0, 0, imageWidth, imageHeight)
        }

        for (renderable: Renderable in renderables) {
            HTBackgroundRenderable.tryRender(renderable, guiGraphics)
        }
    }

    open fun getFluidWidgets(): List<HTFluidWidget> = listOf()

    //    Extensions    //

    val startX: Int get() = (width - imageWidth) / 2

    val startY: Int get() = (height - imageHeight) / 2
}
