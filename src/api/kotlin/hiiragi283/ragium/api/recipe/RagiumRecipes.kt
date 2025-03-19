package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipes {
    @JvmField
    val CENTRIFUGING: HTDeferredRecipeType<SingleRecipeInput, HTCentrifugingRecipe> = HTDeferredRecipeType(
        "centrifuging",
        HTCentrifugingRecipe.CODEC,
        HTCentrifugingRecipe.STREAM_CODEC,
    )

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTCrushingRecipe> = HTDeferredRecipeType(
        "crushing",
        HTSingleItemRecipe.codec(::HTCrushingRecipe),
        HTSingleItemRecipe.streamCodec(::HTCrushingRecipe),
    )

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<SingleRecipeInput, HTExtractingRecipe> = HTDeferredRecipeType(
        "extracting",
        HTSingleItemRecipe.codec(::HTExtractingRecipe),
        HTSingleItemRecipe.streamCodec(::HTExtractingRecipe),
    )
}
