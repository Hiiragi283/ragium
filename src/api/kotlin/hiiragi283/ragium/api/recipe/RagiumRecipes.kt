package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.registry.HTDeferredRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipes {
    @JvmField
    val CRUSHING: HTDeferredRecipe<SingleRecipeInput, HTCrushingRecipe> = HTDeferredRecipe(
        "crushing",
        HTCrushingRecipe.CODEC,
        HTCrushingRecipe.STREAM_CODEC,
    )
}
