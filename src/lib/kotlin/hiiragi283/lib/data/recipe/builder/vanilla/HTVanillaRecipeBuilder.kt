@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder.vanilla

import hiiragi283.lib.data.recipe.builder.HTRecipeBuilder
import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.HTDelegates
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Recipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * バニラのレシピ向けの[HTRecipeBuilder]の拡張クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
abstract class HTVanillaRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String) : HTRecipeBuilder<RECIPE>(prefix) {
    fun commonInfo(showNotification: Boolean): Recipe.CommonInfo =
        RecipeBuilder.createCraftingCommonInfo(showNotification)

    fun bookInfo(category: RecipeCategory): CraftingRecipe.CraftingBookInfo =
        RecipeBuilder.createCraftingBookInfo(category, group)

    /**
     * レシピ本でのグループ
     */
    var group: String by HTDelegates.onceInitialize { "" }

    // Result
    var result: ItemStackTemplate by HTDelegates.onceInitialize()

    operator fun ItemStackTemplate.unaryPlus() {
        result = this
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +ItemInstanceBuilder.buildTemplate(builderAction)
    }

    //    HTRecipeBuilder    //

    final override fun getRecipeId(): Identifier = result.getKeyOrThrow().identifier()
}
