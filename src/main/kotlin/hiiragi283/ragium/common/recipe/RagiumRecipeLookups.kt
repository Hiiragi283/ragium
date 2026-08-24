package hiiragi283.ragium.common.recipe

import hiiragi283.lib.collection.ListMultiMap
import hiiragi283.lib.collection.buildListMultiMap
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToItemAndFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.recipe.lookup.HTCompoundRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.lib.recipe.lookup.fromRecipeType
import hiiragi283.lib.registry.asSupplier
import hiiragi283.lib.registry.getDataMap
import hiiragi283.lib.resource.modifyPath
import hiiragi283.lib.util.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.recipe.RTBathingRecipe
import hiiragi283.ragium.api.recipe.RTBrewingRecipe
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.api.recipe.RTRefiningRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.fluid.RagiumFluids
import kotlin.collections.iterator
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.Item
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable

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

    // Electronics

    // Arcane

    @JvmStatic
    fun init() {
        ASSEMBLING.fromRecipeType(RagiumRecipeTypes.ASSEMBLING, identity())
        COMPRESSING.fromRecipeType(RagiumRecipeTypes.COMPRESSING, identity())
        CRUSHING.fromRecipeType(RagiumRecipeTypes.CRUSHING, identity())
        CUTTING.fromRecipeType(RagiumRecipeTypes.CUTTING, identity())

        FREEZING.fromRecipeType(RagiumRecipeTypes.FREEZING, identity())
        MELTING.fromRecipeType(RagiumRecipeTypes.MELTING, identity())
        PYROLYZING.fromRecipeType(RagiumRecipeTypes.PYROLYZING, identity())

        BATHING.fromRecipeType(RagiumRecipeTypes.BATHING, identity())
        BATHING.addSubLookup { contextMap: ContextMap ->
            val registries: HolderLookup.Provider = contextMap.getOptional(HTRecipeLookupContext.REGISTRIES) ?: return@addSubLookup mapOf()
            val oxidizableMap: Map<Holder<Block>, Oxidizable> = registries.lookupOrThrow(Registries.BLOCK).getDataMap(NeoForgeDataMaps.OXIDIZABLES)
            val recipeMap: MutableMap<RecipeKey, RTBathingRecipe> = mutableMapOf()
            for ((key: Holder<Block>, value: Oxidizable) in oxidizableMap) {
                val base: Item = key.value().asItem()
                val oxidized: Item = value.nextOxidationStage().asItem()
                RagiumRecipeBuilders.bathing {
                    itemIngredient { items { +base } }
                    fluidIngredient {
                        +registries.getOrThrow(RagiumFluids.OXYGEN.fluidTag)
                        amount = 250
                    }
                    result { +oxidized }
                    recipeId prefix "oxidization/"
                }.save(recipeMap::put)
                RagiumRecipeBuilders.bathing {
                    itemIngredient { items { +oxidized } }
                    fluidIngredient {
                        +registries.getOrThrow(RagiumFluids.HYDROGEN.fluidTag)
                        amount = 250
                    }
                    result { +base }
                    recipeId prefix "reduction/"
                }.save(recipeMap::put)
            }
            recipeMap
        }

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
                            result { +mix.to() }
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
