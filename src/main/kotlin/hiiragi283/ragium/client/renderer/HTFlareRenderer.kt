package hiiragi283.ragium.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.entity.HTFlare
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation

class HTFlareRenderer(context: EntityRendererProvider.Context) : EntityRenderer<HTFlare>(context) {
    companion object {
        @JvmField
        val TEXTURE_ID: ResourceLocation = RagiumAPI.id("textures/entity/flare.png")

        @JvmField
        val RENDER_TYPE: RenderType = RenderType.entityCutoutNoCull(TEXTURE_ID)

        @JvmStatic
        private fun vertex(
            consumer: VertexConsumer,
            pose: PoseStack.Pose,
            packedLight: Int,
            x: Float,
            y: Int,
            u: Int,
            v: Int,
        ) {
            consumer
                .addVertex(pose, x - 0.5f, y.toFloat() - 0.25f, 0.0f)
                .setColor(-1)
                .setUv(u.toFloat(), v.toFloat())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)
        }
    }

    override fun getBlockLightLevel(entity: HTFlare, pos: BlockPos): Int = 15

    override fun render(
        entity: HTFlare,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
    ) {
        poseStack.pushPose()
        poseStack.scale(2f, 2f, 2f)
        poseStack.mulPose(entityRenderDispatcher.cameraOrientation())
        val poseIn: PoseStack.Pose = poseStack.last()
        val consumer: VertexConsumer = bufferSource.getBuffer(RENDER_TYPE)
        vertex(consumer, poseIn, packedLight, 0f, 0, 0, 1)
        vertex(consumer, poseIn, packedLight, 1f, 0, 1, 1)
        vertex(consumer, poseIn, packedLight, 1f, 1, 1, 0)
        vertex(consumer, poseIn, packedLight, 0f, 1, 0, 0)
        poseStack.popPose()
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
    }

    override fun getTextureLocation(entity: HTFlare): ResourceLocation = TEXTURE_ID
}
