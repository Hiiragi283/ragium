package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

//    Fluid    //

fun IRecipeSlotBuilder.addFluidStack(stack: FluidStack?): IRecipeSlotBuilder {
    val stack1: FluidStack = stack ?: return this
    if (stack1.isEmpty) return this
    val amount: Long = stack1.amount.toLong()
    addFluidStack(stack1.fluid, amount)
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

//    HTItemResult    //

fun <T : IIngredientAcceptor<*>> T.addItemResult(recipe: HTMachineRecipeBase, index: Int): IIngredientAcceptor<*> = addItemStacks(
    recipe.itemResults
        .getOrNull(index)
        ?.getItem(ItemEnchantments.EMPTY)
        ?.let(::listOf) ?: listOf(),
)
