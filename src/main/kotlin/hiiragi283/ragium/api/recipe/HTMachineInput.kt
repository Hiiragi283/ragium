package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

class HTMachineInput private constructor(
    val typeSize: HTMachineType.Size,
    val type: HTMachineType,
    val tier: HTMachineTier,
    private val itemInputs: List<ItemStack>,
    private val fluidInputs: List<ResourceAmount<FluidVariant>>,
    val catalyst: ItemStack,
) : RecipeInput {
    companion object {
        @JvmStatic
        fun create(type: HTMachineConvertible, tier: HTMachineTier, builderAction: Builder.() -> Unit): HTMachineInput {
            val itemInputs: MutableList<ItemStack> = mutableListOf()
            val fluidInputs: MutableMap<FluidVariant, Long> = mutableMapOf()

            val catalyst: ItemStack = Builder(itemInputs, fluidInputs).apply(builderAction).catalyst

            check(fluidInputs.size <= 2) { "Fluid inputs must be 2 or less!" }
            check(itemInputs.size <= 3) { "Item inputs must be 3 or less!" }
            val bool1: Boolean = fluidInputs.size == 2
            val bool2: Boolean = itemInputs.size == 3
            val bool3: Boolean = type.asMachine()[HTMachinePropertyKeys.RECIPE_SIZE] == HTMachineType.Size.LARGE
            val typeSize: HTMachineType.Size = when {
                bool1 || bool2 || bool3 -> HTMachineType.Size.LARGE
                else -> HTMachineType.Size.SIMPLE
            }
            return HTMachineInput(
                typeSize,
                type.asMachine(),
                tier,
                itemInputs,
                fluidInputs.map { (variant: FluidVariant, amount: Long) -> ResourceAmount(variant, amount) },
                catalyst,
            )
        }
    }

    fun getItem(index: Int): ItemStack = itemInputs.getOrNull(index) ?: ItemStack.EMPTY

    fun getFluid(index: Int): ResourceAmount<FluidVariant> = fluidInputs.getOrNull(index) ?: ResourceAmount(FluidVariant.blank(), 0)

    //    RecipeInput    //

    override fun getStackInSlot(slot: Int): ItemStack = itemInputs[slot]

    override fun getSize(): Int = itemInputs.size

    //    Builder    //

    class Builder(private val itemInputs: MutableList<ItemStack>, private val fluidInputs: MutableMap<FluidVariant, Long>) {
        var catalyst: ItemStack = ItemStack.EMPTY

        fun add(stack: ItemStack): Builder = apply {
            itemInputs.add(stack)
        }

        fun add(fluid: FluidVariant, amount: Long): Builder = apply {
            fluidInputs[fluid] = amount
        }

        fun add(resourceAmount: ResourceAmount<FluidVariant>): Builder = add(resourceAmount.resource, resourceAmount.amount)
    }
}
