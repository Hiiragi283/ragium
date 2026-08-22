package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.text.Text
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.gui.widget.HTFluidWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.resources.Identifier
import net.minecraft.util.FormattedCharSequence
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag

class HTFluidWidgetRenderer(gui: HTGuiAccess, widget: HTFluidWidget) : HTSpriteWidgetRenderer<HTFluidWidget>(gui, widget) {
    override fun renderBackground(bounds: HTBounds, graphics: GuiGraphicsExtractor) {
        val texture: Identifier = when (widget) {
            is HTFluidWidget.Slot -> widget.backgroundType.slotTexture
            is HTFluidWidget.Tank -> widget.backgroundType.tankTexture
        }
        HTSpriteRenderHelper.blit(graphics, texture, bounds)
    }

    override fun shouldRender(): Boolean = !widget.isEmpty

    private fun getModel(): FluidModel = Minecraft.getInstance()
        .modelManager
        .fluidStateModelSet
        .get(widget.resource.fluid.defaultFluidState())

    override fun getSprite(): TextureAtlasSprite = getModel().stillMaterial().sprite()

    override fun getColor(): Int = getModel().fluidTintSource()?.colorAsStack(widget.getFluidStack()) ?: 0

    override fun getLevel(): Float = when (widget) {
        is HTFluidWidget.Slot -> 1f
        is HTFluidWidget.Tank -> widget.currentFilledLevel
    }.coerceAtMost(1f)

    override fun collectTooltips(flag: TooltipFlag): List<FormattedCharSequence> = when {
        widget.isEmpty -> listOf()
        else -> widget.getFluidStack()
            .getTooltipLines(Item.TooltipContext.of(null), null, flag)
            .map(Text::getVisualOrderText)
    }
}
