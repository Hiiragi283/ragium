package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.HTRecipeHolder
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeMap
import net.minecraft.world.item.crafting.RecipeType

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
    fun getAllRecipesN(context: Context): Sequence<HTRecipeHolder<RECIPE>>

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

        /**
         * @since 26.1.3
         */
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> byType(
            recipeType: RecipeType<RECIPE>
        ): Sequence<HTRecipeHolder<RECIPE>> = this.recipeMap.byType(recipeType).asSequence().map(::HTRecipeHolder)
    }
}
