package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.HTRecipePredicate
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.common.util.TriPredicate
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * Hiiragi Seriesで使用される[HTRecipePredicate]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTRecipePredicates {
    //    Single Input    //

    /**
     * 1種類の入力を判定する[HTRecipePredicate]の拡張インターフェースです。
     * @param INPUT [RecipeInput]を継承したクラス
     * @param INPUT_A 入力を判定するクラス
     */
    interface SingleInput<INPUT : RecipeInput, INPUT_A : Any> :
        HTRecipePredicate<INPUT>,
        Predicate<INPUT_A> {
        override fun test(input: INPUT_A): Boolean
    }

    /**
     * 1種類の液体を判定する[SingleInput]の拡張インターフェースです。
     */
    fun interface SingleFluid : SingleInput<HTSingleFluidRecipeInput, FluidInstance> {
        override fun matches(input: HTSingleFluidRecipeInput): Boolean = test(input.fluid)
    }

    /**
     * 1種類のアイテムを判定する[SingleInput]の拡張インターフェースです。
     */
    fun interface SingleItem : SingleInput<SingleRecipeInput, ItemInstance> {
        override fun matches(input: SingleRecipeInput): Boolean = test(input.item())
    }

    //    Double Input    //

    /**
     * 2種類の入力を判定する[HTRecipePredicate]の拡張インターフェースです。
     * @param INPUT [RecipeInput]を継承したクラス
     * @param INPUT_A 1番目の入力を判定するクラス
     * @param INPUT_B 2番目の入力を判定するクラス
     */
    interface DoubleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any> :
        HTRecipePredicate<INPUT>,
        BiPredicate<INPUT_A, INPUT_B> {
        override fun test(first: INPUT_A, second: INPUT_B): Boolean
    }

    /**
     * 1種類のアイテムと液体を判定する[DoubleInput]の拡張インターフェースです。
     */
    fun interface ItemAndFluid : DoubleInput<HTItemAndFluidRecipeInput, ItemInstance, FluidInstance> {
        override fun matches(input: HTItemAndFluidRecipeInput): Boolean {
            val (item: ItemInstance, fluid: FluidInstance) = input
            return test(item, fluid)
        }
    }

    /**
     * 2種類のアイテムを判定する[DoubleInput]の拡張インターフェースです。
     */
    fun interface DoubleItem : DoubleInput<RecipeInput, ItemInstance, ItemInstance> {
        override fun matches(input: RecipeInput): Boolean = input.size() >= 2 && test(input.getItem(0), input.getItem(1))
    }

    //    Triple Input    //

    /**
     * 3種類の入力を判定する[HTRecipePredicate]の拡張インターフェースです。
     * @param INPUT [RecipeInput]を継承したクラス
     * @param INPUT_A 1番目の入力を判定するクラス
     * @param INPUT_B 2番目の入力を判定するクラス
     * @param INPUT_C 3番目の入力を判定するクラス
     */
    interface TripleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, INPUT_C : Any> :
        HTRecipePredicate<INPUT>,
        TriPredicate<INPUT_A, INPUT_B, INPUT_C> {
        override fun test(first: INPUT_A, second: INPUT_B, third: INPUT_C): Boolean
    }

    /**
     * 3種類のアイテムを判定する[TripleItem]の拡張インターフェースです。
     */
    fun interface TripleItem : TripleInput<RecipeInput, ItemInstance, ItemInstance, ItemInstance> {
        override fun matches(input: RecipeInput): Boolean = input.size() >= 3 && test(input.getItem(0), input.getItem(1), input.getItem(2))
    }
}
