package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTItemToItemRecipe : HTProcessingRecipe<SingleRecipeInput> {
    fun getRequiredAmount(input: SingleRecipeInput): Int

    //    Serializable    //

    interface Serializable :
        HTItemToItemRecipe,
        HTSerializableRecipe<SingleRecipeInput>
}
