package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.fluid.HTAmountedFluid
import hiiragi283.ragium.api.machine.HTMachine
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

class HTMachineInput private constructor(
    val typeSize: HTMachineType.Size,
    val key: HTMachineKey,
    val tier: HTMachineTier,
    private val itemInputs: List<ItemStack>,
    private val fluidInputs: List<HTAmountedFluid>,
    val catalyst: ItemStack,
) : RecipeInput {
    companion object {
        @JvmStatic
        fun create(type: HTMachine, tier: HTMachineTier, builderAction: Builder.() -> Unit): HTMachineInput {
            val itemInputs: MutableList<ItemStack> = mutableListOf()
            val fluidInputs: MutableMap<Fluid, Long> = mutableMapOf()

            val catalyst: ItemStack = Builder(itemInputs, fluidInputs).apply(builderAction).catalyst

            check(fluidInputs.size <= 2) { "Fluid inputs must be 2 or less!" }
            check(itemInputs.size <= 3) { "Item inputs must be 3 or less!" }
            val bool1: Boolean = fluidInputs.size == 2
            val bool2: Boolean = itemInputs.size == 3
            val bool3: Boolean = type.asProperties()[HTMachinePropertyKeys.RECIPE_SIZE] == HTMachineType.Size.LARGE
            val typeSize: HTMachineType.Size = when {
                bool1 || bool2 || bool3 -> HTMachineType.Size.LARGE
                else -> HTMachineType.Size.SIMPLE
            }
            return HTMachineInput(
                typeSize,
                type.key,
                tier,
                itemInputs,
                fluidInputs.map { (fluid: Fluid, amount: Long) -> HTAmountedFluid(fluid, amount) },
                catalyst,
            )
        }
    }

    fun getItem(index: Int): ItemStack = itemInputs.getOrNull(index) ?: ItemStack.EMPTY

    fun getFluid(index: Int): HTAmountedFluid = fluidInputs.getOrNull(index) ?: HTAmountedFluid.EMPTY

    //    RecipeInput    //

    override fun getStackInSlot(slot: Int): ItemStack = itemInputs[slot]

    override fun getSize(): Int = itemInputs.size

    //    Builder    //

    class Builder(private val itemInputs: MutableList<ItemStack>, private val fluidInputs: MutableMap<Fluid, Long>) {
        var catalyst: ItemStack = ItemStack.EMPTY

        fun add(stack: ItemStack): Builder = apply {
            itemInputs.add(stack)
        }

        fun add(fluid: Fluid, amount: Long): Builder = apply {
            fluidInputs[fluid] = amount
        }

        fun add(amounted: HTAmountedFluid): Builder = add(amounted.fluid, amounted.amount)
    }
}
