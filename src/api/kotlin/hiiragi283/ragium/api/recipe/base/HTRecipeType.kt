package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTMachineRecipesUpdatedEvent
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.core.RegistryAccess
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import java.util.*

/**
 * [RecipeSerializer]と[RecipeType]を束ねたクラス
 * @see [HTMachineRecipeBase.getRecipeType]
 */
class HTRecipeType<T : HTMachineRecipeBase>(val machine: HTMachineType, val serializer: RecipeSerializer<T>) : RecipeType<T> {
    constructor(
        machine: HTMachineType,
        codec: MapCodec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
    ) : this(
        machine,
        object : RecipeSerializer<T> {
            override fun codec(): MapCodec<T> = codec

            override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
        },
    )

    private var recipeCache: Map<ResourceLocation, RecipeHolder<T>> = mapOf()
    private var lastRecipe: ResourceLocation? = null
    private var changed: Boolean = true

    fun getFirstRecipe(input: HTMachineRecipeInput, level: Level): Result<T> {
        var firstRecipe: RecipeHolder<T>? = null
        // Check cache update
        this.reloadCache()
        // Find from cache
        if (lastRecipe != null) {
            firstRecipe = recipeCache[lastRecipe]
        }
        if (firstRecipe == null) {
            firstRecipe =
                recipeCache.values.firstOrNull { holder: RecipeHolder<T> -> holder.value.matches(input, level) }
        }
        return Optional
            .ofNullable(firstRecipe)
            .map { holder: RecipeHolder<T> ->
                lastRecipe = holder.id
                Result.success(holder.value)
            }.orElseGet {
                lastRecipe = null
                Result.failure(HTMachineException.NoMatchingRecipe(false))
            }
    }

    fun getAllRecipes(): List<RecipeHolder<T>> = recipeCache.values.toList()

    //    Cache    //

    fun setChanged() {
        changed = true
    }

    fun reloadCache() {
        if (changed && RagiumAPI.getInstance().getCurrentSide().isServer) {
            RagiumAPI
                .getInstance()
                .getCurrentServer()
                ?.recipeManager
                ?.let(::reloadCache)
            changed = false
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun reloadCache(manager: RecipeManager) {
        lastRecipe = null
        recipeCache = buildMap {
            val consumer: (RecipeHolder<T>) -> Unit = { holder: RecipeHolder<T> ->
                put(holder.id, holder)
            }
            // Reload from RecipeManager
            manager.getAllRecipesFor(this@HTRecipeType).forEach(consumer)
            // Reload from Event
            val access: RegistryAccess = RagiumAPI.getInstance().getRegistryAccess() ?: return@buildMap
            val event = HTMachineRecipesUpdatedEvent(
                access,
                this@HTRecipeType,
            ) { holder: RecipeHolder<out HTMachineRecipeBase> ->
                val recipe: T = holder.value as? T ?: return@HTMachineRecipesUpdatedEvent
                consumer(RecipeHolder(holder.id, recipe))
            }
            FORGE_BUS.post(event)
        }
    }

    override fun toString(): String = machine.serializedName
}
