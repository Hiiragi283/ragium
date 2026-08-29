package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToItemAndFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.lookup.HTCompoundRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

/**
 * Ragiumで使用される[HTRecipeLookup]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumRecipeLookups {
    @JvmStatic
    private fun <RECIPE : Any> create(path: String): HTCompoundRecipeLookup<RECIPE> = HTCompoundRecipeLookup.create(RagiumAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(recipeType: HTRecipeType<RECIPE>): HTRecipeLookup<RECIPE> = HTVanillaRecipeLookup(recipeType)

    // Mechanical
    @JvmField
    val ASSEMBLING: HTCompoundRecipeLookup<HTDoubleItemToItemRecipe> = create(RagiumConstants.ASSEMBLING)

    @JvmField
    val COMPRESSING: HTCompoundRecipeLookup<HTItemToItemRecipe> = create(RagiumConstants.COMPRESSING)

    @JvmField
    val CRUSHING: HTCompoundRecipeLookup<HTItemToDoubleItemRecipe> = create(RagiumConstants.CRUSHING)

    @JvmField
    val CUTTING: HTCompoundRecipeLookup<HTItemToDoubleItemRecipe> = create(RagiumConstants.CUTTING)

    @JvmField
    val DRAINING: HTCompoundRecipeLookup<HTItemToItemAndFluidRecipe> = create(RagiumConstants.DRAINING)

    @JvmField
    val FILLING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConstants.FILLING)

    // Heat
    @JvmField
    val FREEZING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConstants.FREEZING)

    @JvmField
    val MELTING: HTCompoundRecipeLookup<HTItemToFluidRecipe> = create(RagiumConstants.MELTING)

    @JvmField
    val PYROLYZING: HTCompoundRecipeLookup<HTItemToItemAndFluidRecipe> = create(RagiumConstants.PYROLYZING)

    @JvmField
    val REFINING: HTRecipeLookup<RTRefiningRecipe> = create(RagiumRecipeTypes.REFINING)

    // Chemical
    @JvmField
    val BATHING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConstants.BATHING)

    @JvmField
    val ELECTROLYZING: HTRecipeLookup<RTElectrolyzingRecipe> = create(RagiumRecipeTypes.ELECTROLYZING)

    // Bio
    @JvmField
    val BREWING: HTCompoundRecipeLookup<HTItemAndFluidToFluidRecipe> = create(RagiumConstants.BREWING)

    @JvmField
    val PLANTING: HTCompoundRecipeLookup<HTItemToDoubleItemRecipe> = create(RagiumConstants.PLANTING)

    // Electronics

    // Arcane
}
