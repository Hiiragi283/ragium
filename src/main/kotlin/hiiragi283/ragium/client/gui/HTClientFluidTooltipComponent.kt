package hiiragi283.ragium.client.gui

import hiiragi283.ragium.api.extension.getSpriteAndColor
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.extension.toFloatColor
import hiiragi283.ragium.common.inventory.HTFluidTooltipComponent
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.MutableComponent
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent

class HTClientFluidTooltipComponent(component: HTFluidTooltipComponent) : ClientTooltipComponent {
    val content: SimpleFluidContent = component.content
    val stack: FluidStack get() = content.copy()

    override fun getHeight(): Int = 1 + 18 + 2

    override fun getWidth(font: Font): Int = 1 + 18 + 2

    /*override fun renderText(
        font: Font,
        mouseX: Int,
        mouseY: Int,
        matrix: Matrix4f,
        bufferSource: MultiBufferSource.BufferSource,
    ) {
        if (stack.isEmpty) return
        // 液体名を描画
        font.renderText(stack.hoverName, mouseX, mouseY + 18, matrix, bufferSource)
    }*/

    override fun renderImage(
        font: Font,
        x: Int,
        y: Int,
        guiGraphics: GuiGraphics,
    ) {
        if (stack.isEmpty) return
        // テクスチャを描画
        val (sprite: TextureAtlasSprite, color: Int) = stack.getSpriteAndColor()
        val floatColor: Triple<Float, Float, Float> = toFloatColor(color)
        guiGraphics.blit(
            x,
            y,
            0,
            sprite.contents().width(),
            sprite.contents().height(),
            sprite,
            floatColor.first,
            floatColor.second,
            floatColor.third,
            1.0f,
        )
        // 液体量を描画
        val amountText: MutableComponent = intText(stack.amount)
        guiGraphics.drawString(font, amountText, x + 19 - 2 - font.width(amountText), y + 6 + 3, 0xffffff, true)
    }
}
