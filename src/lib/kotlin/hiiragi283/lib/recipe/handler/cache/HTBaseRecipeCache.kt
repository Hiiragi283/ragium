package hiiragi283.lib.recipe.handler.cache

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.right
import hiiragi283.lib.util.unwrap
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable

/**
 * [HTRecipeLookup]に基づいて，取得したレシピをキャッシュするクラスです。
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
abstract class HTBaseRecipeCache<RECIPE : Any>(protected val lookup: HTRecipeLookup<RECIPE>) : ValueIOSerializable {
    protected var lastRecipe: Either<RecipeKey, HTRecipeHolder<RECIPE>>? = null

    protected fun restoreLastRecipe(context: HTRecipeLookup.Context) {
        // 現在のキャッシュがレシピIDのみの場合，lookupからレシピを検索して更新
        lastRecipe?.onLeft { lastKey: RecipeKey ->
            lookup.getAllRecipes(context)
                .firstOrNull { (key: RecipeKey, _) -> lastKey == key }
                ?.right()
                .let { lastRecipe = it }
        }
    }

    protected fun getRecipeKey(): RecipeKey? = lastRecipe?.map(HTRecipeHolder<*>::key)?.unwrap()

    final override fun serialize(output: ValueOutput) {
        output.storeNullable(HTConstants.ID, Recipe.KEY_CODEC, getRecipeKey())
    }

    final override fun deserialize(input: ValueInput) {
        input.read(HTConstants.ID, Recipe.KEY_CODEC).ifPresent { lastRecipe = Either.Left(it) }
    }
}
