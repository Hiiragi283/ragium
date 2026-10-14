package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.HTRecipePredicate
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.input.getItemOrEmpty
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.common.util.TriPredicate
import net.neoforged.neoforge.fluids.FluidInstance
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * Hiiragi Seriesで使用される[HTRecipePredicate]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTRecipePredicates {
    //    Single Input    //

    /**
     * 1種類の入力を判定する[HTRecipePredicate]の拡張インターフェースです。
     * @param INPUT_A 入力を判定するクラス
     */
    interface SingleInput<INPUT_A : Any> :
        HTRecipePredicate<HTRecipeInput>,
        Predicate<INPUT_A> {
        override fun test(input: INPUT_A): Boolean
    }

    /**
     * 1種類の液体を判定する[SingleInput]の拡張インターフェースです。
     */
    fun interface SingleFluid : SingleInput<FluidInstance> {
        override fun matches(input: HTRecipeInput): Boolean = test(input.getFluidOrEmpty(0))
    }

    /**
     * 1種類のアイテムを判定する[SingleInput]の拡張インターフェースです。
     */
    fun interface SingleItem : SingleInput<ItemInstance> {
        override fun matches(input: HTRecipeInput): Boolean = test(input.getItemOrEmpty(0))
    }

    /**
     * 複数のアイテムを判定する[SingleInput]の拡張インターフェースです。
     * @since 26.1.11
     */
    fun interface MultiItem : SingleInput<List<ItemInstance>> {
        override fun matches(input: HTRecipeInput): Boolean = test(input.items)
    }

    //    Double Input    //

    /**
     * 2種類の入力を判定する[HTRecipePredicate]の拡張インターフェースです。
     * @param INPUT_A 1番目の入力を判定するクラス
     * @param INPUT_B 2番目の入力を判定するクラス
     */
    interface DoubleInput<INPUT_A : Any, INPUT_B : Any> :
        HTRecipePredicate<HTRecipeInput>,
        BiPredicate<INPUT_A, INPUT_B> {
        override fun test(first: INPUT_A, second: INPUT_B): Boolean
    }

    /**
     * 1種類のアイテムと液体を判定する[DoubleInput]の拡張インターフェースです。
     */
    fun interface ItemAndFluid : DoubleInput<ItemInstance, FluidInstance> {
        override fun matches(input: HTRecipeInput): Boolean = test(input.getItemOrEmpty(0), input.getFluidOrEmpty(0))
    }

    /**
     * 2種類のアイテムを判定する[DoubleInput]の拡張インターフェースです。
     */
    fun interface DoubleItem : DoubleInput<ItemInstance, ItemInstance> {
        override fun matches(input: HTRecipeInput): Boolean = test(input.getItemOrEmpty(0), input.getItemOrEmpty(1))
    }

    /**
     * 2種類の液体を判定する[DoubleInput]の拡張インターフェースです。
     * @since 26.1.5
     */
    fun interface DoubleFluid : DoubleInput<FluidInstance, FluidInstance> {
        override fun matches(input: HTRecipeInput): Boolean = test(input.getFluidOrEmpty(0), input.getFluidOrEmpty(1))
    }

    //    Triple Input    //

    /**
     * 3種類の入力を判定する[HTRecipePredicate]の拡張インターフェースです。
     * @param INPUT_A 1番目の入力を判定するクラス
     * @param INPUT_B 2番目の入力を判定するクラス
     * @param INPUT_C 3番目の入力を判定するクラス
     */
    interface TripleInput<INPUT_A : Any, INPUT_B : Any, INPUT_C : Any> :
        HTRecipePredicate<HTRecipeInput>,
        TriPredicate<INPUT_A, INPUT_B, INPUT_C> {
        override fun test(first: INPUT_A, second: INPUT_B, third: INPUT_C): Boolean
    }

    /**
     * 2種類のアイテムと1種類の液体を判定する[TripleInput]の拡張インターフェースです。
     * @since 26.1.8
     */
    fun interface DoubleItemAndFluid : TripleInput<ItemInstance, ItemInstance, FluidInstance> {
        override fun matches(input: HTRecipeInput): Boolean =
            test(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getFluidOrEmpty(0))
    }

    /**
     * 1種類のアイテムと2種類の液体を判定する[TripleInput]の拡張インターフェースです。
     * @since 26.1.9
     */
    fun interface ItemAndDoubleFluid : TripleInput<ItemInstance, FluidInstance, FluidInstance> {
        override fun matches(input: HTRecipeInput): Boolean =
            test(input.getItemOrEmpty(0), input.getFluidOrEmpty(0), input.getFluidOrEmpty(1))
    }

    /**
     * 3種類のアイテムを判定する[TripleInput]の拡張インターフェースです。
     */
    fun interface TripleItem : TripleInput<ItemInstance, ItemInstance, ItemInstance> {
        override fun matches(input: HTRecipeInput): Boolean =
            test(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getItemOrEmpty(2))
    }
}
