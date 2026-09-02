package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.HTStorageHelper
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.data.AtlasIds
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import java.util.function.Consumer

class HTEnergySlotWidgetRenderer(gui: HTGuiAccess, widget: HTEnergySlotWidget) :
    HTSpriteWidgetRenderer<HTEnergySlotWidget>(gui, widget) {
    companion object {
        @JvmField
        val SPRITE: Identifier = RagiumAPI.id(HTConstants.GUI, "energy_slot")

        @JvmField
        val BACKGROUND: Identifier = RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, "energy_slot_background.png")
    }

    override fun renderBackground(bounds: HTBounds, graphics: GuiGraphicsExtractor) {
        HTSpriteRenderHelper.blit(graphics, BACKGROUND, bounds)
    }

    override fun shouldRender(): Boolean = !widget.isEmpty

    override fun getSprite(): TextureAtlasSprite = getSprite(AtlasIds.BLOCKS, SPRITE)

    override fun getColor(): Int = -1

    override fun getLevel(): Float = widget.filledLevel

    override fun collectTooltips(consumer: Consumer<Text>, context: Item.TooltipContext, flag: TooltipFlag) {
        HTStorageHelper.addEnergyTooltip(widget, consumer, flag.isCreative)
    }
}
