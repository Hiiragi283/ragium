package hiiragi283.lib.recipe.base

import com.mojang.datafixers.util.Function3
import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.input.getItemOrEmpty
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance
import java.util.function.BiFunction
import java.util.function.Function

/**
 * Hiiragi Seriesで使用される[HTRecipeFactory]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTRecipeFactories {
    //    Single Input    //

    interface SingleInput<INPUT_A : Any, OUTPUT : Any> :
        HTRecipeFactory<HTRecipeInput, OUTPUT>,
        Function<INPUT_A, OUTPUT> {
        override fun apply(input: INPUT_A): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(input: INPUT_A): INPUT_A

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(input: HTRecipeInput): INPUT_A
    }

    /**
     * 1種類の液体から完成品を作る[SingleInput]の拡張インターフェースです。
     */
    interface SingleFluidTo<OUTPUT : Any> : SingleInput<FluidInstance, OUTPUT> {
        override fun produce(input: HTRecipeInput): OUTPUT = apply(input.getFluidOrEmpty(0))

        override fun getMatchingStack(input: HTRecipeInput): FluidInstance = getMatchingStack(input.getFluidOrEmpty(0))
    }

    /**
     * 1種類のアイテムから完成品を作る[SingleInput]の拡張インターフェースです。
     */
    interface SingleItemTo<OUTPUT : Any> : SingleInput<ItemInstance, OUTPUT> {
        override fun produce(input: HTRecipeInput): OUTPUT = apply(input.getItemOrEmpty(0))

        override fun getMatchingStack(input: HTRecipeInput): ItemInstance = getMatchingStack(input.getItemOrEmpty(0))
    }

    //    Double Input    //

    interface DoubleInput<INPUT_A : Any, INPUT_B : Any, OUTPUT : Any> :
        HTRecipeFactory<HTRecipeInput, OUTPUT>,
        BiFunction<INPUT_A, INPUT_B, OUTPUT> {
        override fun apply(first: INPUT_A, second: INPUT_B): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(first: INPUT_A, second: INPUT_B): Pair<INPUT_A, INPUT_B>

        /**
         * 消費される入力を取得します。
         * @since 26.1.9
         */
        fun getMatchingStack(input: HTRecipeInput): Pair<INPUT_A, INPUT_B>
    }

    /**
     * 1種類のアイテムと液体から完成品を作る[DoubleInput]の拡張インターフェースです。
     */
    interface ItemAndFluid<OUTPUT : Any> : DoubleInput<ItemInstance, FluidInstance, OUTPUT> {
        override fun produce(input: HTRecipeInput): OUTPUT = apply(input.getItemOrEmpty(0), input.getFluidOrEmpty(0))

        override fun getMatchingStack(input: HTRecipeInput): Pair<ItemInstance, FluidInstance> =
            getMatchingStack(input.getItemOrEmpty(0), input.getFluidOrEmpty(0))
    }

    /**
     * 2種類のアイテムから完成品を作る[DoubleInput]の拡張インターフェースです。
     */
    interface DoubleItem<OUTPUT : Any> : DoubleInput<ItemInstance, ItemInstance, OUTPUT> {
        override fun produce(input: HTRecipeInput): OUTPUT = apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1))

        override fun getMatchingStack(input: HTRecipeInput): Pair<ItemInstance, ItemInstance> =
            getMatchingStack(input.getItemOrEmpty(0), input.getItemOrEmpty(1))
    }

    /**
     * 2種類の液体から完成品を作る[DoubleInput]の拡張インターフェースです。
     * @since 26.1.5
     */
    interface DoubleFluid<OUTPUT : Any> : DoubleInput<FluidInstance, FluidInstance, OUTPUT> {
        override fun produce(input: HTRecipeInput): OUTPUT = apply(input.getFluidOrEmpty(0), input.getFluidOrEmpty(1))

        override fun getMatchingStack(input: HTRecipeInput): Pair<FluidInstance, FluidInstance> =
            getMatchingStack(input.getFluidOrEmpty(0), input.getFluidOrEmpty(1))
    }

    //    Triple Input    //

    interface TripleInput<INPUT_A : Any, INPUT_B : Any, INPUT_C : Any, OUTPUT : Any> :
        HTRecipeFactory<HTRecipeInput, OUTPUT>,
        Function3<INPUT_A, INPUT_B, INPUT_C, OUTPUT> {
        override fun apply(first: INPUT_A, second: INPUT_B, third: INPUT_C): OUTPUT

        /**
         * 消費される入力を取得します。
         */
        fun getMatchingStack(first: INPUT_A, second: INPUT_B, third: INPUT_C): Triple<INPUT_A, INPUT_B, INPUT_C>

        /**
         * 消費される入力を取得します。
         * @since 26.1.9
         */
        fun getMatchingStack(input: HTRecipeInput): Triple<INPUT_A, INPUT_B, INPUT_C>
    }

    /**
     * 2種類のアイテムと1種類の液体から完成品を作る[TripleInput]の拡張インターフェースです。
     * @since 26.1.8
     */
    interface DoubleItemAndFluid<OUTPUT : Any> :
        TripleInput<ItemInstance, ItemInstance, FluidInstance, OUTPUT> {
        override fun produce(input: HTRecipeInput): OUTPUT =
            apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getFluidOrEmpty(0))

        override fun getMatchingStack(input: HTRecipeInput): Triple<ItemInstance, ItemInstance, FluidInstance> =
            getMatchingStack(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getFluidOrEmpty(0))
    }

    /**
     * 1種類のアイテムと2種類の液体から完成品を作る[TripleInput]の拡張インターフェースです。
     * @since 26.1.9
     */
    interface ItemAndDoubleFluid<OUTPUT : Any> :
        TripleInput<ItemInstance, FluidInstance, FluidInstance, OUTPUT> {
        override fun produce(input: HTRecipeInput): OUTPUT =
            apply(input.getItemOrEmpty(0), input.getFluidOrEmpty(0), input.getFluidOrEmpty(1))

        override fun getMatchingStack(input: HTRecipeInput): Triple<ItemInstance, FluidInstance, FluidInstance> =
            getMatchingStack(input.getItemOrEmpty(0), input.getFluidOrEmpty(0), input.getFluidOrEmpty(1))
    }

    /**
     * 3種類のアイテムから完成品を作る[TripleInput]の拡張インターフェースです。
     */
    interface TripleItem<OUTPUT : Any> : TripleInput<ItemInstance, ItemInstance, ItemInstance, OUTPUT> {
        override fun produce(input: HTRecipeInput): OUTPUT =
            apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getItemOrEmpty(2))

        override fun getMatchingStack(input: HTRecipeInput): Triple<ItemInstance, ItemInstance, ItemInstance> =
            getMatchingStack(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getItemOrEmpty(2))
    }
}
