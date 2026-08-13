package hiiragi283.ragium.integration.jei

import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.HTJeiRecipeType
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.item.RagiumItems

data object RagiumJeiRecipeTypes {
    // Mechanical
    @JvmField
    val CRUSHING: HTHolderJeiRecipeType<HTItemToDoubleItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.CRUSHING, RagiumItems.COAL_COKE, 18 * 6, 18 * 1)

    // Heat
    @JvmField
    val MELTING: HTHolderJeiRecipeType<HTItemToFluidRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.MELTING, RagiumItems.COAL_COKE, 18 * 4, 18 * 1)

    // Chemical

    // Bio

    // Electronics

    // Arcane
}
