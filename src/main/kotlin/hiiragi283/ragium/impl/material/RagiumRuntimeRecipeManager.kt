package hiiragi283.ragium.impl.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.collection.ImmutableMultiMap
import hiiragi283.ragium.api.collection.buildMultiMap
import hiiragi283.ragium.api.collection.immutableMultiMapOf
import hiiragi283.ragium.api.data.map.HTRuntimeRecipeProvider
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.recipe.HTRuntimeRecipeManager
import hiiragi283.ragium.api.recipe.castRecipe
import hiiragi283.ragium.api.registry.getHolderDataMap
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent

@EventBusSubscriber
object RagiumRuntimeRecipeManager : HTRuntimeRecipeManager {
    @JvmStatic
    private var providerCache: ImmutableMultiMap<RecipeType<*>, HTRuntimeRecipeProvider> = immutableMultiMapOf()

    @JvmStatic
    private var recipeMultiMap: ImmutableMultiMap<RecipeType<*>, RecipeHolder<*>> = immutableMultiMapOf()

    @JvmStatic
    private fun reloadRecipeMap(access: RegistryAccess?, manager: RecipeManager) {
        if (access == null) return
        if (this.recipeMultiMap.isEmpty()) {
            this.recipeMultiMap = buildMultiMap {
                providerCache.forEach { (recipeType: RecipeType<*>, provider: HTRuntimeRecipeProvider) ->
                    putAll(recipeType, provider.generateRecipes(access, manager))
                }
            }
        }
    }

    override fun <RECIPE : Recipe<*>> getAllRecipes(
        recipeManager: RecipeManager,
        recipeType: RecipeType<RECIPE>,
    ): List<RecipeHolder<RECIPE>> = getAllRecipes(RagiumPlatform.INSTANCE.getRegistryAccess(), recipeManager, recipeType)

    @JvmStatic
    private fun <RECIPE : Recipe<*>> getAllRecipes(
        access: RegistryAccess?,
        recipeManager: RecipeManager,
        recipeType: RecipeType<RECIPE>,
    ): List<RecipeHolder<RECIPE>> {
        reloadRecipeMap(access, recipeManager)
        return recipeMultiMap[recipeType].mapNotNull { it.castRecipe() }
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
                else -> getAllRecipes(level.registryAccess(), level.recipeManager, recipeType)
                    .firstOrNull { holder: RecipeHolder<RECIPE> -> holder.value.matches(input, level) }
            }
        }
    }

    @SubscribeEvent
    fun onDataMapUpdated(event: DataMapsUpdatedEvent) {
        if (event.cause != DataMapsUpdatedEvent.UpdateCause.SERVER_RELOAD) return
        event.ifRegistry(Registries.RECIPE_TYPE) { registry: Registry<RecipeType<*>> ->
            val holderMap: Map<Holder.Reference<RecipeType<*>>, Map<ResourceLocation, HTRuntimeRecipeProvider>> =
                registry.getHolderDataMap(RagiumDataMaps.MATERIAL_RECIPE)

            providerCache = buildMultiMap {
                for ((holder: Holder.Reference<RecipeType<*>>, map: Map<ResourceLocation, HTRuntimeRecipeProvider>) in holderMap) {
                    val recipeType: RecipeType<*> = holder.value()
                    putAll(recipeType, map.values)
                }
            }

            recipeMultiMap = immutableMultiMapOf()
            RagiumAPI.LOGGER.info("Reloaded Material Recipes!")
        }
    }
}
