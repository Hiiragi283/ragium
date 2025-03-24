package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient
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

fun IRecipeSlotBuilder.addIngredients(ingredient: SizedIngredient?): IRecipeSlotBuilder =
    addIngredients(VanillaTypes.ITEM_STACK, ingredient?.items?.toList() ?: listOf())

fun IRecipeSlotBuilder.addIngredients(ingredient: FluidIngredient?): IRecipeSlotBuilder =
    addIngredients(NeoForgeTypes.FLUID_STACK, ingredient?.stacks?.toList() ?: listOf())

//    Output    //

fun IRecipeSlotBuilder.addOutput(output: HTItemOutput?): IRecipeSlotBuilder = addItemStack(output?.get() ?: ItemStack.EMPTY)

fun IRecipeSlotBuilder.addOutput(output: HTFluidOutput?): IRecipeSlotBuilder = addFluidStack(output?.get())
