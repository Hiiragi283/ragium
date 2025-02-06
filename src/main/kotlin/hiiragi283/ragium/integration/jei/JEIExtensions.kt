package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

//    Item    //

fun createEmptyStack(name: Component): ItemStack {
    val stack = ItemStack(Blocks.BARRIER)
    stack.set(DataComponents.CUSTOM_NAME, name)
    return stack
}

fun createEmptyMaterialStack(prefix: HTTagPrefix, material: HTMaterialKey): ItemStack =
    createEmptyStack(Component.literal("Empty Matching Items: ${prefix.createText(material).string}"))

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

val SizedIngredient.stacks: List<ItemStack> get() = items.toList()

val SizedFluidIngredient.stacks: List<FluidStack> get() = fluids.toList()

fun <T : IIngredientAcceptor<*>> T.addIngredients(ingredient: SizedIngredient?): IIngredientAcceptor<*> =
    addIngredients(VanillaTypes.ITEM_STACK, ingredient?.stacks ?: listOf())

fun <T : IIngredientAcceptor<*>> T.addIngredients(ingredient: FluidIngredient?): IIngredientAcceptor<*> =
    addIngredients(NeoForgeTypes.FLUID_STACK, ingredient?.stacks?.toList() ?: listOf())
