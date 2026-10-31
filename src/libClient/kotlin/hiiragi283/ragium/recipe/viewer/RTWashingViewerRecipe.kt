package hiiragi283.ragium.recipe.viewer

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.viewer.HTViewerRecipe
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
data class RTWashingViewerRecipe(
    override val id: Identifier?,
    val primary: SlotDisplay,
    val secondary: SlotDisplay,
    val results: List<SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<RTWashingViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.fieldOf("primary_ingredient").forGetter(RTWashingViewerRecipe::primary),
                SlotDisplay.CODEC.fieldOf("secondary_ingredient").forGetter(RTWashingViewerRecipe::secondary),
                SlotDisplay.CODEC.listOf().fieldOf(HTConstants.RESULTS).forGetter(RTWashingViewerRecipe::results),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::RTWashingViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        primaryIngredient: SlotDisplay,
        secondaryIngredient: SlotDisplay,
        results: List<SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), primaryIngredient, secondaryIngredient, results, progressData)

    override fun isHandled(): Boolean = results.isNotEmpty()
}
