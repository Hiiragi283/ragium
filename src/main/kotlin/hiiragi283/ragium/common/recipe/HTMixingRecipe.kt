package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTFluidRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTMixingRecipe(
    val primary: HTItemIngredient,
    val secondary: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    val result: Ior<HTItemResult, HTFluidResult>,
    override val time: Int,
) : HTProcessingRecipe.Serializable<HTMixingRecipe.Input>,
    HTFluidRecipe<HTMixingRecipe.Input> {
    constructor(
        itemIngredient: List<HTItemIngredient>,
        fluidIngredient: HTFluidIngredient,
        result: Ior<HTItemResult, HTFluidResult>,
        time: Int,
    ) : this(
        itemIngredient[0],
        itemIngredient[1],
        fluidIngredient,
        result,
        time,
    )

    override fun test(input: Input): Boolean {
        val (firstItem: ItemStack, secondItem: ItemStack, fluid: FluidStack) = input
        return primary.test(firstItem) && secondary.test(secondItem) && fluidIngredient.test(fluid)
    }

    override fun assemble(input: Input, preview: Boolean): ItemStack = result.getLeft()?.getOrEmpty(preview) ?: ItemStack.EMPTY

    override fun assembleFluid(input: Input): FluidStack = result.getRight()?.getOrEmpty() ?: FluidStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()

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
