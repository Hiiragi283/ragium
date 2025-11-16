package hiiragi283.ragium.impl.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.collection.ImmutableMultiMap
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildMultiMap
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.collection.immutableMultiMapOf
import hiiragi283.ragium.api.collection.immutableTableOf
import hiiragi283.ragium.api.data.map.HTMaterialRecipeData
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.recipe.HTMaterialRecipeManager
import hiiragi283.ragium.api.recipe.castRecipe
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent

@EventBusSubscriber
object RagiumMaterialRecipeManager : HTMaterialRecipeManager {
    @JvmStatic
    private lateinit var itemCreator: HTItemIngredientCreator

    @JvmStatic
    private lateinit var fluidCreator: HTFluidIngredientCreator

    @JvmStatic
    private var recipeMultiMap: ImmutableMultiMap<RecipeType<*>, RecipeHolder<*>> = immutableMultiMapOf()

    @JvmStatic
    private var recipeTable: ImmutableTable<RecipeType<*>, ResourceLocation, RecipeHolder<*>> = immutableTableOf()

    override fun <RECIPE : Recipe<*>> getAllRecipes(recipeType: RecipeType<RECIPE>): List<RecipeHolder<RECIPE>> =
        recipeMultiMap[recipeType].mapNotNull { it.castRecipe() }

    @JvmStatic
    fun <RECIPE : Recipe<*>> getRecipeById(recipeType: RecipeType<RECIPE>, id: ResourceLocation?): RecipeHolder<RECIPE>? = when (id) {
        null -> null
        else -> recipeTable[recipeType, id]?.castRecipe()
    }

    override fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        recipeType: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: ResourceLocation?,
    ): RecipeHolder<RECIPE>? {
        val holder: RecipeHolder<RECIPE>? = getRecipeById(recipeType, lastRecipe)
        return getRecipeFor(recipeType, input, level, holder)
    }

    override fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        recipeType: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: RecipeHolder<RECIPE>?,
    ): RecipeHolder<RECIPE>? = when {
        input.isEmpty -> null
        else -> {
            when {
                lastRecipe != null && lastRecipe.value.matches(input, level) -> lastRecipe
                else -> getAllRecipes(recipeType).firstOrNull { holder -> holder.value.matches(input, level) }
            }
        }
    }

    @SubscribeEvent
    fun onDataMapUpdated(event: DataMapsUpdatedEvent) {
        if (event.cause != DataMapsUpdatedEvent.UpdateCause.SERVER_RELOAD) return
        val registries: RegistryAccess = event.registries
        itemCreator = RagiumPlatform.INSTANCE.createItemCreator(registries)
        fluidCreator = RagiumPlatform.INSTANCE.createFluidCreator(registries)

        event.ifRegistry(Registries.RECIPE_TYPE) { registry: Registry<RecipeType<*>> ->
            val keyMap: Map<ResourceKey<RecipeType<*>>, Map<ResourceLocation, HTMaterialRecipeData>> =
                registry.getDataMap(RagiumDataMaps.MATERIAL_RECIPE)
            recipeMultiMap = buildMultiMap {
                for ((key: ResourceKey<RecipeType<*>>, map: Map<ResourceLocation, HTMaterialRecipeData>) in keyMap) {
                    val recipeType: RecipeType<*> = registry.get(key) ?: continue
                    for (data: HTMaterialRecipeData in map.values) {
                        val recipes: List<RecipeHolder<*>> = data.generateRecipes(registries, itemCreator, fluidCreator)
                        putAll(recipeType, recipes)
                    }
                }
            }
            recipeTable = buildTable {
                recipeMultiMap.forEach { (key: RecipeType<*>, value: RecipeHolder<*>) ->
                    put(key, value.id, value)
                }
            }
            RagiumAPI.LOGGER.info("Reloaded Material Recipes!")
        }
    }
}
