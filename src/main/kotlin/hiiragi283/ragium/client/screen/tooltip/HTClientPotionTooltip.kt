package hiiragi283.ragium.client.screen.tooltip

import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.util.HTPotionBundle
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see [ClientBundleTooltip]
 */
@OnlyIn(Dist.CLIENT)
class HTClientPotionTooltip(val bundle: HTPotionBundle) : ClientTooltipComponent {
    companion object {
        @JvmStatic
        private val BACKGROUND: ResourceLocation = vanillaId("container/bundle/background")

        @JvmStatic
        private val SLOT: ResourceLocation = vanillaId("container/bundle/slot")
    }

    private fun backgroundWidth(): Int = this.bundle.size * 18 + 2

    private fun backgroundHeight(): Int = 1 * 20 + 2

    override fun getHeight(): Int = backgroundHeight() + 4

    override fun getWidth(font: Font): Int = backgroundWidth()

    override fun renderImage(
        font: Font,
        x: Int,
        y: Int,
        guiGraphics: GuiGraphics,
    ) {
        val sizeX: Int = this.bundle.size
        guiGraphics.blitSprite(BACKGROUND, x, y, backgroundWidth(), backgroundHeight())
        var index = 0
        for (x1: Int in (0 until sizeX)) {
            renderSlot(
                font,
                x + x1 * 18 + 1,
                y + 1,
                guiGraphics,
                index++,
            )
        }
    }

    private fun renderSlot(
        font: Font,
        x: Int,
        y: Int,
        guiGraphics: GuiGraphics,
        index: Int,
    ) {
        val stack: ItemStack = bundle.getItemUnsafe(index)
        guiGraphics.blitSprite(SLOT, x, y, 0, 18, 20)
        guiGraphics.renderItem(stack, x + 1, y + 1, index)
        guiGraphics.renderItemDecorations(font, stack, x + 1, y + 1)
    }
}
