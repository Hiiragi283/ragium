package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachine
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

class HTMachineInput private constructor(
    val key: HTMachineKey,
    val tier: HTMachineTier,
    private val itemInputs: List<ItemStack>,
    private val fluidInputs: List<Pair<Fluid, Long>>,
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
            return HTMachineInput(
                type.key,
                tier,
                itemInputs,
                fluidInputs.toList(),
                catalyst,
            )
        }
    }

    fun getItem(index: Int): ItemStack = itemInputs.getOrNull(index) ?: ItemStack.EMPTY

    fun getFluid(index: Int): Pair<Fluid, Long> = fluidInputs.getOrNull(index) ?: (Fluids.EMPTY to 0L)

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

        fun add(pair: Pair<Fluid, Long>): Builder = add(pair.first, pair.second)
    }
}
