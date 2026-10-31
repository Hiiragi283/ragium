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
data class HTMultiToSingleViewerRecipe(
    override val id: Identifier?,
    val ingredients: List<SlotDisplay>,
    val result: SlotDisplay,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<HTMultiToSingleViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.listOf().fieldOf(HTConstants.INGREDIENTS).forGetter(HTMultiToSingleViewerRecipe::ingredients),
                SlotDisplay.CODEC.fieldOf(HTConstants.RESULT).forGetter(HTMultiToSingleViewerRecipe::result),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::HTMultiToSingleViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        ingredients: List<SlotDisplay>,
        result: SlotDisplay,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), ingredients, result, progressData)

    override fun isHandled(): Boolean = ingredients.isNotEmpty()
}
