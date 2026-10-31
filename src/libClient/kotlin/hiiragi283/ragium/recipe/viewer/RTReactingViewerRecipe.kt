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
    val itemResults: List<SlotDisplay>,
    val fluidResults: List<SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<RTReactingViewerRecipe> = HTCodecs.record { instance ->
            val listCodec: Codec<List<SlotDisplay>> = SlotDisplay.CODEC.listOf()
            instance.group(
                HTViewerRecipe.idCodec(),
                listCodec.fieldOf("item_ingredients").forGetter(RTReactingViewerRecipe::itemIngredients),
                listCodec.fieldOf("fluid_ingredients").forGetter(RTReactingViewerRecipe::fluidIngredients),
                SlotDisplay.CODEC.fieldOf(HTConstants.CATALYST).forGetter(RTReactingViewerRecipe::catalyst),
                listCodec.fieldOf("item_results").forGetter(RTReactingViewerRecipe::itemResults),
                listCodec.fieldOf("fluid_results").forGetter(RTReactingViewerRecipe::fluidResults),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::RTReactingViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        itemIngredients: List<SlotDisplay>,
        fluidIngredients: List<SlotDisplay>,
        catalyst: SlotDisplay,
        itemResults: List<SlotDisplay>,
        fluidResults: List<SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), itemIngredients, fluidIngredients, catalyst, itemResults, fluidResults, progressData)

    override fun isHandled(): Boolean = when {
        itemIngredients.isEmpty() && fluidIngredients.isEmpty() -> false
        itemResults.isEmpty() && fluidResults.isEmpty() -> false
        else -> true
    }
}
