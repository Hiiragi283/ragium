package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.registry.getDataMap
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.util.identity
import hiiragi283.core.impl.recipe.cache.HTCompoundRecipeLookup
import hiiragi283.core.impl.recipe.cache.HTVanillaRecipeLookup
import hiiragi283.core.impl.recipe.cache.fromRecipeType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.base.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.custom.HTBookMeltingRecipe
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

data object RagiumRecipeLookups {
    // Machine - Basic
    @JvmField
    val ALLOYING: HTRecipeLookup<HTAlloyingRecipe> = create(RagiumRecipeTypes.ALLOYING)

    @JvmField
    val ASSEMBLING: HTCompoundRecipeLookup<HTDoubleItemToItemRecipe> = create(RagiumConst.ASSEMBLING)

    @JvmField
    val COMPRESSING: HTCompoundRecipeLookup<HTItemToItemRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CUTTING: HTCompoundRecipeLookup<HTItemToMultiItemRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val PLANTING: HTCompoundRecipeLookup<HTPlantingRecipe> = create(RagiumConst.PLANTING)

    // Machine - Advanced
    @JvmField
    val FREEZING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConst.FREEZING)

    @JvmField
    val IMPLODING: HTRecipeLookup<HTImplodingRecipe> = create(RagiumRecipeTypes.IMPLODING)

    @JvmField
    val MELTING: HTCompoundRecipeLookup<HTItemToFluidRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: HTCompoundRecipeLookup<HTItemOrFluidRecipe> = create(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTRecipeLookup<HTRefiningRecipe> = create(RagiumRecipeTypes.REFINING)

    @JvmField
    val WASHING: HTRecipeLookup<HTWashingRecipe> = create(RagiumRecipeTypes.WASHING)

    // Machine - Elite
    @JvmField
    val BATHING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConst.BATHING)

    @JvmField
    val CHEMICAL_REACTING: HTRecipeLookup<HTChemicalReactingRecipe> = create(RagiumRecipeTypes.CHEMICAL_REACTING)

    @JvmField
    val MIXING: HTRecipeLookup<HTMixingRecipe> = create(RagiumRecipeTypes.MIXING)

    // Machine - Ultimate
    @JvmField
    val MASS_FABRICATING: HTRecipeLookup.Translatable<HTMassFabricatingRecipe> = object : HTRecipeLookup.Translatable<HTMassFabricatingRecipe> {
        override fun getAllRecipes(context: HTRecipeLookup.Context): Map<ResourceLocation, HTMassFabricatingRecipe> = context
            .lookup(Registries.ITEM)
            .getOrNull()
            ?.getDataMap(RagiumDataMapTypes.MATTER_POINT)
            ?.asSequence()
            ?.associate { (item: Holder<Item>, point: Int) ->
                HTRecipeHolder(
                    item.toLike().getId().withPrefix("${RagiumConst.MASS_FABRICATING}/"),
                    HTMassFabricatingRecipe(ItemStack(item), point),
                )
            } ?: mapOf()

        override fun getKey(): ResourceKey<RecipeType<*>> = Registries.RECIPE_TYPE.createKey(RagiumAPI.id(RagiumConst.MASS_FABRICATING))
    }

    // Device - Ultimate
    @JvmField
    val ENCHANTING: HTCompoundRecipeLookup<HTEnchantingRecipe> = create(RagiumConst.ENCHANTING)

    @JvmStatic
    private fun <RECIPE : Any> create(path: String): HTCompoundRecipeLookup<RECIPE> = HTCompoundRecipeLookup.create(RagiumAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(recipeType: HTRecipeType<RECIPE>): HTRecipeLookup<RECIPE> = HTVanillaRecipeLookup(recipeType)

    @JvmStatic
    fun init() {
        ASSEMBLING.fromRecipeType(RagiumRecipeTypes.ASSEMBLING, identity())
        COMPRESSING.fromRecipeType(RagiumRecipeTypes.COMPRESSING, identity())
        CUTTING.fromRecipeType(RagiumRecipeTypes.CUTTING, identity())
        PLANTING.fromRecipeType(RagiumRecipeTypes.PLANTING, identity())

        FREEZING.fromRecipeType(RagiumRecipeTypes.FREEZING, identity())
        MELTING.fromRecipeType(RagiumRecipeTypes.MELTING, identity())
        MELTING.addRecipes(RagiumAPI.id(RagiumConst.MELTING, "exp_from_ench_book") to HTBookMeltingRecipe)
        PYROLYZING.fromRecipeType(RagiumRecipeTypes.PYROLYZING, identity())

        BATHING.fromRecipeType(RagiumRecipeTypes.BATHING, identity())

        ENCHANTING.fromRecipeType(RagiumRecipeTypes.ENCHANTING, identity())
    }
}
