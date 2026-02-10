package hiiragi283.ragium.api.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.model.HTTexturedModel
import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation

/**
 * @see hiiragi283.core.api.data.model.HTTexturedModels
 */
object RagiumTexturedModels {
    //    Block    //

    @JvmStatic
    fun machine(prefix: String, tier: String, front: ResourceLocation): HTTexturedModel {
        val top: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, prefix, "top_$tier")
        val side: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, prefix, "side_$tier")
        val bottom: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, prefix, "bottom")

        return HTTexturedModel(
            ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM,
            TextureMapping()
                .put(TextureSlot.TOP, top)
                .put(TextureSlot.SIDE, side)
                .put(TextureSlot.BOTTOM, bottom)
                .put(TextureSlot.FRONT, front),
        )
    }

    //    Item    //

    @JvmField
    val MUSIC_DISC: HTTexturedModel.Provider = HTTexturedModel.create(
        ModelTemplates.MUSIC_DISC,
        HTIdLike::getId.andThen(TextureMapping::layer0),
    )
}
