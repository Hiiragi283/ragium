package hiiragi283.lib.recipe.viewer

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.display.SlotDisplay
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
@JvmRecord
data class HTSingleToSingleViewerRecipe(
    override val id: Identifier?,
    val ingredient: SlotDisplay,
    val result: SlotDisplay,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<HTSingleToSingleViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTSingleToSingleViewerRecipe::ingredient),
                SlotDisplay.CODEC.fieldOf(HTConstants.RESULT).forGetter(HTSingleToSingleViewerRecipe::result),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::HTSingleToSingleViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        ingredient: SlotDisplay,
        result: SlotDisplay,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), ingredient, result, progressData)
}
