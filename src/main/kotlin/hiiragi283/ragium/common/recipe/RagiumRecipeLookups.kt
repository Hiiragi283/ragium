package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.getDataSequence
import hiiragi283.core.impl.recipe.HTRecipeTypeImpl
import hiiragi283.core.impl.recipe.HTRecipeTypeManager
import hiiragi283.core.impl.recipe.addProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

data object RagiumRecipeLookups {
    // Machine - Basic
    @JvmField
    val ALLOYING: HTRecipeTypeImpl<HTShapelessRecipeInput, HTAlloyingRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val ASSEMBLING: HTRecipeTypeImpl<HTShapelessRecipeInput, HTAssemblingRecipe> = create(RagiumConst.ASSEMBLING)

    @JvmField
    val CUTTING: HTRecipeTypeImpl<SingleRecipeInput, HTSingleMultiOutputRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val PLANTING: HTRecipeTypeImpl<HTDoubleRecipeInput, HTDoubleMultiOutputRecipe> = create(RagiumConst.PLANTING)

    // Machine - Advanced
    @JvmField
    val FREEZING: HTRecipeTypeImpl<HTItemAndFluidRecipeInput, HTFreezingRecipe> = create(RagiumConst.FREEZING)

    @JvmField
    val MELTING: HTRecipeTypeImpl<SingleRecipeInput, HTMeltingRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTRecipeTypeImpl<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> = create(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTRecipeTypeImpl<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> = create(RagiumConst.REFINING)

    // Machine - Elite
    @JvmField
    val CHEMICAL_WASHING: HTRecipeTypeImpl<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> = create(RagiumConst.CHEMICAL_WASHING)

    @JvmField
    val ELECTROLYZING: HTRecipeTypeImpl<HTSingleFluidRecipeInput, HTElectrolyzingRecipe> = create(RagiumConst.ELECTROLYZING)

    @JvmField
    val MIXING: HTRecipeTypeImpl<HTChemicalRecipeInput, HTMixingRecipe> = create(RagiumConst.MIXING)

    @JvmField
    val WASHING: HTRecipeTypeImpl<HTItemAndFluidRecipeInput, HTWashingRecipe> = create(RagiumConst.WASHING)

    // Machine - Ultimate
    @JvmField
    val DUPLICATING: HTRecipeTypeImpl<HTItemAndFluidRecipeInput, RagiumDuplicatingRecipe> = create(RagiumConst.DUPLICATING)

    @JvmField
    val ENCHANTING: HTRecipeTypeImpl<HTEnchantingRecipe.Input, HTEnchantingRecipe> = create(RagiumConst.ENCHANTING)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(path: String): HTRecipeTypeImpl<INPUT, RECIPE> =
        HTRecipeTypeManager.create(RagiumAPI.id(path))

    @JvmStatic
    fun init() {
        ALLOYING.addProvider(RagiumRecipeTypes.ALLOYING.get(), identity())
        ASSEMBLING.addProvider(RagiumRecipeTypes.ASSEMBLING.get(), identity())
        CUTTING.addProvider(RagiumRecipeTypes.CUTTING.get(), identity())
        PLANTING.addProvider(RagiumRecipeTypes.PLANTING.get(), identity())

        FREEZING.addProvider(RagiumRecipeTypes.FREEZING.get(), identity())
        MELTING.addProvider(RagiumRecipeTypes.MELTING.get(), identity())
        PYROLYZING.addProvider(RagiumRecipeTypes.PYROLYZING.get(), identity())
        REFINING.addProvider(RagiumRecipeTypes.REFINING.get(), identity())

        CHEMICAL_WASHING.addProvider(RagiumRecipeTypes.CHEMICAL_WASHING.get(), identity())
        ELECTROLYZING.addProvider(RagiumRecipeTypes.ELECTROLYZING.get(), identity())
        MIXING.addProvider(RagiumRecipeTypes.MIXING.get(), identity())
        WASHING.addProvider(RagiumRecipeTypes.WASHING.get(), identity())

        DUPLICATING.addProvider { context: HTRecipeLookup.Context ->
            context.access
                .lookupOrThrow(Registries.ITEM)
                .getDataSequence(RagiumDataMapTypes.DUPLICATION_COST)
                .map { (holder: HTSimpleHolderLike<Item>, matterValue: Int) ->
                    HTRecipeHolder(holder.getId(), RagiumDuplicatingRecipe(HTIngredientCreator.create(holder.get()), matterValue))
                }
        }
        ENCHANTING.addProvider(RagiumRecipeTypes.ENCHANTING.get(), identity())
    }
}
