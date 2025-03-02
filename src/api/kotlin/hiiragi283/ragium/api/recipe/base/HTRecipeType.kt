package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTMachineRecipesUpdatedEvent
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

/**
 * [RecipeSerializer]と[RecipeType]を束ねたクラス
 * @see [HTMachineRecipe.getRecipeType]
 */
class HTRecipeType<T : HTMachineRecipe>(val machine: HTMachineType, val serializer: RecipeSerializer<T>) : RecipeType<T> {
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
    private var changed: Boolean = true

    /**
     * 指定した[context]と[level]から最初に一致するレシピを返します。
     * @return 見つからなかった場合は[Result.failure]
     */
    fun getFirstRecipe(context: HTMachineRecipeContext, level: Level): RecipeHolder<T>? {
        // Check cache update
        this.reloadCache()
        // Find from cache
        return recipeCache.values.firstOrNull { holder: RecipeHolder<T> -> holder.value.matches(context, level) }
    }

    /**
     * キャッシュされたレシピの一覧を返します。
     */
    fun getAllRecipes(): List<RecipeHolder<T>> = recipeCache.values.toList()

    //    Reloading    //

    /**
     * レシピを更新するフラグを立てます。
     */
    fun setChanged() {
        changed = true
    }

    /**
     * レシピを現在のサーバに基づいて更新します。
     */
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

    /**
     * 指定した[manager]からレシピを更新します。
     */
    @Suppress("UNCHECKED_CAST")
    fun reloadCache(manager: RecipeManager) {
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
            ) { holder: RecipeHolder<out HTMachineRecipe> ->
                val recipe: T = holder.value as? T ?: return@HTMachineRecipesUpdatedEvent
                consumer(RecipeHolder(holder.id, recipe))
            }
            FORGE_BUS.post(event)
        }
    }

    override fun toString(): String = machine.serializedName
}
