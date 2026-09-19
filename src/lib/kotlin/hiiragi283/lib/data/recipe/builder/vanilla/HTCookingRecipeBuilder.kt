@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder.vanilla

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.ConditionalExporter
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

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
    companion object {
        /**
         * かまどレシピのみを生成します。
         */
        @JvmStatic
        inline fun smelting(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCookingRecipeBuilder(::SmeltingRecipe, identity(), HTConstants.SMELTING).apply(builderAction)
        }

        /**
         * 溶鉱炉レシピのみを生成します。
         */
        @JvmStatic
        inline fun blasting(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCookingRecipeBuilder(::BlastingRecipe, identity(), HTConstants.BLASTING).apply(builderAction)
        }

        /**
         * 燻製器レシピのみを生成します。
         */
        @JvmStatic
        inline fun smoking(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCookingRecipeBuilder(::SmokingRecipe, identity(), HTConstants.SMOKING).apply(builderAction)
        }

        /**
         * かまどレシピと溶鉱炉レシピを生成します。後者の処理時間は前者の半分で登録されます。
         */
        @JvmStatic
        inline fun smeltingAndBlasting(
            exporter: ConditionalExporter<Recipe<*>>,
            builderAction: HTCookingRecipeBuilder.() -> Unit
        ) {
            sequenceOf(
                smelting(builderAction),
                HTCookingRecipeBuilder(::BlastingRecipe, { it / 2 }, HTConstants.BLASTING).apply(builderAction)
            ).forEach { it.save(exporter) }
        }

        /**
         * かまどレシピと燻製器レシピを生成します。後者の処理時間は前者の半分で登録されます。
         */
        @JvmStatic
        inline fun smeltingAndSmoking(
            exporter: ConditionalExporter<Recipe<*>>,
            builderAction: HTCookingRecipeBuilder.() -> Unit
        ) {
            sequenceOf(
                smelting(builderAction),
                HTCookingRecipeBuilder(::SmokingRecipe, { it / 2 }, HTConstants.SMOKING).apply(builderAction)
            ).forEach { it.save(exporter) }
        }
    }

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
