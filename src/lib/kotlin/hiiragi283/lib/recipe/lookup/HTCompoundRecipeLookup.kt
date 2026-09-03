package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

/**
 * 複数の[HTRecipeLookup]を束ねた[HTRecipeLookup]の実装クラスです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTCompoundRecipeLookup<out RECIPE> private constructor(private val id: Identifier) : HTRecipeLookup<RECIPE> {
    companion object {
        @JvmStatic
        private val instances: MutableMap<Identifier, HTCompoundRecipeLookup<*>> = Object2ObjectLinkedOpenHashMap()

        /**
         * 新しい[HTCompoundRecipeLookup]のインスタンスを作成します。
         */
        @JvmStatic
        fun <RECIPE : Any> create(id: Identifier): HTCompoundRecipeLookup<RECIPE> {
            val recipeType = HTCompoundRecipeLookup<RECIPE>(id)
            check(instances.put(id, recipeType) == null) { "Duplicated recipe type $id" }
            return recipeType
        }

        /*fun clearCache(event: TagsUpdatedEvent.ServerDataLoad) {
            // Clear cached recipes
            instances.values.forEach(HTCompoundRecipeLookup<*>::clearCache)
            // Reload cached recipes (excluding potions)
            val contextMap: ContextMap = ContextMap.Builder()
                .withParameter(HTRecipeLookupContext.RECIPES, event.serverResources.recipeManager.recipeMap())
                .withParameter(HTRecipeLookupContext.REGISTRIES, event.registries)
                .create(HTRecipeLookupContext.CONTEXT)
            for (lookup: HTCompoundRecipeLookup<*> in instances.values) {
                lookup.getAllRecipes(contextMap)
            }
        }*/
    }

    private val lookups: MutableList<HTRecipeLookup<RECIPE>> = ObjectArrayList()
    /*private var cachedRecipes: Map<RecipeKey, RECIPE> = mapOf()

    private fun clearCache() {
        cachedRecipes = mapOf()
    }*/

    /**
     * レシピの一覧を追加します。
     */
    fun addRecipes(vararg recipes: Pair<RecipeKey, @UnsafeVariance RECIPE>) {
        addSubLookup { recipes.asSequence() }
    }

    /**
     * [HTRecipeLookup]を追加します。
     */
    fun addSubLookup(lookup: HTRecipeLookup<@UnsafeVariance RECIPE>) {
        check(lookup != this)
        this.lookups += lookup
    }

    override fun getAllRecipesN(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>> =
        lookups.asSequence().flatMap { it.getAllRecipesN(context) }

    override fun toString(): String = "HTCompoundRecipeLookup(id=$id)"
}

//    Extensions    //

/**
 * バニラの[RecipeType]からレシピの一覧を追加します。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE [HTCompoundRecipeLookup]のレシピのクラス
 * @param VANILLA_RECIPE バニラの[Recipe]を継承したクラス
 * @param recipeType バニラの[RecipeType]
 * @param transform [VANILLA_RECIPE]を[RECIPE]に変換するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <INPUT : RecipeInput, RECIPE : Any, VANILLA_RECIPE : Recipe<INPUT>> HTCompoundRecipeLookup<RECIPE>.fromRecipeType(
    recipeType: RecipeType<VANILLA_RECIPE>,
    transform: (VANILLA_RECIPE) -> RECIPE?
) {
    this.addSubLookup { context: HTRecipeLookup.Context ->
        context
            .byType(recipeType)
            .mapNotNull { (key: RecipeKey, recipe: VANILLA_RECIPE) ->
                val recipe1: RECIPE = transform(recipe) ?: return@mapNotNull null
                HTRecipeHolder(key, recipe1)
            }
    }
}
