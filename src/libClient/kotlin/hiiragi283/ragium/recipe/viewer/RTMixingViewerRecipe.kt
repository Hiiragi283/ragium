package hiiragi283.ragium.recipe.viewer

import com.mojang.serialization.Codec
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
data class RTMixingViewerRecipe(
    override val id: Identifier?,
    val itemIngredients: List<SlotDisplay>,
    val fluidIngredient: SlotDisplay,
    val itemResult: Optional<SlotDisplay>,
    val fluidResult: Optional<SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<RTMixingViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.listOf().fieldOf("item_ingredients").forGetter(RTMixingViewerRecipe::itemIngredients),
                SlotDisplay.CODEC.fieldOf("fluid_ingredient").forGetter(RTMixingViewerRecipe::fluidIngredient),
                SlotDisplay.CODEC.optionalFieldOf("item_result").forGetter(RTMixingViewerRecipe::itemResult),
                SlotDisplay.CODEC.optionalFieldOf("fluid_result").forGetter(RTMixingViewerRecipe::fluidResult),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::RTMixingViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        itemIngredients: List<SlotDisplay>,
        fluidIngredient: SlotDisplay,
        itemResult: Optional<SlotDisplay>,
        fluidResult: Optional<SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), itemIngredients, fluidIngredient, itemResult, fluidResult, progressData)

    override fun isHandled(): Boolean = when {
        itemIngredients.isEmpty() -> false
        else -> itemResult.isPresent || fluidResult.isPresent
    }
}
