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
 * @since 26.1.13
 */
@JvmRecord
data class RTFluidReactingViewerRecipe(
    override val id: Identifier?,
    val ingredients: List<SlotDisplay>,
    val catalyst: SlotDisplay,
    val itemResult: Optional<SlotDisplay>,
    val fluidResults: List<SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<RTFluidReactingViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.listOf().fieldOf(HTConstants.INGREDIENTS).forGetter(RTFluidReactingViewerRecipe::ingredients),
                SlotDisplay.CODEC.fieldOf(HTConstants.CATALYST).forGetter(RTFluidReactingViewerRecipe::catalyst),
                SlotDisplay.CODEC.optionalFieldOf("item_result").forGetter(RTFluidReactingViewerRecipe::itemResult),
                SlotDisplay.CODEC.listOf().fieldOf("fluid_results").forGetter(RTFluidReactingViewerRecipe::fluidResults),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::RTFluidReactingViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        ingredients: List<SlotDisplay>,
        catalyst: SlotDisplay,
        itemResult: Optional<SlotDisplay>,
        fluidResults: List<SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), ingredients, catalyst, itemResult, fluidResults, progressData)

    override fun isHandled(): Boolean = when {
        ingredients.isEmpty() -> false
        itemResult.isEmpty && fluidResults.isEmpty() -> false
        else -> true
    }
}
