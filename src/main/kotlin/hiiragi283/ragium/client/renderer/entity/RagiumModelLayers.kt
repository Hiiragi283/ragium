package hiiragi283.ragium.client.renderer.entity

import hiiragi283.ragium.common.variant.HTDrumVariant
import net.minecraft.client.model.geom.ModelLayerLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object RagiumModelLayers {
    @JvmField
    val DRUM_MINECARTS: Map<HTDrumVariant, ModelLayerLocation> = HTDrumVariant.entries.associateWith { variant: HTDrumVariant ->
        ModelLayerLocation(variant.entityHolder.id, "main")
    }
}
