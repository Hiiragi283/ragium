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
data class HTItemSeparatingViewerRecipe(
    override val id: Identifier?,
    val ingredient: SlotDisplay,
    val itemResults: List<SlotDisplay>,
    val fluidResult: SlotDisplay,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemSeparatingViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTItemSeparatingViewerRecipe::ingredient),
                SlotDisplay.CODEC.listOf().fieldOf("item_results").forGetter(HTItemSeparatingViewerRecipe::itemResults),
                SlotDisplay.CODEC.fieldOf("fluid_result").forGetter(HTItemSeparatingViewerRecipe::fluidResult),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::HTItemSeparatingViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        ingredient: SlotDisplay,
        itemResults: List<SlotDisplay>,
        fluidResult: SlotDisplay,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), ingredient, itemResults, fluidResult, progressData)

    override fun isHandled(): Boolean = itemResults.isNotEmpty()
}
