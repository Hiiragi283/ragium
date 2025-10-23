package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.variant.HTDrumVariant
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see [net.minecraft.client.model.geom.ModelLayers]
 */
@OnlyIn(Dist.CLIENT)
object RagiumModelLayers {
    @JvmField
    val FUEL_GENERATOR: ModelLayerLocation = create("generator")

    @JvmField
    val DRUM_MINECARTS: Map<HTDrumVariant, ModelLayerLocation> = HTDrumVariant.entries.associateWith { variant: HTDrumVariant ->
        create(variant.entityHolder.id)
    }

    @JvmStatic
    private fun create(path: String): ModelLayerLocation = create(RagiumAPI.id(path))

    @JvmStatic
    private fun create(id: ResourceLocation): ModelLayerLocation = ModelLayerLocation(id, "main")
}
