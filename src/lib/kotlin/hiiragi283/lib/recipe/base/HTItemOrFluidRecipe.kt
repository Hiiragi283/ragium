package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.util.Ior
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance

interface HTItemOrFluidRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<HTItemAndFluidResult>,
    HTProgressRecipe<HTItemAndFluidRecipeInput> {

    open class Basic(
        val ingredient: Ior<HTItemIngredient, HTFluidIngredient>,
        val result: Ior<HTItemResult, HTFluidResult>,
        override val progressData: HTProgressData,
    ) : HTItemOrFluidRecipe,
        HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
        companion object {
            @JvmField
            val INGREDIENT_CODEC: MapCodec<Ior<HTItemIngredient, HTFluidIngredient>> = HTCodecs
                .ior(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.ITEM_INGREDIENT),
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID_INGREDIENT),
                )

            @JvmField
            val RESULT_CODEC: MapCodec<Ior<HTItemResult, HTFluidResult>> = HTCodecs
                .ior(
                    HTItemResult.CODEC.fieldOf(HTConstants.ITEM_RESULT),
                    HTFluidResult.CODEC.fieldOf(HTConstants.FLUID_RESULT),
                )

            @JvmStatic
            fun <RECIPE : Basic> codec(factory: (Ior<HTItemIngredient, HTFluidIngredient>, Ior<HTItemResult, HTFluidResult>, HTProgressData) -> RECIPE): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    INGREDIENT_CODEC.forGetter(Basic::ingredient),
                    RESULT_CODEC.forGetter(Basic::result),
                    HTProgressData.CODEC.forGetter(Basic::progressData),
                ).apply(instance, factory)
            }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)
        }

        override fun test(first: TypedInstance<Item>, second: TypedInstance<Fluid>): Boolean = ingredient.fold(
            { it.test(first) && HTIngredientHelper.isEmpty(second) },
            { it.test(second) && HTIngredientHelper.isEmpty(first) },
            { item: HTItemIngredient, fluid: HTFluidIngredient -> item.test(first) && fluid.test(second) },
        )

        override fun getRequiredAmount(first: TypedInstance<Item>, second: TypedInstance<Fluid>): Pair<Int, Int> = ingredient
            .mapLeft { it.getRequiredAmount(first) }
            .mapRight { it.getRequiredAmount(second) }
            .toPair()
            .let { (item: Int?, fluid: Int?) -> (item ?: 0) to (fluid ?: 0) }

        override fun apply(first: ItemInstance, second: FluidInstance): HTItemAndFluidResult = result.mapLeft { it.create() }.mapRight { it.create() }.let(::HTItemAndFluidResult)
    }
}
