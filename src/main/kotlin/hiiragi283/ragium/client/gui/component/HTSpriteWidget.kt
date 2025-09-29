package hiiragi283.ragium.client.gui.component

import com.mojang.blaze3d.systems.RenderSystem
import hiiragi283.ragium.api.extension.drawQuad
import hiiragi283.ragium.api.extension.setShaderColor
import hiiragi283.ragium.api.gui.component.HTBackgroundRenderable
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.ClientTooltipFlag
import kotlin.math.min

@OnlyIn(Dist.CLIENT)
abstract class HTSpriteWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    message: Component,
) : HTAbstractWidget(x, y, width, height, message),
    HTBackgroundRenderable {
    protected val font: Font = Minecraft.getInstance().font

    final override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        // Render sprite
        renderSprite(guiGraphics)
        // Render tooltip
        if (getBounds().contains(mouseX, mouseY)) {
            guiGraphics.renderComponentTooltip(
                font,
                buildList { collectTooltips(this::add, getTooltipFlag()) },
                mouseX,
                mouseY,
            )
        }
    }

    /**
     * @see [de.ellpeck.actuallyadditions.mod.inventory.gui.FluidDisplay.draw]
     */
    private fun renderSprite(guiGraphics: GuiGraphics) {
        if (!shouldRender()) return
        val sprite: TextureAtlasSprite = getSprite() ?: return
        val color: Int = getColor()

        val minU: Float = sprite.u0
        val maxU: Float = sprite.u1
        val minV: Float = sprite.v0
        val maxV: Float = sprite.v1
        val delta: Float = maxV - minV
        val fillLevel: Float = getLevel() * height

        RenderSystem.setShaderTexture(0, sprite.atlasLocation())
        RenderSystem.defaultBlendFunc()
        setShaderColor(guiGraphics, color) {
            RenderSystem.enableBlend()
            for (i: Int in (0..(Mth.ceil(fillLevel) / width))) {
                val subHeight: Float = min(width.toFloat(), fillLevel - (width * i))
                val offsetY: Float = height - width * i - subHeight
                drawQuad(
                    guiGraphics,
                    x.toFloat(),
                    y + offsetY,
                    width.toFloat(),
                    subHeight,
                    minU,
                    maxV - delta * (subHeight / width),
                    maxU,
                    maxV,
                )
            }
            RenderSystem.disableBlend()
        }
    }

    protected fun getTooltipFlag(): TooltipFlag = ClientTooltipFlag.of(
        when (Minecraft.getInstance().options.advancedItemTooltips) {
            true -> TooltipFlag.ADVANCED
            false -> TooltipFlag.NORMAL
        },
    )

    protected fun getSprite(id: ResourceLocation, atlas: ResourceLocation): TextureAtlasSprite? = Minecraft
        .getInstance()
        .getTextureAtlas(atlas)
        .apply(id)

    protected abstract fun shouldRender(): Boolean

    protected abstract fun getSprite(): TextureAtlasSprite?

    protected abstract fun getColor(): Int

    protected abstract fun getLevel(): Float

    protected abstract fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag)
}
