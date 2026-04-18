package hiiragi283.ragium.client.gui.widget

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.text.Text
import hiiragi283.core.client.gui.widget.HTSpriteWidgetRenderer
import hiiragi283.core.util.HTSpriteRenderHelper
import hiiragi283.core.util.HTStorageHelper
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

@OnlyIn(Dist.CLIENT)
class HTEnergySlotWidgetRenderer(gui: HTAbstractGui, widget: HTEnergySlotWidget) :
    HTSpriteWidgetRenderer<HTEnergySlotWidget>(gui, widget) {
    companion object {
        @JvmField
        val SPRITE: ResourceLocation = RagiumAPI.id(HTConst.GUI, "energy_slot")

        @JvmField
        val BACKGROUND: ResourceLocation = RagiumAPI.id(HTConst.TEXTURES, HTConst.GUI, "energy_slot_background.png")
    }

    override fun renderBackground(bounds: HTBounds, guiGraphics: GuiGraphics) {
        HTSpriteRenderHelper.blit(guiGraphics, BACKGROUND, bounds)
    }

    override fun shouldRender(): Boolean = !widget.isEmpty()

    override fun getSprite(): TextureAtlasSprite? = getSprite(SPRITE)

    override fun getColor(): Int = -1

    override fun getLevel(): Fraction = widget.getLevelAsFraction()

    override fun collectTooltips(consumer: Consumer<Text>, flag: TooltipFlag) {
        HTStorageHelper.addEnergyTooltip(widget, consumer, false)
    }
}
