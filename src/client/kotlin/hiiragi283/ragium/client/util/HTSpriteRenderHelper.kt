package hiiragi283.ragium.client.util

import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.resource.modifyPath
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.Direction
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.joml.Matrix4f
import org.joml.Vector3f

@OnlyIn(Dist.CLIENT)
object HTSpriteRenderHelper {
    @JvmStatic
    fun fixTextureId(id: Identifier): Identifier = id.modifyPath { path: String ->
        when {
            path.endsWith(".png") -> path
            else -> "$path.png"
        }
    }

    //    Renderer    //

    @JvmStatic
    fun blit(
        graphics: GuiGraphicsExtractor,
        texture: Identifier,
        bounds: HTBounds,
        uOffset: Float = 0f,
        vOffset: Float = 0f,
        textureWidth: Int = bounds.width,
        textureHeight: Int = bounds.height,
    ) {
        val (x: Int, y: Int, width: Int, height: Int) = bounds
        blit(
            graphics,
            texture,
            x,
            y,
            width,
            height,
            uOffset,
            vOffset,
            textureWidth,
            textureHeight,
        )
    }

    @JvmStatic
    fun blit(
        graphics: GuiGraphicsExtractor,
        texture: Identifier,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        uOffset: Int = 0,
        vOffset: Int = 0,
        textureWidth: Int = width,
        textureHeight: Int = height,
    ) {
        graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            fixTextureId(texture),
            x,
            y,
            uOffset,
            vOffset,
            width,
            height,
            textureWidth,
            textureHeight,
        )
    }

    /**
     * 参照 : [PneumaticCraft - GuiUtils.drawFluidTexture](https://github.com/TeamPneumatic/pnc-repressurized/blob/1.21/src/main/java/me/desht/pneumaticcraft/client/util/GuiUtils.java)
     */
    fun drawQuad(
        graphics: GuiGraphicsExtractor,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        minU: Float,
        minV: Float,
        maxU: Float,
        maxV: Float,
    ) {
        val matrix4f: Matrix4f = graphics.pose().last().pose()
        Tesselator
            .getInstance()
            .begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
            .apply {
                addVertex(matrix4f, x, y + height, 0f).setUv(minU, maxV)
                addVertex(matrix4f, x + width, y + height, 0f).setUv(maxU, maxV)
                addVertex(matrix4f, x + width, y, 0f).setUv(maxU, minV)
                addVertex(matrix4f, x, y, 0f).setUv(minU, minV)
            }.buildOrThrow()
            .let(BufferUploader::drawWithShader)
    }

    /**
     * 参照 : [Oritech - SmallTankRenderer.drawQuad](https://github.com/Rearth/Oritech/blob/1.21/common/src/main/java/rearth/oritech/client/renderers/SmallTankRenderer.java)
     */
    fun drawQuad(
        direction: Direction,
        consumer: VertexConsumer,
        matrix: Matrix4f,
        pose: PoseStack.Pose,
        sprite: TextureAtlasSprite,
        color: Int,
        light: Int,
        overlay: Int,
    ) {
        val normal: Vector3f = direction.step()
        val positions: Array<FloatArray> = getQuadVertices(direction)
        for (i: Int in positions.indices.reversed()) {
            val pos: FloatArray = positions[i]
            val u: Float = sprite.getU(FRAME_U[i])
            val v: Float = sprite.getV(FRAME_V[i])

            consumer
                .addVertex(matrix, pos[0], pos[1], pos[2])
                .setColor(color)
                .setUv(u, v)
                .setLight(light)
                .setOverlay(overlay)
                .setNormal(pose, normal.x, normal.y, normal.z)
        }
    }

    /**
     * 参照 : [Oritech - SmallTankRenderer.getFrameU](https://github.com/Rearth/Oritech/blob/1.21/common/src/main/java/rearth/oritech/client/renderers/SmallTankRenderer.java)
     */
    @JvmStatic
    private val FRAME_U: FloatArray = floatArrayOf(0f, 1f, 1f, 0f)

    /**
     * 参照 : [Oritech - SmallTankRenderer.getFrameV](https://github.com/Rearth/Oritech/blob/1.21/common/src/main/java/rearth/oritech/client/renderers/SmallTankRenderer.java)
     */
    @JvmStatic
    private val FRAME_V: FloatArray = floatArrayOf(0f, 0f, 1f, 1f)

    /**
     * 上の列から左上，右上，右下，左下の順
     *
     * 参照 : [Oritech - SmallTankRenderer.getQuadVerticesByDirection](https://github.com/Rearth/Oritech/blob/1.21/common/src/main/java/rearth/oritech/client/renderers/SmallTankRenderer.java)
     */
    @JvmStatic
    private fun getQuadVertices(direction: Direction): Array<FloatArray> = when (direction) {
        Direction.DOWN -> arrayOf(
            floatArrayOf(0f, 0f, 1f),
            floatArrayOf(1f, 0f, 1f),
            floatArrayOf(1f, 0f, 0f),
            floatArrayOf(0f, 0f, 0f),
        )

        Direction.UP -> arrayOf(
            floatArrayOf(0f, 1f, 0f),
            floatArrayOf(1f, 1f, 0f),
            floatArrayOf(1f, 1f, 1f),
            floatArrayOf(0f, 1f, 1f),
        )

        Direction.NORTH -> arrayOf(
            floatArrayOf(1f, 1f, 0f),
            floatArrayOf(0f, 1f, 0f),
            floatArrayOf(0f, 0f, 0f),
            floatArrayOf(1f, 0f, 0f),
        )

        Direction.SOUTH -> arrayOf(
            floatArrayOf(0f, 1f, 1f),
            floatArrayOf(1f, 1f, 1f),
            floatArrayOf(1f, 0f, 1f),
            floatArrayOf(0f, 0f, 1f),
        )

        Direction.WEST -> arrayOf(
            floatArrayOf(0f, 1f, 0f),
            floatArrayOf(0f, 1f, 1f),
            floatArrayOf(0f, 0f, 1f),
            floatArrayOf(0f, 0f, 0f),
        )

        Direction.EAST -> arrayOf(
            floatArrayOf(1f, 1f, 1f),
            floatArrayOf(1f, 1f, 0f),
            floatArrayOf(1f, 0f, 0f),
            floatArrayOf(1f, 0f, 1f),
        )
    }

    @JvmStatic
    fun drawFluidBox(
        poseStack: PoseStack,
        consumer: VertexConsumer,
        sprite: TextureAtlasSprite,
        color: Int,
        light: Int,
        overlay: Int,
        sides: Iterable<Direction> = Direction.entries,
    ) {
        val pose: PoseStack.Pose = poseStack.last()
        val matrix: Matrix4f = pose.pose()

        for (direction: Direction in sides) {
            drawQuad(direction, consumer, matrix, pose, sprite, color, light, overlay)
        }
    }
}
