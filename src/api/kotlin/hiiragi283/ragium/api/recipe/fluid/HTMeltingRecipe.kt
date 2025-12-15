package hiiragi283.ragium.api.recipe.fluid

import hiiragi283.ragium.api.recipe.HTSingleItemInputRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeType

interface HTMeltingRecipe :
    HTFluidRecipe,
    HTSingleItemInputRecipe {
    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = null

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MELTING.get()
}
