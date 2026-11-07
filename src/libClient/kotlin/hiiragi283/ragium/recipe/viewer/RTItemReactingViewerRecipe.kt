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
data class RTItemReactingViewerRecipe(
    override val id: Identifier?,
    val itemIngredient: Optional<SlotDisplay>,
    val fluidIngredient: Optional<SlotDisplay>,
    val catalyst: SlotDisplay,
    val itemResult: Optional<SlotDisplay>,
    val fluidResult: Optional<SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<RTItemReactingViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.optionalFieldOf("item_ingredients").forGetter(RTItemReactingViewerRecipe::itemIngredient),
                SlotDisplay.CODEC.optionalFieldOf("fluid_ingredients").forGetter(RTItemReactingViewerRecipe::fluidIngredient),
                SlotDisplay.CODEC.fieldOf(HTConstants.CATALYST).forGetter(RTItemReactingViewerRecipe::catalyst),
                SlotDisplay.CODEC.optionalFieldOf("item_results").forGetter(RTItemReactingViewerRecipe::itemResult),
                SlotDisplay.CODEC.optionalFieldOf("fluid_results").forGetter(RTItemReactingViewerRecipe::fluidResult),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::RTItemReactingViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        itemIngredient: Optional<SlotDisplay>,
        fluidIngredient: Optional<SlotDisplay>,
        catalyst: SlotDisplay,
        itemResult: Optional<SlotDisplay>,
        fluidResult: Optional<SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), itemIngredient, fluidIngredient, catalyst, itemResult, fluidResult, progressData)

    override fun isHandled(): Boolean = when {
        itemIngredient.isEmpty() && fluidIngredient.isEmpty() -> false
        itemResult.isEmpty() && fluidResult.isEmpty() -> false
        else -> true
    }
}
