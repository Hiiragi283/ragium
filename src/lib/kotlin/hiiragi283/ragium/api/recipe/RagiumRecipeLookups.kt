package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTItemOrFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToItemAndFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.lib.recipe.lookup.HTCompoundRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

/**
 * Ragiumで使用される[HTRecipeLookup]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumRecipeLookups {
    @JvmStatic
    private fun <RECIPE : Any> create(path: String): HTCompoundRecipeLookup<RECIPE> =
        HTCompoundRecipeLookup.create(RagiumAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
        recipeType: RecipeType<RECIPE>
    ): HTRecipeLookup<RECIPE> = HTVanillaRecipeLookup(recipeType)

    // Mechanical
    @JvmField
    val ASSEMBLING: HTCompoundRecipeLookup<HTSizedShapelessRecipe> = create(RagiumConstants.ASSEMBLING)

    @JvmField
    val CENTRIFUGING: HTCompoundRecipeLookup<RTCentrifugingRecipe> = create(RagiumConstants.CENTRIFUGING)

    @JvmField
    val COMPRESSING: HTCompoundRecipeLookup<HTItemToItemRecipe> = create(RagiumConstants.COMPRESSING)

    @JvmField
    val CRUSHING: HTCompoundRecipeLookup<HTItemToDoubleItemRecipe> = create(RagiumConstants.CRUSHING)

    @JvmField
    val CUTTING: HTCompoundRecipeLookup<HTItemToDoubleItemRecipe> = create(RagiumConstants.CUTTING)

    @JvmField
    val FILLING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConstants.FILLING)

    // Heat
    @JvmField
    val ALLOYING: HTCompoundRecipeLookup<HTSizedShapelessRecipe> = create(RagiumConstants.ALLOYING)

    @JvmField
    val FREEZING: HTRecipeLookup<RTFreezingRecipe> = create(RagiumRecipeTypes.FREEZING)

    @JvmField
    val MELTING: HTCompoundRecipeLookup<HTItemToFluidRecipe> = create(RagiumConstants.MELTING)

    @JvmField
    val PYROLYZING: HTCompoundRecipeLookup<HTItemToItemAndFluidRecipe> = create(RagiumConstants.PYROLYZING)

    @JvmField
    val REFINING: HTCompoundRecipeLookup<HTFluidSeparatingRecipe> = create(RagiumConstants.REFINING)

    // Chemical
    @JvmField
    val BATHING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConstants.BATHING)

    @JvmField
    val EXCHANGING: HTCompoundRecipeLookup<HTItemOrFluidRecipe> = create(RagiumConstants.EXCHANGING)

    @JvmField
    val MIXING: HTCompoundRecipeLookup<RTMixingRecipe> = create(RagiumConstants.MIXING)

    @JvmField
    val REACTING: HTRecipeLookup<RTReactingRecipe> = create(RagiumRecipeTypes.REACTING)

    @JvmField
    val WASHING: HTRecipeLookup<RTWashingRecipe> = create(RagiumRecipeTypes.WASHING)

    // Resource
    @JvmField
    val BREWING: HTCompoundRecipeLookup<HTItemAndFluidToFluidRecipe> = create(RagiumConstants.BREWING)

    @JvmField
    val PLANTING: HTCompoundRecipeLookup<HTItemToDoubleItemRecipe> = create(RagiumConstants.PLANTING)

    @JvmField
    val COLLECTING: HTRecipeLookup<RTCollectingRecipe> = create(RagiumRecipeTypes.COLLECTING)

    @JvmField
    val RESOURCE_PUMPING: HTRecipeLookup<RTResourcePumpingRecipe> = create(RagiumRecipeTypes.RESOURCE_PUMPING)

    // Electronics
    @JvmField
    val ELECTROLYZING: HTCompoundRecipeLookup<HTFluidSeparatingRecipe> = create(RagiumConstants.ELECTROLYZING)

    @JvmField
    val ENGRAVING: HTRecipeLookup<RTEngravingRecipe> = create(RagiumRecipeTypes.ENGRAVING)

    // Arcane
    @JvmField
    val ENCHANTING: HTRecipeLookup<RTEnchantingRecipe> = create(RagiumRecipeTypes.ENCHANTING)
}
