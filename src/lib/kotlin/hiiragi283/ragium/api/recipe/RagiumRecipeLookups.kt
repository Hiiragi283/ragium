package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.lookup.HTCompoundRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.lib.recipe.lookup.fromRecipeType
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

data object RagiumRecipeLookups {
    @JvmStatic
    private fun <RECIPE : Any> create(path: String): HTCompoundRecipeLookup<RECIPE> = HTCompoundRecipeLookup.create(RagiumAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(recipeType: HTRecipeType<RECIPE>): HTRecipeLookup<RECIPE> = HTVanillaRecipeLookup(recipeType)

    // Mechanical
    @JvmField
    val CRUSHING: HTCompoundRecipeLookup<HTItemToDoubleItemRecipe> = create(RagiumConstants.CRUSHING)

    // Heat
    @JvmField
    val MELTING: HTCompoundRecipeLookup<HTItemToFluidRecipe> = create(RagiumConstants.MELTING)

    // Chemical

    // Bio

    // Electronics

    // Arcane

    @JvmStatic
    fun init() {
        CRUSHING.fromRecipeType(RagiumRecipeTypes.CRUSHING, identity())

        MELTING.fromRecipeType(RagiumRecipeTypes.MELTING, identity())
    }
}
