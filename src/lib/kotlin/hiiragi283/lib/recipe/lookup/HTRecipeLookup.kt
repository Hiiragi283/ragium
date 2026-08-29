package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.RecipeKey
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeMap

/**
 * レシピの一覧を提供するインターフェースです。
 *
 * 参照 : [Mekanism - IMekanismRecipeTypeProvider](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/recipe/IMekanismRecipeTypeProvider.java)
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTRecipeLookup<out RECIPE> {
    /**
     * レシピの一覧を取得します。
     */
    fun getAllRecipes(context: Context): Map<RecipeKey, RECIPE>

    /**
     * @param recipeMap レシピの一覧
     * @param registries レジストリへのアクセス
     * @author Hiiragi Tsubasa
     * @since 26.1.1
     */
    @JvmRecord
    data class Context(val recipeMap: RecipeMap, val registries: RegistryAccess) {
        companion object {
            @JvmField
            val EMPTY = Context(RecipeMap.EMPTY, RegistryAccess.EMPTY)
        }

        constructor(level: ServerLevel) : this(level.recipeAccess().recipeMap(), level.registryAccess())
    }
}
