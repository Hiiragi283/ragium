package hiiragi283.ragium.client.unused

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.unused.HTOblivionCubeEntity
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.model.*
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class HTOblivionCubeEntityRenderer(ctx: EntityRendererFactory.Context) :
    MobEntityRenderer<HTOblivionCubeEntity, HTOblivionCubeEntityRenderer.Model>(
        ctx,
        Model(ctx.getPart(ENTITY_MODEL_LAYER)),
        0.5f,
    ) {
    companion object {
        @JvmField
        val ENTITY_MODEL_LAYER = EntityModelLayer(RagiumAPI.id("oblivion_cube"), "main")
        
        fun init() {
            EntityRendererRegistry.register(null, ::HTOblivionCubeEntityRenderer)

            EntityModelLayerRegistry.registerModelLayer(ENTITY_MODEL_LAYER) {
                val modelData = ModelData()
                modelData.root.addChild(
                    EntityModelPartNames.CUBE,
                    ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F),
                    ModelTransform.pivot(0F, 0F, 0F),
                )
                TexturedModelData.of(modelData, 64, 64)
            }
        }
    }

    override fun getTexture(entity: HTOblivionCubeEntity): Identifier = RagiumAPI.id("textures/entity/oblivion_cube.png")

    //    Model    //

    class Model(modelPart: ModelPart) : EntityModel<HTOblivionCubeEntity>() {
        private val base: ModelPart = modelPart.getChild(EntityModelPartNames.CUBE)

        override fun setAngles(
            entity: HTOblivionCubeEntity,
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
