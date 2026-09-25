package hiiragi283.lib.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.renderer.texture.SpriteContents
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling
import net.minecraft.core.TypedInstance
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
data object HTGuiRenderHelper {
    @JvmStatic
    fun getModel(instance: TypedInstance<Fluid>): FluidModel = Minecraft.getInstance()
        .modelManager
        .fluidStateModelSet
        .get(instance.typeHolder().value().defaultFluidState())

    @Suppress("RedundantNullableReturnType")
    @JvmStatic
    fun getSprite(instance: TypedInstance<Fluid>): TextureAtlasSprite? = getModel(instance)
        .stillMaterial()
        .sprite()

    @JvmStatic
    fun getColor(stack: FluidStack): Int = getModel(stack).fluidTintSource()?.colorAsStack(stack) ?: 0xffffffff.toInt()

    @JvmStatic
    fun renderFluid(graphics: GuiGraphicsExtractor, stack: FluidStack, x: Int, y: Int, width: Int, height: Int) {
        if (stack.isEmpty) return
        getSprite(stack)?.let { sprite: TextureAtlasSprite ->
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
                getColor(stack)
            )
            graphics.disableScissor()
        }
    }
}
