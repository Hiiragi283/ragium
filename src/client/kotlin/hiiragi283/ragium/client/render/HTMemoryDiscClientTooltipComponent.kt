package hiiragi283.ragium.client.render

import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.item.tooltip.HTMemoryDiscTooltipComponent
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.world.item.ItemStack

@ConsistentCopyVisibility
@JvmRecord
data class HTMemoryDiscClientTooltipComponent private constructor(val data: ItemStack, val text: Text = RagiumTranslation.TOOLTIPS_MEMORY_DISC_DATA.translateColored(HTDefaultColor.YELLOW, data)) : ClientTooltipComponent {
    constructor(tooltip: HTMemoryDiscTooltipComponent) : this(tooltip.data.create())

    override fun getHeight(font: Font): Int = 17 + font.lineHeight

    override fun getWidth(font: Font): Int = font.width(text)

    override fun extractText(graphics: GuiGraphicsExtractor, font: Font, x: Int, y: Int) {
        graphics.text(font, text, x, y, 0xffffff, false)
    }

    override fun extractImage(font: Font, x: Int, y: Int, w: Int, h: Int, graphics: GuiGraphicsExtractor) {
        graphics.item(data, x, y + font.lineHeight)
    }

    override fun equals(other: Any?): Boolean = ItemStack.isSameItemSameComponents((other as? HTMemoryDiscClientTooltipComponent)?.data ?: ItemStack.EMPTY, this.data)

    override fun hashCode(): Int = ItemStack.hashItemAndComponents(this.data)
}
