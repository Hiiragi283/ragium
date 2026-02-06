package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class HTAssemblingRecipe(
    val itemIngredients: List<HTItemIngredient>,
    val fluidIngredient: HTFluidIngredient?,
    val circuit: Int,
    val result: HTItemResult,
    parameters: SubParameters,
) : HTProcessingRecipe<HTAssemblingRecipe.Input>(parameters) {
    companion object {
        const val MAX_ITEM_INPUTS = 9
    }

    constructor(
        itemIngredients: List<HTItemIngredient>,
        fluidIngredient: Optional<HTFluidIngredient>,
        circuit: Int,
        result: HTItemResult,
        subParameters: SubParameters,
    ) : this(
        itemIngredients,
        fluidIngredient.getOrNull(),
        circuit,
        result,
        subParameters,
    )

    override fun matches(input: Input, level: Level): Boolean {
        val bool1: Boolean = !HTShapelessRecipeHelper.shapelessMatch(itemIngredients, input.items).isEmpty()
        val bool2: Boolean = fluidIngredient?.test(input.fluid) ?: true
        val bool3: Boolean = this.circuit == input.type
        return bool1 && bool2 && bool3
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ASSEMBLING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ASSEMBLING.get()

    class Input(items: Map<HTItemResourceType, Int>, val fluid: FluidStack, val type: Int) :
        HTShapelessRecipeInput(items),
        HTFluidRecipeInput {
        override fun getFluid(index: Int): FluidStack = fluid

        override fun getFluidSize(): Int = 1

        override fun isEmpty(): Boolean = super<HTShapelessRecipeInput>.isEmpty() && fluid.isEmpty
    }
}
