package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.base.HTSerializerFactories
import net.minecraft.world.item.crafting.RecipeSerializer

data object RagiumRecipeSerializers {

    // Mechanical

    // Heat
    @JvmField
    val MELTING: RecipeSerializer<RTMeltingRecipe> = HTSerializerFactories.itemToFluid(::RTMeltingRecipe)

    @JvmField
    val SMELTING: RecipeSerializer<RTSmeltingRecipe> = HTSerializerFactories.itemToItem(::RTSmeltingRecipe)

    // Chemical

    // Bio

    // Electronics

    // Arcane
}
