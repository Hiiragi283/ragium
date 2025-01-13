package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [HTMachineRecipe]の判定に用いられるクラス
 * @param key 機械のキー
 * @param tier 機械のティア
 * @param itemInputs アイテムのインプットの一覧
 * @param fluidInputs 液体のインプットの一覧
 * @param catalyst 触媒スロットの[ItemStack]
 */
class HTMachineInput private constructor(
    val key: HTMachineKey,
    val tier: HTMachineTier,
    val itemInputs: List<ItemStack>,
    val fluidInputs: List<FluidStack>,
    val catalyst: ItemStack,
) : RecipeInput {
    companion object {
        /**
         * [HTMachineInput]を返します。
         */
        @JvmStatic
        fun create(key: HTMachineKey, tier: HTMachineTier, builderAction: Builder.() -> Unit): HTMachineInput {
            val itemInputs: MutableList<ItemStack> = mutableListOf()
            val fluidInputs: MutableList<FluidStack> = mutableListOf()
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

    /**
     * 指定した[index]から[FluidStack]を返します。
     */
    fun getFluidInSlot(index: Int): FluidStack = fluidInputs.getOrNull(index) ?: FluidStack.EMPTY

    //    RecipeInput    //

    override fun getItem(index: Int): ItemStack = itemInputs.getOrNull(index) ?: ItemStack.EMPTY

    override fun size(): Int = itemInputs.size

    override fun isEmpty(): Boolean {
        val bool1: Boolean = itemInputs.isEmpty() || itemInputs.all(ItemStack::isEmpty)
        val bool2: Boolean = fluidInputs.isEmpty() || fluidInputs.all(FluidStack::isEmpty)
        return bool1 && bool2 && catalyst.isEmpty
    }

    //    Builder    //

    class Builder(private val itemInputs: MutableList<ItemStack>, private val fluidInputs: MutableList<FluidStack>) {
        /**
         * 触媒スロットの[ItemStack]
         */
        var catalyst: ItemStack = ItemStack.EMPTY

        /**
         * アイテムのインプットを追加します。
         */
        fun add(stack: ItemStack): Builder = apply {
            itemInputs.add(stack)
        }

        /**
         * 液体のインプットを追加します。
         */
        fun add(fluid: Fluid, amount: Int): Builder = add(FluidStack(fluid, amount))

        /**
         * 液体のインプットを追加します。
         */
        fun add(stack: FluidStack): Builder = apply {
            fluidInputs.add(stack)
        }
    }
}
