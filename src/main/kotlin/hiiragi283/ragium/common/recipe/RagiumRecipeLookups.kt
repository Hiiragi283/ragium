package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.function.identity
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.getDataSequence
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.impl.recipe.cache.HTRecipeLookupImpl
import hiiragi283.core.impl.recipe.cache.HTRecipeLookupManager
import hiiragi283.core.impl.recipe.cache.HTVanillaRecipeLookup
import hiiragi283.core.impl.recipe.cache.addProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.base.HTPlantingRecipe
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

data object RagiumRecipeLookups {
    // Machine - Basic
    @JvmField
    val ALLOYING: HTRecipeLookup<HTAlloyingRecipe> = create(RagiumRecipeTypes.ALLOYING)

    @JvmField
    val ASSEMBLING: HTRecipeLookup<HTAssemblingRecipe> = create(RagiumRecipeTypes.ASSEMBLING)

    @JvmField
    val CUTTING: HTRecipeLookupImpl<HTItemToMultiItemRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val PLANTING: HTRecipeLookupImpl<HTPlantingRecipe> = create(RagiumConst.PLANTING)

    // Machine - Advanced
    @JvmField
    val FREEZING: HTRecipeLookup<HTFreezingRecipe> = create(RagiumRecipeTypes.FREEZING)

    @JvmField
    val IMPLODING: HTRecipeLookup<HTImplodingRecipe> = create(RagiumRecipeTypes.IMPLODING)

    @JvmField
    val MELTING: HTRecipeLookup<HTMeltingRecipe> = create(RagiumRecipeTypes.MELTING)

    @JvmField
    val PYROLYZING: HTRecipeLookupImpl<HTItemOrFluidRecipe> = create(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTRecipeLookupImpl<HTItemOrFluidRecipe> = create(RagiumConst.REFINING)

    @JvmField
    val WASHING: HTRecipeLookup<HTWashingRecipe> = create(RagiumRecipeTypes.WASHING)

    // Machine - Elite
    @JvmField
    val CHEMICAL_REACTING: HTRecipeLookup<HTChemicalReactingRecipe> = create(RagiumRecipeTypes.CHEMICAL_REACTING)

    @JvmField
    val CHEMICAL_WASHING: HTRecipeLookupImpl<HTItemOrFluidRecipe> = create(RagiumConst.CHEMICAL_WASHING)

    @JvmField
    val MIXING: HTRecipeLookup<HTMixingRecipe> = create(RagiumRecipeTypes.MIXING)

    // Machine - Ultimate
    @JvmField
    val MASS_FABRICATING: HTRecipeType<HTMassFabricatingRecipe> = object : HTRecipeType<HTMassFabricatingRecipe> {
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
    val ENCHANTING: HTRecipeLookupImpl<HTEnchantingRecipe> = create(RagiumConst.ENCHANTING)

    @JvmStatic
    private fun <RECIPE : Any> create(path: String): HTRecipeLookupImpl<RECIPE> = HTRecipeLookupManager.create(RagiumAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(recipeType: HTDeferredRecipeType<RECIPE>): HTRecipeLookup<RECIPE> =
        HTVanillaRecipeLookup(recipeType)

    @JvmStatic
    fun init() {
        CUTTING.addProvider(RagiumRecipeTypes.CUTTING.get(), identity())
        PLANTING.addProvider(RagiumRecipeTypes.PLANTING.get(), identity())

        PYROLYZING.addProvider(RagiumRecipeTypes.PYROLYZING.get(), identity())
        REFINING.addProvider(RagiumRecipeTypes.REFINING.get(), identity())

        CHEMICAL_WASHING.addProvider(RagiumRecipeTypes.CHEMICAL_WASHING.get(), identity())

        ENCHANTING.addProvider(RagiumRecipeTypes.ENCHANTING.get(), identity())
    }
}
