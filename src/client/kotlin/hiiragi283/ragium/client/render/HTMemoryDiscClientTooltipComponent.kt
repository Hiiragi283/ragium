package hiiragi283.ragium.client.render

import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.text.Text
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.item.tooltip.HTMemoryDiscTooltipComponent
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.item.ItemStack
import org.joml.Matrix4f

@ConsistentCopyVisibility
@JvmRecord
data class HTMemoryDiscClientTooltipComponent private constructor(val data: ItemStack, val text: Text = RagiumTranslation.TOOLTIPS_MEMORY_DISC_DATA.translateColored(HTDefaultColor.YELLOW, data)) : ClientTooltipComponent {
    constructor(tooltip: HTMemoryDiscTooltipComponent) : this(tooltip.data.toStack())

    override fun getHeight(): Int = 17 + 9

    override fun getWidth(font: Font): Int = font.width(text)

    override fun renderText(font: Font, mouseX: Int, mouseY: Int, matrix: Matrix4f, bufferSource: MultiBufferSource.BufferSource) {
        font.drawInBatch(
            text,
            mouseX.toFloat(),
            mouseY.toFloat(),
            0x0,
            false,
            matrix,
            bufferSource,
            Font.DisplayMode.NORMAL,
            0,
            LightTexture.FULL_BRIGHT,
        )
    }

    override fun renderImage(font: Font, x: Int, y: Int, guiGraphics: GuiGraphics) {
        guiGraphics.renderItem(data, x, y + font.lineHeight)
    }

    override fun equals(other: Any?): Boolean = ItemStack.isSameItemSameComponents((other as? HTMemoryDiscClientTooltipComponent)?.data ?: ItemStack.EMPTY, this.data)

    override fun hashCode(): Int = ItemStack.hashItemAndComponents(this.data)
}
