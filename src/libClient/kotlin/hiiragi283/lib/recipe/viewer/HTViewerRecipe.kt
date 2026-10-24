package hiiragi283.lib.recipe.viewer

import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import net.minecraft.resources.Identifier
import java.util.Optional

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTViewerRecipe {
    companion object {
        @JvmStatic
        fun <T : HTViewerRecipe> idCodec(): RecordCodecBuilder<T, Optional<Identifier>> =
            Identifier.CODEC.optionalFieldOf(HTConstants.ID).forGetter { Optional.ofNullable(it.id) }
    }

    val id: Identifier?

    fun isHandled(): Boolean = true

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.12
     */
    interface Progressed :
        HTViewerRecipe,
        HTProgressRecipe.Holder {
        companion object {
            @JvmStatic
            fun <T : HTProgressRecipe.Holder> progressCodec(): RecordCodecBuilder<T, HTProgressData> =
                HTProgressRecipe.Holder.progressCodec()
        }
    }
}
