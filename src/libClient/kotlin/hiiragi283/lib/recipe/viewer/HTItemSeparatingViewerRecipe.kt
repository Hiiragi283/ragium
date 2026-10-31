package hiiragi283.lib.recipe.viewer

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.compactNelFieldOf
import hiiragi283.lib.util.IorNel
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
    val results: IorNel<SlotDisplay, SlotDisplay>,
    override val progressData: HTProgressData,
) : HTViewerRecipe.Progressed {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemSeparatingViewerRecipe> = HTCodecs.record { instance ->
            instance.group(
                HTViewerRecipe.idCodec(),
                SlotDisplay.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTItemSeparatingViewerRecipe::ingredient),
                HTCodecs.ior(
                    SlotDisplay.CODEC.compactNelFieldOf(HTConstants.ITEM, HTConstants.ITEMS),
                    SlotDisplay.CODEC.fieldOf(HTConstants.FLUID),
                ).fieldOf(HTConstants.RESULT).forGetter(HTItemSeparatingViewerRecipe::results),
                HTViewerRecipe.Progressed.progressCodec(),
            ).apply(instance, ::HTItemSeparatingViewerRecipe)
        }
    }

    private constructor(
        id: Optional<Identifier>,
        ingredient: SlotDisplay,
        results: IorNel<SlotDisplay, SlotDisplay>,
        progressData: HTProgressData,
    ) : this(id.getOrNull(), ingredient, results, progressData)
}
