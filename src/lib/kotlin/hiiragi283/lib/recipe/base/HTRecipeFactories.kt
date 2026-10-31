package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.input.asList
import hiiragi283.lib.recipe.input.getItemOrEmpty
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidInstance
import org.apache.commons.lang3.function.TriFunction
import java.util.function.BiFunction
import java.util.function.Function

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
        Function<INPUT_A, OUTPUT> {
        override fun apply(input: INPUT_A): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(input: INPUT_A): INPUT_A
    }

    /**
     * 1種類の液体から完成品を作る[SingleInput]の拡張インターフェースです。
     */
    interface SingleFluidTo<OUTPUT : Any> : SingleInput<HTSingleFluidRecipeInput, FluidInstance, OUTPUT> {
        override fun produce(input: HTSingleFluidRecipeInput): OUTPUT = apply(input.fluid)

        override fun getProgressData(input: HTSingleFluidRecipeInput): HTProgressData = getProgressData(input.fluid)
    }

    /**
     * 1種類のアイテムから完成品を作る[SingleInput]の拡張インターフェースです。
     */
    interface SingleItemTo<OUTPUT : Any> : SingleInput<SingleRecipeInput, ItemInstance, OUTPUT> {
        override fun produce(input: SingleRecipeInput): OUTPUT = apply(input.item())

        override fun getProgressData(input: SingleRecipeInput): HTProgressData = getProgressData(input.item())
    }

    /**
     * 複数のアイテムから完成品を作る[SingleInput]の拡張インターフェースです。
     * @since 26.1.11
     */
    interface MultiItemTo<OUTPUT : Any> : SingleInput<RecipeInput, List<ItemInstance>, OUTPUT> {
        override fun produce(input: RecipeInput): OUTPUT = apply(input.asList())

        override fun getProgressData(input: RecipeInput): HTProgressData = getProgressData(input.asList())
    }

    //    Double Input    //

    interface DoubleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        HTProgressRecipe.Double<INPUT, INPUT_A, INPUT_B>,
        BiFunction<INPUT_A, INPUT_B, OUTPUT> {
        override fun apply(first: INPUT_A, second: INPUT_B): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(first: INPUT_A, second: INPUT_B): Pair<INPUT_A, INPUT_B>
    }

    /**
     * 1種類のアイテムと液体から完成品を作る[DoubleInput]の拡張インターフェースです。
     */
    interface ItemAndFluid<OUTPUT : Any> : DoubleInput<HTItemAndFluidRecipeInput, ItemInstance, FluidInstance, OUTPUT> {
        override fun produce(input: HTItemAndFluidRecipeInput): OUTPUT = apply(input.item, input.fluid)

        override fun getProgressData(input: HTItemAndFluidRecipeInput): HTProgressData = getProgressData(input.item, input.fluid)
    }

    /**
     * 2種類のアイテムから完成品を作る[DoubleInput]の拡張インターフェースです。
     */
    interface DoubleItem<OUTPUT : Any> : DoubleInput<RecipeInput, ItemInstance, ItemInstance, OUTPUT> {
        override fun produce(input: RecipeInput): OUTPUT = apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1))

        override fun getProgressData(input: RecipeInput): HTProgressData = getProgressData(input.getItemOrEmpty(0), input.getItemOrEmpty(1))
    }

    //    Triple Input    //

    interface TripleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, INPUT_C : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        HTProgressRecipe.Triple<INPUT, INPUT_A, INPUT_B, INPUT_C>,
        TriFunction<INPUT_A, INPUT_B, INPUT_C, OUTPUT> {
        override fun apply(first: INPUT_A, second: INPUT_B, third: INPUT_C): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(first: INPUT_A, second: INPUT_B, third: INPUT_C): Triple<INPUT_A, INPUT_B, INPUT_C>
    }
}
