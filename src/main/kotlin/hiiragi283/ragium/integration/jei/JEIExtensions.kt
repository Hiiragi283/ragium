package hiiragi283.ragium.integration.jei

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

//    SizedIngredient    //

val SizedIngredient.stacks: List<ItemStack>
    get() = items.toList()

val SizedFluidIngredient.stacks: List<FluidStack>
    get() = fluids.toList()
