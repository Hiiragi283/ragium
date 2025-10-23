package hiiragi283.ragium.client.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.renderer.RagiumModelLayers
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.renderer.RenderType
import net.minecraft.util.Mth
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.math.min

@OnlyIn(Dist.CLIENT)
class HTFuelGeneratorModel(modelSet: EntityModelSet) : HTModel(RenderType::entityCutout) {
    companion object {
        @JvmField
        val TOP = HTModelPartBuilder("top") {
            CubeListBuilder
                .create()
                .texOffs(0, 0)
                .addBox(-8f, 0f, -8f, 16f, 4f, 16f)
        }

        @JvmField
        val BOTTOM = HTModelPartBuilder("bottom") {
            CubeListBuilder
                .create()
                .texOffs(0, 20)
                .addBox(-8f, 4f, -8f, 16f, 4f, 16f)
        }

        @JvmField
        val CORE = HTModelPartBuilder("core") {
            CubeListBuilder
                .create()
                .texOffs(0, 40)
                .addBox(-4f, -8f, -4f, 8f, 8f, 8f)
        }

        @JvmField
        val BELLOW = HTModelPartBuilder("bellow") {
            CubeListBuilder
                .create()
                .texOffs(24, 46)
                .addBox(-5f, 0f, -5f, 10f, 8f, 10f)
        }

        @JvmStatic
        fun createLayer(): LayerDefinition = createLayerDefinition(64, 64) {
            TOP.addToDefinition(this)
            BOTTOM.addToDefinition(this)
            CORE.addToDefinition(this)
            BELLOW.addToDefinition(this)
        }
    }

    private val top: ModelPart
    private val bottom: ModelPart
    private val core: ModelPart
    private val bellow: ModelPart

    init {
        val root: ModelPart = modelSet.bakeLayer(RagiumModelLayers.FUEL_GENERATOR)
        top = TOP.getChild(root)
        bottom = BOTTOM.getChild(root)
        core = CORE.getChild(root)
        bellow = BELLOW.getChild(root)
    }

    fun renderType(variant: HTGeneratorVariant<*, *>): RenderType? = when (variant) {
        HTGeneratorVariant.Solar -> null
        else -> RagiumAPI.id("textures/entity/${variant.variantName()}.png")
    }?.let(this::renderType)

    override fun renderToBuffer(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        color: Int,
    ) {
        top.render(poseStack, buffer, packedLight, packedOverlay, color)
        bottom.render(poseStack, buffer, packedLight, packedOverlay, color)
        core.render(poseStack, buffer, packedLight, packedOverlay, color)
        bellow.render(poseStack, buffer, packedLight, packedOverlay, color)
    }

    fun render(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        time: Float,
        tier: HTComponentTier?,
    ) {
        val speed: Float = when (tier) {
            HTComponentTier.BASIC -> 0.3f
            HTComponentTier.ADVANCED -> 0.4f
            HTComponentTier.ELITE -> 0.6f
            HTComponentTier.ULTIMATE -> 0.8f
            HTComponentTier.ETERNAL -> 1f
            null -> 0.2f
        }
        top.y = Mth.sin(time * speed + Mth.HALF_PI) * 4 - 4f
        bellow.y = min(Mth.sin(time * speed + Mth.HALF_PI) * 4, 0f)

        top.render(poseStack, buffer, packedLight, packedOverlay)
        bottom.render(poseStack, buffer, packedLight, packedOverlay)
        core.render(poseStack, buffer, packedLight, packedOverlay)
        bellow.render(poseStack, buffer, packedLight, packedOverlay)
    }
}
