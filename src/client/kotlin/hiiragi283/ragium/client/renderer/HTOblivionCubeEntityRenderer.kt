package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.entity.HTOblivionCoreEntity
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class HTOblivionCubeEntityRenderer(ctx: EntityRendererFactory.Context) :
    MobEntityRenderer<HTOblivionCoreEntity, HTOblivionCubeEntityRenderer.Model>(
        ctx,
        Model(ctx.getPart(ENTITY_MODEL_LAYER)),
        0.5f,
    ) {
    companion object {
        @JvmField
        val ENTITY_MODEL_LAYER = EntityModelLayer(Ragium.id("oblivion_cube"), "main")
    }

    override fun getTexture(entity: HTOblivionCoreEntity): Identifier = Ragium.id("textures/entity/oblivion_cube.png")

    //    Model    //

    class Model(modelPart: ModelPart) : EntityModel<HTOblivionCoreEntity>() {
        private val base: ModelPart = modelPart.getChild(EntityModelPartNames.CUBE)

        override fun setAngles(
            entity: HTOblivionCoreEntity,
            limbAngle: Float,
            limbDistance: Float,
            animationProgress: Float,
            headYaw: Float,
            headPitch: Float,
        ) {
        }

        override fun render(
            matrices: MatrixStack,
            vertices: VertexConsumer,
            light: Int,
            overlay: Int,
            color: Int,
        ) {
            listOf(base).forEach { modelPart: ModelPart ->
                modelPart.render(matrices, vertices, light, overlay, color)
            }
        }
    }
}
