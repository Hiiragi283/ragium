package hiiragi283.ragium.client.model

import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import java.util.function.Function

/**
 * @see mekanism.client.model.MekanismJavaModel
 */
@OnlyIn(Dist.CLIENT)
abstract class HTModel(renderType: Function<ResourceLocation, RenderType>) : Model(renderType) {
    companion object {
        fun createLayerDefinition(width: Int, height: Int, builderAction: PartDefinition.() -> Unit): LayerDefinition =
            LayerDefinition.create(
                MeshDefinition().apply { root.builderAction() },
                width,
                height,
            )
    }
}
