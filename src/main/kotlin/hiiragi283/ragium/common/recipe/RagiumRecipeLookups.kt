package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.function.identity
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.getDataSequence
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.impl.recipe.HTRecipeLookupImpl
import hiiragi283.core.impl.recipe.HTRecipeLookupManager
import hiiragi283.core.impl.recipe.HTVanillaRecipeLookup
import hiiragi283.core.impl.recipe.addProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.api.recipe.input.HTMixingRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

data object RagiumRecipeLookups {
    // Machine - Basic
    @JvmField
    val ALLOYING: HTRecipeLookup<HTAlloyingRecipe.Input, HTAlloyingRecipe> = create(RagiumRecipeTypes.ALLOYING)

    @JvmField
    val ASSEMBLING: HTRecipeLookup<HTDoubleRecipeInput, HTAssemblingRecipe> = create(RagiumRecipeTypes.ASSEMBLING)

    @JvmField
    val CUTTING: HTRecipeLookupImpl<SingleRecipeInput, HTSingleMultiOutputRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val PLANTING: HTRecipeLookupImpl<HTDoubleRecipeInput, HTDoubleMultiOutputRecipe> = create(RagiumConst.PLANTING)

    // Machine - Advanced
    @JvmField
    val FREEZING: HTRecipeLookup<HTItemAndFluidRecipeInput, HTFreezingRecipe> = create(RagiumRecipeTypes.FREEZING)

    @JvmField
    val IMPLODING: HTRecipeLookupImpl<HTDoubleRecipeInput, HTDoubleMultiOutputRecipe> = create(RagiumConst.IMPLODING)

    @JvmField
    val MELTING: HTRecipeLookup<SingleRecipeInput, HTMeltingRecipe> = create(RagiumRecipeTypes.MELTING)

    @JvmField
    val PYROLYZING: HTRecipeLookupImpl<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> = create(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTRecipeLookupImpl<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> = create(RagiumConst.REFINING)

    @JvmField
    val WASHING: HTRecipeLookup<HTItemAndFluidRecipeInput, HTWashingRecipe> = create(RagiumRecipeTypes.WASHING)

    // Machine - Elite
    @JvmField
    val CHEMICAL_WASHING: HTRecipeLookupImpl<HTItemAndFluidRecipeInput, HTItemOrFluidRecipe> = create(RagiumConst.CHEMICAL_WASHING)

    @JvmField
    val MIXING: HTRecipeLookupImpl<HTMixingRecipeInput, HTMixingRecipe> = create(RagiumConst.MIXING)

    // Machine - Ultimate
    @JvmField
    val MASS_FABRICATING: HTRecipeType<SingleRecipeInput, HTMassFabricatingRecipe> = object : HTRecipeType<SingleRecipeInput, HTMassFabricatingRecipe> {
        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<HTMassFabricatingRecipe>> = context
            .lookup(Registries.ITEM)
            ?.getDataSequence(RagiumDataMapTypes.MATTER_POINT)
            ?.map { (item: HTSimpleHolderLike<Item>, point: Int) ->
                HTRecipeHolder(
                    item.getId().withPrefix("${RagiumConst.MASS_FABRICATING}/"),
                    HTMassFabricatingRecipe(ItemStack(item.get()), point),
                )
            }
            ?: emptySequence()

        override fun getId(): ResourceLocation = RagiumAPI.id(RagiumConst.MASS_FABRICATING)
    }

    // Device - Ultimate
    @JvmField
    val ENCHANTING: HTRecipeLookupImpl<HTEnchantingRecipe.Input, HTEnchantingRecipe> = create(RagiumConst.ENCHANTING)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Any> create(path: String): HTRecipeLookupImpl<INPUT, RECIPE> =
        HTRecipeLookupManager.create(RagiumAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(
        recipeType: HTDeferredRecipeType<RECIPE>,
    ): HTRecipeLookup<INPUT, RECIPE> = HTVanillaRecipeLookup(recipeType)

    @JvmStatic
    fun init() {
        CUTTING.addProvider(RagiumRecipeTypes.CUTTING.get(), identity())
        PLANTING.addProvider(RagiumRecipeTypes.PLANTING.get(), identity())

        IMPLODING.addProvider(RagiumRecipeTypes.IMPLODING.get(), identity())
        PYROLYZING.addProvider(RagiumRecipeTypes.PYROLYZING.get(), identity())
        REFINING.addProvider(RagiumRecipeTypes.REFINING.get(), identity())

        CHEMICAL_WASHING.addProvider(RagiumRecipeTypes.CHEMICAL_WASHING.get(), identity())
        MIXING.addProvider(RagiumRecipeTypes.MIXING.get(), identity())

        ENCHANTING.addProvider(RagiumRecipeTypes.ENCHANTING.get(), identity())
    }
}
