package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.common.entity.HTEchoBulletEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Colors
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class HTEchoBulletEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<HTEchoBulletEntity>(context) {
    companion object {
        @JvmField
        val TEXTURE: Identifier = Identifier.ofVanilla("textures/entity/enderdragon/dragon_fireball.png")

        @JvmField
        val LAYER: RenderLayer = RenderLayer.getEntityCutout(TEXTURE)
    }

    override fun render(
        entity: HTEchoBulletEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
    ) {
        matrices.push()
        matrices.scale(2.0F, 2.0F, 2.0F)
        matrices.multiply(this.dispatcher.rotation)
        val entry: MatrixStack.Entry = matrices.peek()
        val vertexConsumer: VertexConsumer = vertexConsumers.getBuffer(LAYER)
        produceVertex(vertexConsumer, entry, light, 0.0F, 0, 0, 1)
        produceVertex(vertexConsumer, entry, light, 1.0F, 0, 1, 1)
        produceVertex(vertexConsumer, entry, light, 1.0F, 1, 1, 0)
        produceVertex(vertexConsumer, entry, light, 0.0F, 1, 0, 0)
        matrices.pop()
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    private fun produceVertex(
        vertexConsumer: VertexConsumer,
        entry: MatrixStack.Entry,
        light: Int,
        x: Float,
        z: Int,
        u: Int,
        v: Int,
    ) {
        vertexConsumer
            .vertex(entry, x - 0.5F, z - 0.25F, 0.0F)
            .color(Colors.WHITE)
            .texture(u.toFloat(), v.toFloat())
            .overlay(OverlayTexture.DEFAULT_UV)
            .light(light)
            .normal(entry, 0.0F, 1.0F, 0.0F)
    }

    override fun getTexture(entity: HTEchoBulletEntity): Identifier = TEXTURE

    override fun getBlockLight(entity: HTEchoBulletEntity, pos: BlockPos): Int = 15
}
