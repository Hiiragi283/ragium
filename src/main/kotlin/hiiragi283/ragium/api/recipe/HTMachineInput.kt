package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.extension.itemStack
import hiiragi283.ragium.api.extension.toResourceAmount
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

sealed interface HTMachineInput : RecipeInput {
    //    Simple    //

    class Simple private constructor(
        val firstItem: ResourceAmount<ItemVariant>,
        val secondItem: ResourceAmount<ItemVariant>,
        val firstFluid: ResourceAmount<FluidVariant>,
    ) : HTMachineInput {
        constructor(
            firstItem: ItemStack,
            secondItem: ItemStack = ItemStack.EMPTY,
            firstFluid: ResourceAmount<FluidVariant> = ResourceAmount(FluidVariant.blank(), 0),
        ) : this(
            firstItem.toResourceAmount(),
            secondItem.toResourceAmount(),
            firstFluid,
        )

        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> firstItem.itemStack
            1 -> secondItem.itemStack
            else -> ItemStack.EMPTY
        }

        override fun getSize(): Int = 2
    }

    //    Large    //

    class Large private constructor(
        val firstItem: ResourceAmount<ItemVariant>,
        val secondItem: ResourceAmount<ItemVariant>,
        val thirdItem: ResourceAmount<ItemVariant>,
        val firstFluid: ResourceAmount<FluidVariant>,
        val secondFluid: ResourceAmount<FluidVariant>,
    ) : HTMachineInput {
        constructor(
            firstItem: ItemStack,
            secondItem: ItemStack,
            thirdItem: ItemStack,
            firstFluid: ResourceAmount<FluidVariant>,
            secondFluid: ResourceAmount<FluidVariant>,
        ) : this(
            firstItem.toResourceAmount(),
            secondItem.toResourceAmount(),
            thirdItem.toResourceAmount(),
            firstFluid,
            secondFluid,
        )

        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> firstItem.itemStack
            1 -> secondItem.itemStack
            2 -> thirdItem.itemStack
            else -> ItemStack.EMPTY
        }

        override fun getSize(): Int = 3
    }
}
