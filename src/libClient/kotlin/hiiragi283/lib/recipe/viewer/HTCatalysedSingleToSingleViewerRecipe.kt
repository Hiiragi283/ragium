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
data class HTCatalysedSingleToSingleViewerRecipe(
    override val id: Identifier?,
    val ingredient: SlotDisplay,
    val catalyst: SlotDisplay,
    val result: SlotDisplay,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<HTCatalysedSingleToSingleViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTCatalysedSingleToSingleViewerRecipe::ingredient),
                SlotDisplay.CODEC.fieldOf(HTConstants.CATALYST).forGetter(HTCatalysedSingleToSingleViewerRecipe::catalyst),
                SlotDisplay.CODEC.fieldOf(HTConstants.RESULT).forGetter(HTCatalysedSingleToSingleViewerRecipe::result),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::HTCatalysedSingleToSingleViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        ingredient: SlotDisplay,
        catalyst: SlotDisplay,
        result: SlotDisplay,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), ingredient, catalyst, result, progressData)
}
