package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.ingredient.HTCatalystOrIngredient
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.convert
import hiiragi283.lib.serialization.network.HTStreamCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidInstance

interface HTItemAndFluidToItemRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<ItemStack>,
    HTProgressRecipe<HTItemAndFluidRecipeInput> {

    open class Basic(
        val itemIngredient: HTCatalystOrIngredient,
        val fluidIngredient: HTFluidIngredient,
        val result: HTItemResult,
        override val progressData: HTProgressData,
    ) : HTItemAndFluidToItemRecipe,
        HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: (HTCatalystOrIngredient, HTFluidIngredient, HTItemResult, HTProgressData) -> RECIPE): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    Codec.mapEither(
                        Ingredient.CODEC.fieldOf(HTConstants.CATALYST),
                        HTItemIngredient.CODEC.fieldOf(HTConstants.ITEM_INGREDIENT),
                    ).convert().forGetter(Basic::itemIngredient),
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID_INGREDIENT).forGetter(Basic::fluidIngredient),
                    HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(Basic::result),
                    HTProgressData.CODEC.forGetter(Basic::progressData),
                ).apply(instance, factory)
            }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(factory: (HTCatalystOrIngredient, HTFluidIngredient, HTItemResult, HTProgressData) -> RECIPE): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTStreamCodecs.either(Ingredient.CONTENTS_STREAM_CODEC, HTItemIngredient.STREAM_CODEC),
                Basic::itemIngredient,
                HTFluidIngredient.STREAM_CODEC,
                Basic::fluidIngredient,
                HTItemResult.STREAM_CODEC,
                Basic::result,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory,
            )
        }

        override fun test(first: ItemInstance, second: FluidInstance): Boolean = itemIngredient.fold({ HTIngredientHelper.unwrap(first).let(it::test) }, { it.test(first) }) && fluidIngredient.test(second)

        override fun getRequiredAmount(first: ItemInstance, second: FluidInstance): Pair<Int, Int> = itemIngredient.fold({ 0 }, { it.getRequiredAmount(first) }) to fluidIngredient.getRequiredAmount(second)

        override fun apply(first: ItemInstance, second: FluidInstance): ItemStack = result.create()
    }
}
