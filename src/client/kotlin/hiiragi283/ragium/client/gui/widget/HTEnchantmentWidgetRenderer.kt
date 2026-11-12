package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.HTSpriteRenderHelper
import hiiragi283.lib.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.widget.HTEnchantmentWidget
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class HTEnchantmentWidgetRenderer(gui: HTGuiAccess, widget: HTEnchantmentWidget) : HTAbstractWidgetRenderer<HTEnchantmentWidget>(gui, widget) {
    companion object {
        @JvmField
        val BACKGROUND: Identifier = RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, "enchantment_slot_background.png")

        @JvmField
        val ENCHANTED_BOOK = ItemStack(Items.ENCHANTED_BOOK)
    }

    override fun render(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Render background
        HTSpriteRenderHelper.blit(graphics, BACKGROUND, bounds)
        // Render front
        val power: Float = widget.power()
        if (power > 0f) {
            val (x: Int, y: Int) = bounds
            graphics.item(ENCHANTED_BOOK, x + 1, y + 1)
        }
        // Render tooltip
        renderTooltips(bounds, graphics, mouseX, mouseY) { _, _ ->
            buildList { this += HTCommonTranslation.ENCHANTMENT_POWER.translate(ChatFormatting.AQUA, power) }
        }
    }
}
