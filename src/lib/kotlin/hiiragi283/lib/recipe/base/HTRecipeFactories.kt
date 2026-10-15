package hiiragi283.lib.recipe.base

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

    /**
     * 複数のアイテムから完成品を作る[SingleInput]の拡張インターフェースです。
     * @since 26.1.11
     */
    interface MultiItemTo<OUTPUT : Any> : SingleInput<List<ItemInstance>, OUTPUT> {
        override fun produce(input: HTRecipeInput): OUTPUT = apply(input.items)

        override fun getMatchingStack(input: HTRecipeInput): List<ItemInstance> = getMatchingStack(input.items)
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
}
