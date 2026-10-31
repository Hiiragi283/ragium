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
data class HTFluidSeparatingViewerRecipe(
    override val id: Identifier?,
    val ingredient: SlotDisplay,
    val itemResult: SlotDisplay,
    val fluidResults: List<SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidSeparatingViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTFluidSeparatingViewerRecipe::ingredient),
                SlotDisplay.CODEC.fieldOf("item_result").forGetter(HTFluidSeparatingViewerRecipe::itemResult),
                SlotDisplay.CODEC.listOf().fieldOf("fluid_results").forGetter(HTFluidSeparatingViewerRecipe::fluidResults),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::HTFluidSeparatingViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        ingredient: SlotDisplay,
        itemResult: SlotDisplay,
        fluidResults: List<SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), ingredient, itemResult, fluidResults, progressData)

    override fun isHandled(): Boolean = fluidResults.isNotEmpty()
}
