package hiiragi283.ragium.api.registry

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTRecipesUpdatedEvent
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

/**
 * [RecipeSerializer]と[RecipeType]を束ねたクラス
 */
class HTDeferredRecipe<I : RecipeInput, R : Recipe<I>>(val name: String, val serializer: RecipeSerializer<R>) : RecipeType<R> {
    companion object {
        @JvmStatic
        private val SERIALIZER_REGISTER: DeferredRegister<RecipeSerializer<*>> =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

        @JvmStatic
        private val TYPE_REGISTER: DeferredRegister<RecipeType<*>> =
            DeferredRegister.create(Registries.RECIPE_TYPE, RagiumAPI.MOD_ID)

        @JvmStatic
        fun init(eventBus: IEventBus) {
            SERIALIZER_REGISTER.register(eventBus)
            TYPE_REGISTER.register(eventBus)
        }
    }

    constructor(
        name: String,
        codec: MapCodec<R>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, R>,
    ) : this(
        name,
        object : RecipeSerializer<R> {
            override fun codec(): MapCodec<R> = codec

            override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, R> = streamCodec
        },
    )

    private var recipeCache: Map<ResourceLocation, RecipeHolder<R>> = mapOf()
    private var changed: Boolean = true

    init {
        SERIALIZER_REGISTER.register(name, ::serializer)
        TYPE_REGISTER.register(name) { _: ResourceLocation -> this }
    }

    /**
     * 指定した[input]と[level]から最初に一致するレシピを返します。
     * @return 見つからなかった場合は[Result.failure]
     */
    fun getFirstRecipe(input: I, level: Level, lastRecipe: ResourceLocation?): RecipeHolder<R>? {
        // Check cache update
        this.reloadCache()
        // Find from cache
        var firstRecipe: RecipeHolder<R>? = null
        if (lastRecipe != null) {
            firstRecipe = recipeCache[lastRecipe]?.takeIf { it.value.matches(input, level) }
        }
        return firstRecipe ?: recipeCache.values
            .firstOrNull { holder: RecipeHolder<R> -> holder.value.matches(input, level) }
    }

    /**
     * キャッシュされたレシピの一覧を返します。
     */
    fun getAllRecipes(): List<RecipeHolder<R>> = recipeCache.values.toList()

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
        if (changed &&
            RagiumAPI.Companion
                .getInstance()
                .getCurrentSide()
                .isServer
        ) {
            RagiumAPI.Companion
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
            val consumer: (RecipeHolder<R>) -> Unit = { holder: RecipeHolder<R> ->
                put(holder.id, holder)
            }
            // Reload from RecipeManager
            manager.getAllRecipesFor(this@HTDeferredRecipe).forEach(consumer)
            // Reload from Event
            val access: RegistryAccess = RagiumAPI.Companion.getInstance().getRegistryAccess() ?: return@buildMap
            val event = HTRecipesUpdatedEvent(
                access,
                this@HTDeferredRecipe,
            ) { holder: RecipeHolder<*> ->
                val recipe: R = holder.value as? R ?: return@HTRecipesUpdatedEvent
                consumer(RecipeHolder(holder.id, recipe))
            }
            FORGE_BUS.post(event)
        }
    }

    override fun toString(): String = name
}
