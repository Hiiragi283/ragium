package hiiragi283.ragium.client.renderer

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see net.minecraft.client.model.geom.ModelLayers
 */
@OnlyIn(Dist.CLIENT)
object RagiumModelLayers {
    @JvmField
    val FUEL_GENERATOR: ModelLayerLocation = create("generator")

    @JvmField
    val TANK_MINECART: ModelLayerLocation = create("tank_with_minecart")

    @JvmStatic
    private fun create(path: String): ModelLayerLocation = create(RagiumAPI.id(path))

    @JvmStatic
    private fun create(id: ResourceLocation): ModelLayerLocation = ModelLayerLocation(id, "main")
}
