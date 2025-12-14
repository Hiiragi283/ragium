package hiiragi283.ragium.client.gui.component.base

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.stack.getStillTexture
import hiiragi283.ragium.api.stack.getTintColor
import hiiragi283.ragium.api.text.HTTextUtil
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

@OnlyIn(Dist.CLIENT)
abstract class HTBasicFluidWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(x, y, width, height, Component.empty()),
    HTFluidWidget {
    final override fun shouldRender(): Boolean = getStack() != null

    final override fun getSprite(): TextureAtlasSprite? = getSprite(getStack()?.getStillTexture(), RagiumConst.BLOCK_ATLAS)

    final override fun getColor(): Int = getStack()?.getTintColor() ?: -1

    override fun getLevel(): Fraction = getStoredLevel()

    override fun collectTooltips(consumer: Consumer<Component>, flag: TooltipFlag) {
        HTTextUtil.addFluidTooltip(getStack(), consumer, flag, false)
    }
}
