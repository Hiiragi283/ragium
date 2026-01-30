package hiiragi283.ragium.client.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.client.gui.widget.HTSpriteWidgetRenderer
import hiiragi283.core.util.HTTooltipHelper
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.widget.HTEnergyBarWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

@OnlyIn(Dist.CLIENT)
class HTEnergyBarWidgetRenderer(widget: HTEnergyBarWidget) : HTSpriteWidgetRenderer<HTEnergyBarWidget>(widget) {
    override fun renderBackground(bounds: HTBounds, guiGraphics: GuiGraphics) {
    }

    override fun shouldRender(): Boolean = widget.getAmount() > 0

    override fun getSprite(): TextureAtlasSprite? = getSprite(RagiumAPI.id("container", "energy_bar"))

    override fun getColor(): Int = -1

    override fun getLevel(): Fraction = widget.getLevelAsFraction()

    override fun collectTooltips(consumer: Consumer<Component>, flag: TooltipFlag) {
        HTTooltipHelper.addEnergyTooltip(widget.getAmount(), consumer, false)
    }
}
