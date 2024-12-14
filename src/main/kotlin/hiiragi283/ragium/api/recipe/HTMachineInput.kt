package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

class HTMachineInput private constructor(
    val key: HTMachineKey,
    val tier: HTMachineTier,
    val itemInputs: List<ItemStack>,
    private val fluidInputs: List<HTFluidVariantStack>,
    val catalyst: ItemStack,
) : RecipeInput {
    companion object {
        @JvmStatic
        fun create(key: HTMachineKey, tier: HTMachineTier, builderAction: Builder.() -> Unit): HTMachineInput {
            val itemInputs: MutableList<ItemStack> = mutableListOf()
            val fluidInputs: MutableList<HTFluidVariantStack> = mutableListOf()
            val catalyst: ItemStack = Builder(itemInputs, fluidInputs).apply(builderAction).catalyst
            return HTMachineInput(
                key,
                tier,
                itemInputs,
                fluidInputs,
                catalyst,
            )
        }
    }

    fun getItem(index: Int): ItemStack = itemInputs.getOrNull(index) ?: ItemStack.EMPTY

    fun getFluid(index: Int): HTFluidVariantStack = fluidInputs.getOrNull(index) ?: HTFluidVariantStack.EMPTY

    //    RecipeInput    //

    override fun getStackInSlot(slot: Int): ItemStack = itemInputs[slot]

    override fun getSize(): Int = itemInputs.size

    override fun isEmpty(): Boolean {
        val bool1: Boolean = itemInputs.isEmpty() || itemInputs.all(ItemStack::isEmpty)
        val bool2: Boolean = fluidInputs.isEmpty() || fluidInputs.all(HTFluidVariantStack::isEmpty)
        return bool1 && bool2 && catalyst.isEmpty
    }

    //    Builder    //

    class Builder(private val itemInputs: MutableList<ItemStack>, private val fluidInputs: MutableList<HTFluidVariantStack>) {
        var catalyst: ItemStack = ItemStack.EMPTY

        fun add(stack: ItemStack): Builder = apply {
            itemInputs.add(stack)
        }

        fun add(fluid: Fluid, amount: Long): Builder = add(FluidVariant.of(fluid), amount)

        fun add(variant: FluidVariant, amount: Long): Builder = add(HTFluidVariantStack(variant, amount))

        fun add(stack: HTFluidVariantStack): Builder = apply {
            fluidInputs.add(stack)
        }
    }
}
