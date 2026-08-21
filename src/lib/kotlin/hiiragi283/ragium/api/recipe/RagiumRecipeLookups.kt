package hiiragi283.ragium.api.recipe

import hiiragi283.lib.collection.ListMultiMap
import hiiragi283.lib.collection.buildListMultiMap
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTItemOrFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.recipe.lookup.HTCompoundRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.lib.recipe.lookup.fromRecipeType
import hiiragi283.lib.registry.asSupplier
import hiiragi283.lib.resource.modifyPath
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import net.minecraft.resources.Identifier
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

data object RagiumRecipeLookups {
    @JvmStatic
    private fun <RECIPE : Any> create(path: String): HTCompoundRecipeLookup<RECIPE> = HTCompoundRecipeLookup.create(RagiumAPI.id(path))

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(recipeType: HTRecipeType<RECIPE>): HTRecipeLookup<RECIPE> = HTVanillaRecipeLookup(recipeType)

    // Mechanical
    @JvmField
    val ASSEMBLING: HTCompoundRecipeLookup<HTDoubleItemToItemRecipe> = create(RagiumConstants.ASSEMBLING)

    @JvmField
    val CRUSHING: HTCompoundRecipeLookup<HTItemToDoubleItemRecipe> = create(RagiumConstants.CRUSHING)

    @JvmField
    val CUTTING: HTCompoundRecipeLookup<HTItemToDoubleItemRecipe> = create(RagiumConstants.CUTTING)

    // Heat
    @JvmField
    val FREEZING: HTCompoundRecipeLookup<HTItemAndFluidToItemRecipe> = create(RagiumConstants.FREEZING)

    @JvmField
    val MELTING: HTCompoundRecipeLookup<HTItemToFluidRecipe> = create(RagiumConstants.MELTING)

    // Chemical
    @JvmField
    val ELECTROLYZING: HTRecipeLookup<RTElectrolyzingRecipe> = create(RagiumRecipeTypes.ELECTROLYZING)

    // Bio
    @JvmField
    val BREWING: HTCompoundRecipeLookup<HTItemOrFluidRecipe> = create(RagiumConstants.BREWING)

    // Electronics

    // Arcane

    @JvmStatic
    fun init() {
        ASSEMBLING.fromRecipeType(RagiumRecipeTypes.ASSEMBLING, identity())
        CRUSHING.fromRecipeType(RagiumRecipeTypes.CRUSHING, identity())
        CUTTING.fromRecipeType(RagiumRecipeTypes.CUTTING, identity())

        FREEZING.fromRecipeType(RagiumRecipeTypes.FREEZING, identity())
        MELTING.fromRecipeType(RagiumRecipeTypes.MELTING, identity())

        BREWING.fromRecipeType(RagiumRecipeTypes.BREWING, identity())
        BREWING.addSubLookup { contextMap: ContextMap ->
            val multiMap: ListMultiMap<Identifier, RTBrewingRecipe> = buildListMultiMap {
                contextMap.getOptional(HTRecipeLookupContext.BREWING)
                    ?.let(PotionBrewing::potionMixes)
                    ?.asSequence()
                    ?.forEach { mix: PotionBrewing.Mix<Potion> ->
                        RagiumRecipeBuilders.brewing {
                            itemIngredient { +mix.ingredient }
                            fluidIngredient { +HTPotionFluidIngredient(mix.from()) }
                            fluidResult { +mix.to() }
                        }.save { _, recipe: RTBrewingRecipe ->
                            put(mix.to().asSupplier().getId().modifyPath { "/${RagiumConstants.BREWING}/$it" }, recipe)
                        }
                    }
            }
            if (multiMap.isEmpty) return@addSubLookup mapOf()
            val recipeMap: MutableMap<RecipeKey, RTBrewingRecipe> = mutableMapOf()
            for ((potionTo: Identifier, recipes: Collection<RTBrewingRecipe>) in multiMap.entries) {
                recipes.forEachIndexed { index: Int, recipe: RTBrewingRecipe ->
                    recipeMap[RecipeKey(potionTo.withSuffix("_$index"))] = recipe
                }
            }
            recipeMap
        }
    }
}
