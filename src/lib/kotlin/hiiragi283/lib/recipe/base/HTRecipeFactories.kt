package hiiragi283.lib.recipe.base

import com.mojang.datafixers.util.Function3
import hiiragi283.lib.recipe.HTRecipeFactory
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.input.getItemOrEmpty
import java.util.function.BiFunction
import java.util.function.Function
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * Hiiragi Seriesで使用される[HTRecipeFactory]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTRecipeFactories {
    //    Single Input    //

    interface SingleInput<INPUT : RecipeInput, INPUT_A : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        Function<INPUT_A, OUTPUT> {
        override fun apply(input: INPUT_A): OUTPUT
    }

    /**
     * 1種類の液体から完成品を作る[SingleInput]の拡張インターフェースです。
     */
    fun interface SingleFluidTo<OUTPUT : Any> : SingleInput<HTSingleFluidRecipeInput, FluidInstance, OUTPUT> {
        override fun produce(input: HTSingleFluidRecipeInput): OUTPUT = apply(input.fluid)
    }

    /**
     * 1種類のアイテムから完成品を作る[SingleInput]の拡張インターフェースです。
     */
    fun interface SingleItemTo<OUTPUT : Any> : SingleInput<SingleRecipeInput, ItemInstance, OUTPUT> {
        override fun produce(input: SingleRecipeInput): OUTPUT = apply(input.item())
    }

    //    Double Input    //

    interface DoubleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        BiFunction<INPUT_A, INPUT_B, OUTPUT> {
        override fun apply(first: INPUT_A, second: INPUT_B): OUTPUT
    }

    /**
     * 1種類のアイテムと液体から完成品を作る[DoubleInput]の拡張インターフェースです。
     */
    fun interface ItemAndFluid<OUTPUT : Any> : DoubleInput<HTItemAndFluidRecipeInput, ItemInstance, FluidInstance, OUTPUT> {
        override fun produce(input: HTItemAndFluidRecipeInput): OUTPUT = apply(input.item, input.fluid)
    }

    /**
     * 2種類のアイテムから完成品を作る[DoubleInput]の拡張インターフェースです。
     */
    fun interface DoubleItem<OUTPUT : Any> : DoubleInput<RecipeInput, ItemInstance, ItemInstance, OUTPUT> {
        override fun produce(input: RecipeInput): OUTPUT = apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1))
    }

    //    Triple Input    //

    interface TripleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, INPUT_C : Any, OUTPUT : Any> :
        HTRecipeFactory<INPUT, OUTPUT>,
        Function3<INPUT_A, INPUT_B, INPUT_C, OUTPUT> {
        override fun apply(first: INPUT_A, second: INPUT_B, third: INPUT_C): OUTPUT
    }

    /**
     * 3種類のアイテムから完成品を作る[TripleInput]の拡張インターフェースです。
     */
    fun interface TripleItem<OUTPUT : Any> : TripleInput<RecipeInput, ItemInstance, ItemInstance, ItemInstance, OUTPUT> {
        override fun produce(input: RecipeInput): OUTPUT = apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1), input.getItemOrEmpty(2))
    }
}
