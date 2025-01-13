package hiiragi283.ragium.api.extension

import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

//    Fluid    //

val Fluid.name: Component
    get() = fluidType.description

//    SizedFluidIngredient    //

fun SizedFluidIngredient.matches(stack: FluidStack): Boolean = ingredient().test(stack) && stack.amount >= amount()
