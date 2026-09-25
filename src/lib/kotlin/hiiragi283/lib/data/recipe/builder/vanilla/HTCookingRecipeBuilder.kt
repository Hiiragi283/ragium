package hiiragi283.lib.data.recipe.builder.vanilla

import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Identity
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory

/**
 * 精錬レシピ向けの[HTVanillaRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - SimpleCookingRecipeBuilder][net.minecraft.data.recipes.SimpleCookingRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTCookingRecipeBuilder(
    private val factory: AbstractCookingRecipe.Factory<*>,
    private val timeOperator: Identity<Int>,
    prefix: String
) : HTSingleItemRecipeBuilder<AbstractCookingRecipe>(prefix) {
    /**
     * レシピ本のカテゴリ
     */
    var category: CookingBookCategory by HTDelegates.onceInitialize { CookingBookCategory.MISC }

    /**
     * 精錬時にもらえる経験値量
     */
    var exp: Float by HTDelegates.onceInitialize { 0f }

    /**
     * 精錬に必要な時間
     *
     * デフォルトは200 ticks = 10 sec
     */
    var time: Int by HTDelegates.onceInitialize { 20 * 10 }

    override fun createRecipe(): AbstractCookingRecipe = factory.create(
        commonInfo(true),
        AbstractCookingRecipe.CookingBookInfo(category, group),
        ingredient,
        result,
        exp,
        timeOperator(time)
    )
}
