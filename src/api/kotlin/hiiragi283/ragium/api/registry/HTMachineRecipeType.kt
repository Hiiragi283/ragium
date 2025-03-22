package hiiragi283.ragium.api.registry

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTRecipesUpdatedEvent
import hiiragi283.ragium.api.extension.toRegistryStream
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

class HTMachineRecipeType(val name: String, private val factory: (HTRecipeDefinition) -> DataResult<out HTMachineRecipe>) :
    RecipeType<HTMachineRecipe>,
    RecipeSerializer<HTMachineRecipe> {
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

    init {
        SERIALIZER_REGISTER.register(name) { _: ResourceLocation -> this }
        TYPE_REGISTER.register(name) { _: ResourceLocation -> this }
    }

    private var recipeCache: Map<ResourceLocation, RecipeHolder<HTMachineRecipe>> = mapOf()
    private var changed: Boolean = true

    fun createRecipe(definition: HTRecipeDefinition): DataResult<out HTMachineRecipe> = factory(definition)

    /**
     * 指定した[input]と[level]から最初に一致するレシピを返します。
     * @return 見つからなかった場合は[Result.failure]
     */
    fun getFirstRecipe(input: HTMachineInput, level: Level, lastRecipe: ResourceLocation?): RecipeHolder<HTMachineRecipe>? {
        // キャッシュの更新を促す
        this.reloadCache()
        // 更新されたキャッシュから取得する
        var firstRecipe: RecipeHolder<HTMachineRecipe>? = null
        if (lastRecipe != null) {
            firstRecipe = recipeCache[lastRecipe]?.takeIf { it.value.matches(input, level) }
        }
        return firstRecipe ?: recipeCache.values
            .firstOrNull { holder: RecipeHolder<HTMachineRecipe> -> holder.value.matches(input, level) }
    }

    /**
     * キャッシュされたレシピの一覧を返します。
     */
    fun getAllRecipes(): List<RecipeHolder<HTMachineRecipe>> = recipeCache.values.toList()

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
            RagiumAPI
                .getInstance()
                .getCurrentSide()
                .isServer
        ) {
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
            val consumer: (RecipeHolder<HTMachineRecipe>) -> Unit = { holder: RecipeHolder<HTMachineRecipe> ->
                put(holder.id, holder)
            }
            // RecipeManagerから再読み込み
            manager.getAllRecipesFor(this@HTMachineRecipeType).forEach(consumer)
            // Eventから再読み込み
            val access: RegistryAccess = RagiumAPI.getInstance().getRegistryAccess() ?: return@buildMap
            val event = HTRecipesUpdatedEvent(
                access,
                this@HTMachineRecipeType,
            ) { holder: RecipeHolder<*> ->
                val recipe: HTMachineRecipe = holder.value as? HTMachineRecipe ?: return@HTRecipesUpdatedEvent
                consumer(RecipeHolder(holder.id, recipe))
            }
            FORGE_BUS.post(event)
        }
    }

    override fun toString(): String = name

    //    RecipeSerializer    //

    private val codec: MapCodec<HTMachineRecipe> =
        HTRecipeDefinition.CODEC.flatXmap(factory, HTMachineRecipe::getDefinition)

    override fun codec(): MapCodec<HTMachineRecipe> = codec

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, HTMachineRecipe> = codec.toRegistryStream()
}
