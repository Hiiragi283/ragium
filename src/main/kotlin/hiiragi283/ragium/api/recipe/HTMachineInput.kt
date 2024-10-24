package hiiragi283.ragium.api.recipe

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

sealed interface HTMachineInput : RecipeInput {
    //    Simple    //

    class Simple(
        val firstItem: ItemStack,
        val secondItem: ItemStack = ItemStack.EMPTY,
        val firstFluid: ResourceAmount<FluidVariant> = ResourceAmount(FluidVariant.blank(), 0),
    ) : HTMachineInput {
        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> firstItem
            1 -> secondItem
            else -> ItemStack.EMPTY
        }

        override fun getSize(): Int = 2
    }

    //    Large    //

    class Large(
        val firstItem: ItemStack,
        val secondItem: ItemStack,
        val thirdItem: ItemStack,
        val firstFluid: ResourceAmount<FluidVariant>,
        val secondFluid: ResourceAmount<FluidVariant>,
    ) : HTMachineInput {
        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> firstItem
            1 -> secondItem
            2 -> thirdItem
            else -> ItemStack.EMPTY
        }

        override fun getSize(): Int = 3
    }
}
