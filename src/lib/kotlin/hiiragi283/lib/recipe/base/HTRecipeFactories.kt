package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.HTRecipeFactory
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.common.util.TriPredicate
import org.apache.commons.lang3.function.TriFunction
import java.util.function.BiFunction
import java.util.function.BiPredicate
import java.util.function.Function
import java.util.function.Predicate

/**
 * Hiiragi Seriesで使用される[HTRecipeFactory]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTRecipeFactories {
    //    Single Input    //

    interface SingleInput<INPUT : RecipeInput, INPUT_A : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        HTProgressRecipe.Single<INPUT, INPUT_A>,
        Predicate<INPUT_A>,
        Function<INPUT_A, OUTPUT> {
        override fun test(input: INPUT_A): Boolean

        override fun apply(input: INPUT_A): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(input: INPUT_A): INPUT_A
    }

    //    Double Input    //

    interface DoubleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        HTProgressRecipe.Double<INPUT, INPUT_A, INPUT_B>,
        BiPredicate<INPUT_A, INPUT_B>,
        BiFunction<INPUT_A, INPUT_B, OUTPUT> {
        override fun test(first: INPUT_A, second: INPUT_B): Boolean

        override fun apply(first: INPUT_A, second: INPUT_B): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(first: INPUT_A, second: INPUT_B): Pair<INPUT_A, INPUT_B>
    }

    /**
     * @since 26.1.13
     */
    interface CatalysedSingleInput<INPUT : RecipeInput, INPUT_A : Any, CATALYST : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        HTProgressRecipe.Double<INPUT, INPUT_A, CATALYST>,
        BiPredicate<INPUT_A, CATALYST>,
        BiFunction<INPUT_A, CATALYST, OUTPUT> {
        override fun test(input: INPUT_A, catalyst: CATALYST): Boolean

        override fun apply(input: INPUT_A, catalyst: CATALYST): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(input: INPUT_A): INPUT_A
    }

    //    Triple Input    //

    interface TripleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, INPUT_C : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        TriPredicate<INPUT_A, INPUT_B, INPUT_C>,
        HTProgressRecipe.Triple<INPUT, INPUT_A, INPUT_B, INPUT_C>,
        TriFunction<INPUT_A, INPUT_B, INPUT_C, OUTPUT> {
        override fun test(first: INPUT_A, second: INPUT_B, third: INPUT_C): Boolean

        override fun apply(first: INPUT_A, second: INPUT_B, third: INPUT_C): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(first: INPUT_A, second: INPUT_B, third: INPUT_C): Triple<INPUT_A, INPUT_B, INPUT_C>
    }
}
