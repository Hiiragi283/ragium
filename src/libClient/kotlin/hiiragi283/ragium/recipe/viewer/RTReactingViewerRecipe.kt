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
data class RTReactingViewerRecipe(
    override val id: Identifier?,
    val itemIngredients: List<SlotDisplay>,
    val fluidIngredients: List<SlotDisplay>,
    val catalyst: SlotDisplay,
    val itemResult: Optional<SlotDisplay>,
    val fluidResult: Optional<SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<RTReactingViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.listOf().fieldOf("item_ingredients").forGetter(RTReactingViewerRecipe::itemIngredients),
                SlotDisplay.CODEC.listOf().fieldOf("fluid_ingredients").forGetter(RTReactingViewerRecipe::fluidIngredients),
                SlotDisplay.CODEC.fieldOf(HTConstants.CATALYST).forGetter(RTReactingViewerRecipe::catalyst),
                SlotDisplay.CODEC.optionalFieldOf("item_result").forGetter(RTReactingViewerRecipe::itemResult),
                SlotDisplay.CODEC.optionalFieldOf("fluid_result").forGetter(RTReactingViewerRecipe::fluidResult),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::RTReactingViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        itemIngredients: List<SlotDisplay>,
        fluidIngredients: List<SlotDisplay>,
        catalyst: SlotDisplay,
        itemResult: Optional<SlotDisplay>,
        fluidResult: Optional<SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), itemIngredients, fluidIngredients, catalyst, itemResult, fluidResult, progressData)

    override fun isHandled(): Boolean = when {
        itemIngredients.isEmpty() && fluidIngredients.isEmpty() -> false
        else -> itemResult.isPresent || fluidResult.isPresent
    }
}
