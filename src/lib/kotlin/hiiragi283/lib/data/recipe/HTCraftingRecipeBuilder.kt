@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.CraftingRecipe

/**
 * クラフトレシピ向けの[HTRecipeBuilder]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTCraftingRecipeBuilder<out RECIPE : CraftingRecipe>(prefix: String) : HTRecipeBuilder<RECIPE>(prefix) {
    /**
     * レシピ本のカテゴリ
     */
    var category: RecipeCategory = RecipeCategory.MISC

    /**
     * レシピ本でのグループ
     */
    var group: String? = null

    fun bookInfo(): CraftingRecipe.CraftingBookInfo = bookInfo(category, group)

    @PublishedApi internal var result: ItemStackTemplate by HTDelegates.onceInitialize()

    operator fun ItemStackTemplate.unaryPlus() {
        result = this
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildTemplate(builderAction)
    }

    final override fun getPrimalId(): Identifier = result.item().getKeyOrThrow().identifier()
}
