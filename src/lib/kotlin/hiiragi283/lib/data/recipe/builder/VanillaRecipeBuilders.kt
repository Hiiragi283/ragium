@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.ConditionalExporter
import hiiragi283.lib.data.recipe.builder.vanilla.HTCookingRecipeBuilder
import hiiragi283.lib.data.recipe.builder.vanilla.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.builder.vanilla.HTShapelessRecipeBuilder
import hiiragi283.lib.data.recipe.builder.vanilla.HTSingleItemRecipeBuilder
import hiiragi283.lib.data.recipe.builder.vanilla.HTSmithingRecipeBuilder
import hiiragi283.lib.util.identity
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.item.crafting.StonecutterRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
data object VanillaRecipeBuilders {
    //    Crafting    //

    /**
     * 定形クラフトレシピを生成します。
     */
    @JvmStatic
    inline fun shaped(builderAction: HTShapedRecipeBuilder.() -> Unit): HTShapedRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTShapedRecipeBuilder().apply(builderAction)
    }

    /**
     * 不定形クラフトレシピを生成します。
     */
    @JvmStatic
    inline fun shapeless(builderAction: HTShapelessRecipeBuilder.() -> Unit): HTShapelessRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTShapelessRecipeBuilder().apply(builderAction)
    }

    //    Cooking    //

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

    //    Other    //

    /**
     * 鍛冶台レシピを生成します。
     */
    @JvmStatic
    inline fun smithing(builderAction: HTSmithingRecipeBuilder.() -> Unit): HTSmithingRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSmithingRecipeBuilder().apply(builderAction)
    }

    /**
     * 石切台レシピを生成します。
     */
    @JvmStatic
    inline fun stonecutting(builderAction: Stonecutting.() -> Unit): Stonecutting {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return Stonecutting().apply(builderAction)
    }

    /**
     * 石切台レシピ向けの[HTSingleItemRecipeBuilder]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.7
     */
    class Stonecutting : HTSingleItemRecipeBuilder<StonecutterRecipe>("stonecutting") {
        override fun createRecipe(): StonecutterRecipe = StonecutterRecipe(commonInfo(true), ingredient, result)
    }
}
