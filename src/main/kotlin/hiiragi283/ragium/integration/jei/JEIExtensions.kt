package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.item.HTItemStackBuilder
import hiiragi283.ragium.api.recipe.base.HTFluidOutputRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTItemOutput
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

//    Fluid    //

fun IRecipeSlotBuilder.addFluidStack(stack: FluidStack?): IRecipeSlotBuilder {
    val stack1: FluidStack = stack ?: return this
    if (stack1.isEmpty) return this
    val amount: Long = stack1.amount.toLong()
    addFluidStack(stack1.fluid, amount, stack1.componentsPatch)
    return setFluidRenderer(amount, false, 16, 16)
}

fun IRecipeSlotBuilder.addIngredients(ingredient: SizedFluidIngredient?): IRecipeSlotBuilder {
    val ingredient1: SizedFluidIngredient = ingredient ?: return this
    val amount: Long = ingredient.amount().toLong()
    addIngredients(NeoForgeTypes.FLUID_STACK, ingredient1.stacks)
    return setFluidRenderer(amount, false, 16, 16)
}

//    SizedIngredient    //

val SizedFluidIngredient.stacks: List<FluidStack> get() = fluids.toList()

fun <T : IIngredientAcceptor<*>> T.addIngredients(ingredient: HTItemIngredient?): IIngredientAcceptor<*> =
    addIngredients(VanillaTypes.ITEM_STACK, ingredient?.matchingStacks ?: listOf())

fun <T : IIngredientAcceptor<*>> T.addIngredients(ingredient: FluidIngredient?): IIngredientAcceptor<*> =
    addIngredients(NeoForgeTypes.FLUID_STACK, ingredient?.stacks?.toList() ?: listOf())

//    HTItemOutput    //

fun IRecipeSlotBuilder.addItemOutput(output: HTItemOutput?): IRecipeSlotBuilder {
    var stack: ItemStack = output?.get() ?: return this
    if (stack.isEmpty) {
        stack = HTItemStackBuilder(Items.BARRIER, output.count)
            .put(DataComponents.CUSTOM_NAME, Component.literal("Empty Output: #${output.rawId}"))
            .build()
    }
    return addItemStack(stack)
}

fun IRecipeSlotBuilder.addItemOutput(recipe: HTFluidOutputRecipe, index: Int): IRecipeSlotBuilder =
    addItemOutput(recipe.itemOutputs.getOrNull(index))

fun IRecipeSlotBuilder.addFluidOutput(recipe: HTFluidOutputRecipe, index: Int): IRecipeSlotBuilder =
    addFluidStack(recipe.fluidOutputs.getOrNull(index)?.get())
