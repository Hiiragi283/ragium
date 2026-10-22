@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.builder.vanilla.HTCookingRecipeBuilder
import hiiragi283.lib.data.recipe.builder.vanilla.HTShapedRecipeBuilder
import hiiragi283.lib.data.recipe.builder.vanilla.HTShapelessRecipeBuilder
import hiiragi283.lib.data.recipe.builder.vanilla.HTSingleItemRecipeBuilder
import hiiragi283.lib.util.identity
import net.minecraft.core.Holder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.SmeltingRecipe
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
     * かまどレシピと溶鉱炉レシピを生成します。後者の処理時間は前者の半分で登録されます。
     */
    @JvmStatic
    inline fun smeltingAndBlasting(
        context: BootstrapContext<Recipe<*>>,
        builderAction: HTCookingRecipeBuilder.() -> Unit
    ): Sequence<Holder.Reference<Recipe<*>>> = sequenceOf(
        HTCookingRecipeBuilder(::SmeltingRecipe, identity(), HTConstants.SMELTING).apply(builderAction),
        HTCookingRecipeBuilder(::BlastingRecipe, { it / 2 }, HTConstants.BLASTING).apply(builderAction)
    ).map { it.save(context) }

    //    Other    //

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
