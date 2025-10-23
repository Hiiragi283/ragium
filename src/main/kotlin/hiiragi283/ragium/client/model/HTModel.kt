package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.function.IdToFunction
import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.client.renderer.RenderType
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see mekanism.client.model.MekanismJavaModel
 */
@OnlyIn(Dist.CLIENT)
abstract class HTModel(renderType: IdToFunction<RenderType>) : Model(renderType) {
    companion object {
        fun createLayerDefinition(width: Int, height: Int, builderAction: PartDefinition.() -> Unit): LayerDefinition =
            LayerDefinition.create(
                MeshDefinition().apply { root.builderAction() },
                width,
                height,
            )
    }
}
