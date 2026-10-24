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
data class HTDoubleToSingleViewerRecipe(
    override val id: Identifier?,
    val primary: SlotDisplay,
    val secondary: SlotDisplay,
    val result: SlotDisplay,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<HTDoubleToSingleViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.fieldOf("primary_ingredient").forGetter(HTDoubleToSingleViewerRecipe::primary),
                SlotDisplay.CODEC.fieldOf("secondary_ingredient").forGetter(HTDoubleToSingleViewerRecipe::secondary),
                SlotDisplay.CODEC.fieldOf(HTConstants.RESULT).forGetter(HTDoubleToSingleViewerRecipe::result),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::HTDoubleToSingleViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        primaryIngredient: SlotDisplay,
        secondaryIngredient: SlotDisplay,
        result: SlotDisplay,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), primaryIngredient, secondaryIngredient, result, progressData)
}
