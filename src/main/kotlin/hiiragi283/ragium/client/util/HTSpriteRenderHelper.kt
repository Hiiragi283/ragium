package hiiragi283.ragium.client.util

import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getStillTexture
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.FastColor
import org.joml.Matrix4f
import org.joml.Vector3f

object HTSpriteRenderHelper {
    @JvmStatic
    inline fun setShaderColor(guiGraphics: GuiGraphics, color: Int, action: () -> Unit) {
        val red: Float = FastColor.ARGB32.red(color) / 255f
        val green: Float = FastColor.ARGB32.green(color) / 255f
        val blue: Float = FastColor.ARGB32.blue(color) / 255f
        val alpha: Float = FastColor.ARGB32.alpha(color) / 255f
        guiGraphics.setColor(red, green, blue, alpha)
        action()
        guiGraphics.setColor(1f, 1f, 1f, 1f)
    }

    @JvmStatic
    fun getFluidSprite(stack: ImmutableFluidStack): TextureAtlasSprite? {
        val texture: ResourceLocation = stack.getStillTexture() ?: return null
        return Minecraft
            .getInstance()
            .getTextureAtlas(RagiumConst.BLOCK_ATLAS)
            .apply(texture)
    }

    //    Renderer    //

    /**
     * @see me.desht.pneumaticcraft.client.util.GuiUtils.drawFluidTexture
     */
    fun drawQuad(
        guiGraphics: GuiGraphics,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        minU: Float,
        minV: Float,
        maxU: Float,
        maxV: Float,
    ) {
        val matrix4f: Matrix4f = guiGraphics.pose().last().pose()
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
     * @see rearth.oritech.client.renderers.SmallTankRenderer.drawQuad
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
     * @see rearth.oritech.client.renderers.SmallTankRenderer.getFrameU
     */
    @JvmStatic
    private val FRAME_U: FloatArray = floatArrayOf(0f, 1f, 1f, 0f)

    /**
     * @see rearth.oritech.client.renderers.SmallTankRenderer.getFrameV
     */
    @JvmStatic
    private val FRAME_V: FloatArray = floatArrayOf(0f, 0f, 1f, 1f)

    /**
     * 上の列から左上，右上，右下，左下の順
     * @see rearth.oritech.client.renderers.SmallTankRenderer.getQuadVerticesByDirection
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
        bufferSource: MultiBufferSource,
        sprite: TextureAtlasSprite,
        color: Int,
        light: Int,
        overlay: Int,
        sides: Iterable<Direction> = Direction.entries,
    ) {
        val consumer: VertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(RagiumConst.BLOCK_ATLAS))
        val pose: PoseStack.Pose = poseStack.last()
        val matrix: Matrix4f = pose.pose()

        for (direction: Direction in sides) {
            drawQuad(direction, consumer, matrix, pose, sprite, color, light, overlay)
        }
    }
}
