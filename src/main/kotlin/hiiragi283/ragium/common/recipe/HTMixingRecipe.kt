package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.recipe.HTSerializableRecipe
import hiiragi283.core.support.recipe.base.HTBasicItemOrFluidRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTMixingRecipe(
    val primary: HTItemIngredient,
    val secondary: HTItemIngredient?,
    val fluidIngredient: HTFluidIngredient,
    val result: Ior<HTItemResult, HTFluidResult>,
    override val progressData: HTProgressData,
) : HTRecipePredicates.TripleInput<HTMixingRecipe.Input, ItemStack, ItemStack, FluidStack>,
    HTRecipeFactories.DoubleItemAndFluid<Ior<ItemStack, FluidStack>>,
    HTProgressRecipe.Simple<HTMixingRecipe.Input>,
    HTSerializableRecipe<HTMixingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMixingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC
                        .listOrElement(1, 2)
                        .fieldOf(HTConst.ITEM_INGREDIENT)
                        .forGetter { listOfNotNull(it.primary, it.secondary) },
                    HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HTMixingRecipe::fluidIngredient),
                    HTBasicItemOrFluidRecipe.RESULT_CODEC.forGetter(HTMixingRecipe::result),
                    HTProgressData.CODEC.forGetter(HTMixingRecipe::progressData),
                ).apply(instance, ::HTMixingRecipe)
        }
    }

    constructor(
        itemIngredient: List<HTItemIngredient>,
        fluidIngredient: HTFluidIngredient,
        result: Ior<HTItemResult, HTFluidResult>,
        progressData: HTProgressData,
    ) : this(
        itemIngredient[0],
        itemIngredient.getOrNull(1),
        fluidIngredient,
        result,
        progressData,
    )

    override fun test(first: ItemStack, second: ItemStack, third: FluidStack): Boolean = primary.test(first) && (secondary?.test(second) ?: second.isEmpty) && fluidIngredient.test(third)

    override fun matches(input: Input): Boolean {
        val (firstItem: ItemStack, secondItem: ItemStack, fluid: FluidStack) = input
        return test(firstItem, secondItem, fluid)
    }

    override fun getMatchingStacks(first: ItemStack, second: ItemStack, third: FluidStack): Triple<ItemStack, ItemStack, FluidStack> = Triple(
        primary.getMatchingStack(first),
        secondary?.getMatchingStack(second) ?: ItemStack.EMPTY,
        fluidIngredient.getMatchingStack(third),
    )

    override fun assemble(firstInput: ItemStack, secondInput: ItemStack, thirdInput: FluidStack): Ior<ItemStack, FluidStack> = result.mapLeft { it.createOrEmpty() }.mapRight { it.create() }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING

    override fun isIncomplete(): Boolean {
        if (primary.isIncomplete()) return true
        if (secondary?.isIncomplete() ?: false) return true
        if (fluidIngredient.isIncomplete()) return true
        return result.getLeft()?.isIncomplete() ?: false
    }

    @JvmRecord
    data class Input(val firstItem: ItemStack, val secondItem: ItemStack, val fluid: FluidStack) : HTFluidRecipeInput {
        override fun getFluid(index: Int): FluidStack = when (index) {
            0 -> fluid
            else -> error("No fluid for index $index")
        }

        override fun getFluidSize(): Int = 2

        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> firstItem
            1 -> secondItem
            else -> error("No fluid for index $index")
        }

        override fun size(): Int = 2
    }
}
