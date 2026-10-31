package hiiragi283.lib.recipe.viewer

import com.mojang.serialization.Codec
import hiiragi283.lib.recipe.base.HTProgressData
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
data class HTItemOrFluidViewerRecipe(
    override val id: Identifier?,
    val itemIngredient: Optional<SlotDisplay>,
    val fluidIngredient: Optional<SlotDisplay>,
    val itemResult: Optional<SlotDisplay>,
    val fluidResult: Optional<SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemOrFluidViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.optionalFieldOf("item_ingredient").forGetter(HTItemOrFluidViewerRecipe::itemIngredient),
                SlotDisplay.CODEC.optionalFieldOf("fluid_ingredient").forGetter(HTItemOrFluidViewerRecipe::fluidIngredient),
                SlotDisplay.CODEC.optionalFieldOf("item_result").forGetter(HTItemOrFluidViewerRecipe::itemResult),
                SlotDisplay.CODEC.optionalFieldOf("fluid_result").forGetter(HTItemOrFluidViewerRecipe::fluidResult),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::HTItemOrFluidViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        itemIngredients: Optional<SlotDisplay>,
        fluidIngredient: Optional<SlotDisplay>,
        itemResult: Optional<SlotDisplay>,
        fluidResult: Optional<SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), itemIngredients, fluidIngredient, itemResult, fluidResult, progressData)

    override fun isHandled(): Boolean = when {
        itemIngredient.isEmpty && fluidIngredient.isEmpty -> false
        itemResult.isEmpty && fluidResult.isEmpty -> false
        else -> true
    }
}
