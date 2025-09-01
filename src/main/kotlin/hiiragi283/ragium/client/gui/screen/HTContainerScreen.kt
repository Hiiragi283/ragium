package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.gui.component.HTBackgroundRenderable
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.inventory.container.HTContainerMenu
import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.client.gui.component.HTEnergyNetworkWidget
import hiiragi283.ragium.client.gui.component.HTFluidHandlerWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.capability.IFluidHandler

@OnlyIn(Dist.CLIENT)
abstract class HTContainerScreen<MENU : HTContainerMenu>(menu: MENU, inventory: Inventory, title: Component) :
    AbstractContainerScreen<MENU>(menu, inventory, title) {
    abstract val texture: ResourceLocation?

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
            if (renderable is HTBackgroundRenderable) {
                renderable.renderBackground(guiGraphics)
            }
        }
    }

    //    Extensions    //

    val startX: Int get() = (width - imageWidth) / 2

    val startY: Int get() = (height - imageHeight) / 2

    fun createFluidWidget(index: Int, x: Int, y: Int): HTFluidHandlerWidget {
        val handler: IFluidHandler? =
            ((menu as? HTContainerWithContextMenu<*>)?.context as? HTHandlerBlockEntity)?.getFluidHandler(null)
        return HTFluidHandlerWidget(handler, index, startX + x, startY + y).apply(::addRenderableWidget)
    }

    fun createEnergyWidget(
        key: ResourceKey<Level>,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyNetworkWidget = HTEnergyNetworkWidget(
        key,
        startX + x,
        startY + y,
    ).apply(::addRenderableWidget)
}
