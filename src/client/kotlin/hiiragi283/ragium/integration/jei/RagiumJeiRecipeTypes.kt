package hiiragi283.ragium.integration.jei

import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.HTJeiRecipeType
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.item.RagiumItems

data object RagiumJeiRecipeTypes {
    // Mechanical
    @JvmField
    val ASSEMBLING: HTHolderJeiRecipeType<HTDoubleItemToItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.ASSEMBLING, RagiumItems.COAL_COKE)

    @JvmField
    val CRUSHING: HTHolderJeiRecipeType<HTItemToDoubleItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.CRUSHING, RagiumItems.COAL_COKE)

    // Heat
    @JvmField
    val MELTING: HTHolderJeiRecipeType<HTItemToFluidRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.MELTING, RagiumItems.COAL_COKE)

    // Chemical

    // Bio

    // Electronics

    // Arcane
}
