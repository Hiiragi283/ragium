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
data class HTSingleToItemAndFluidViewerRecipe(
    override val id: Identifier?,
    val ingredient: SlotDisplay,
    val itemResult: Optional<SlotDisplay>,
    val fluidResult: Optional<SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<HTSingleToItemAndFluidViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTSingleToItemAndFluidViewerRecipe::ingredient),
                SlotDisplay.CODEC.optionalFieldOf("item_result").forGetter(HTSingleToItemAndFluidViewerRecipe::itemResult),
                SlotDisplay.CODEC.optionalFieldOf("fluid_result").forGetter(HTSingleToItemAndFluidViewerRecipe::fluidResult),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::HTSingleToItemAndFluidViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        ingredient: SlotDisplay,
        itemResult: Optional<SlotDisplay>,
        fluidResult: Optional<SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), ingredient, itemResult, fluidResult, progressData)
}
