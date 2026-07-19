package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.getMatchingStack
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.codec.convert
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.Option
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.api.recipe.result.HTChemicalResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTChemicalReactingRecipe(
    val primary: HTFluidIngredient,
    val secondary: Ior<HTFluidIngredient, Ingredient>,
    val fluidResults: List<HTFluidResult>,
    val itemResult: Option<HTItemResult>,
    override val progressData: HTProgressData,
) : HTRecipePredicates.TripleInput<HTChemicalReactingRecipe.Input, ItemStack, FluidStack, FluidStack>,
    HTRecipeFactories.ItemAndDoubleFluid<HTChemicalResult>,
    HTProgressRecipe.Simple<HTChemicalReactingRecipe.Input>,
    HTSerializableRecipe<HTChemicalReactingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTChemicalReactingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTFluidIngredient.CODEC
                        .fieldOf("primary")
                        .forGetter(HTChemicalReactingRecipe::primary),
                    HTCodecs
                        .ior(
                            HTFluidIngredient.CODEC.fieldOf("secondary"),
                            HTCodecs.INGREDIENT.fieldOf(HTConst.CATALYST),
                        ).forGetter(HTChemicalReactingRecipe::secondary),
                    HTFluidResult.CODEC
                        .listOrElement(1..2)
                        .fieldOf(HTConst.FLUID_RESULT)
                        .forGetter(HTChemicalReactingRecipe::fluidResults),
                    HTItemResult.CODEC.optionalFieldOf(HTConst.ITEM_RESULT).convert().forGetter(HTChemicalReactingRecipe::itemResult),
                    HTProgressData.CODEC.forGetter { it.progressData },
                ).apply(instance, ::HTChemicalReactingRecipe)
        }
    }

    override fun matches(input: Input): Boolean {
        val (catalyst: ItemStack, firstFluid: FluidStack, secondFluid: FluidStack) = input
        return test(catalyst, firstFluid, secondFluid)
    }

    override fun test(first: ItemStack, second: FluidStack, third: FluidStack): Boolean {
        if (!primary.test(second)) return false
        return secondary.merge(
            { it.test(third) && first.isEmpty },
            { it.test(first) && third.isEmpty },
            { secondary: Boolean, catalyst1: Boolean -> catalyst1 && secondary },
        )
    }

    override fun getMatchingStacks(first: ItemStack, second: FluidStack, third: FluidStack): Triple<ItemStack, FluidStack, FluidStack> = Triple(
        secondary.getRight()?.getMatchingStack(first) ?: ItemStack.EMPTY,
        primary.getMatchingStack(second),
        secondary.getLeft()?.getMatchingStack(third) ?: FluidStack.EMPTY,
    )

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack, thirdInput: FluidStack): HTChemicalResult = HTChemicalResult.create(fluidResults, itemResult)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CHEMICAL_REACTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CHEMICAL_REACTING

    override fun isIncomplete(): Boolean {
        if (primary.isIncomplete()) return true
        if (secondary.merge(HTFluidIngredient::isIncomplete, Ingredient::hasNoItems) { fluid: Boolean, item: Boolean -> fluid || item }) return true
        return itemResult.fold({ false }, HTItemResult::isIncomplete)
    }

    @JvmRecord
    data class Input(val catalyst: ItemStack, val firstFluid: FluidStack, val secondFluid: FluidStack) : HTFluidRecipeInput {
        override fun getFluid(index: Int): FluidStack = when (index) {
            0 -> firstFluid
            1 -> secondFluid
            else -> error("No fluid for index $index")
        }

        override fun getFluidSize(): Int = 2

        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> catalyst
            else -> error("No item for index $index")
        }

        override fun size(): Int = 1
    }
}
