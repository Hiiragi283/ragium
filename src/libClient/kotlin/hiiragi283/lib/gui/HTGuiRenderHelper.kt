package hiiragi283.lib.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.renderer.texture.SpriteContents
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.neoforged.neoforge.client.ClientTooltipFlag
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
data object HTGuiRenderHelper {
    //    Fluid    //

    /**
     * @since 26.1.8
     */
    @Suppress("UNNECESSARY_SAFE_CALL")
    @JvmStatic
    fun getSpriteAndColor(stack: FluidStack): Pair<TextureAtlasSprite, Int>? {
        val model: FluidModel = Minecraft.getInstance()
            .modelManager
            .fluidStateModelSet
            .get(stack.fluid.defaultFluidState())
        val color: Int = model.fluidTintSource()?.colorAsStack(stack) ?: 0xffffffff.toInt()
        return model.stillMaterial().sprite()?.let { it to color }
    }

    @JvmStatic
    fun renderFluid(graphics: GuiGraphicsExtractor, stack: FluidStack, x: Int, y: Int, width: Int, height: Int) {
        if (stack.isEmpty) return
        getSpriteAndColor(stack)?.let { (sprite: TextureAtlasSprite, color: Int) ->
            val spriteContents: SpriteContents = sprite.contents()
            val tileScaling = GuiSpriteScaling.Tile(spriteContents.width(), spriteContents.height())

            graphics.enableScissor(x, y, x + width, y + height)
            graphics.blitTiledSprite(
                RenderPipelines.GUI_TEXTURED,
                sprite,
                x,
                y,
                width,
                height,
                0,
                0,
                tileScaling.width,
                tileScaling.height,
                tileScaling.width,
                tileScaling.height,
                color
            )
            graphics.disableScissor()
        }
    }

    //    Tooltip    //

    /**
     * @since 26.1.8
     */
    fun getTooltipContext(): Item.TooltipContext =
        Minecraft.getInstance().let { Item.TooltipContext.of(it.level, it.player) }

    /**
     * @since 26.1.8
     */
    @JvmStatic
    fun getTooltipFlag(): TooltipFlag = ClientTooltipFlag.of(
        when (Minecraft.getInstance().options.advancedItemTooltips) {
            true -> TooltipFlag.ADVANCED
            false -> TooltipFlag.NORMAL
        }
    )
}
