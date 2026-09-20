package hiiragi283.lib.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.FaceInfo
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.Direction
import org.joml.Vector3f

/**
 * Hiiragi Seriesで使用される描画を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
data object HTRenderHelper {
    /**
     * キューブを描画します。
     * @see net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer.submitCube
     */
    @JvmStatic
    fun submitCube(
        submitNodeCollector: SubmitNodeCollector,
        poseStack: PoseStack,
        renderType: RenderType,
        from: Vector3f,
        to: Vector3f,
        sprite: TextureAtlasSprite,
        color: Int,
        light: Int
    ) {
        submitNodeCollector.submitCustomGeometry(poseStack, renderType) {
                pose: PoseStack.Pose,
                consumer: VertexConsumer
            ->
            for (direction: Direction in Direction.entries) {
                val faceInfo: FaceInfo = FaceInfo.fromFacing(direction)
                val positions: List<Vector3f> = listOf(
                    faceInfo.getVertexInfo(0).select(from, to),
                    faceInfo.getVertexInfo(1).select(from, to),
                    faceInfo.getVertexInfo(2).select(from, to),
                    faceInfo.getVertexInfo(3).select(from, to)
                )
                for (i: Int in positions.indices) {
                    val position: Vector3f = positions[i]
                    val (u: Float, v: Float) = when (i) {
                        1 -> sprite.u1 to sprite.v0
                        2 -> sprite.u1 to sprite.v1
                        3 -> sprite.u0 to sprite.v1
                        else -> sprite.u0 to sprite.v0
                    }
                    consumer
                        .addVertex(pose, position)
                        .setUv(u, v)
                        .setColor(color)
                        .setLight(light)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setNormal(pose, position)
                }
            }
        }
    }
}
