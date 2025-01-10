package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

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
    val fluidInputs: List<HTFluidVariantStack>,
    val catalyst: ItemStack,
) : RecipeInput {
    companion object {
        /**
         * [HTMachineInput]を返します。
         */
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

    /**
     * 指定した[index]から[HTFluidVariantStack]を返します。
     */
    fun getFluidInSlot(index: Int): HTFluidVariantStack = fluidInputs.getOrNull(index) ?: HTFluidVariantStack.EMPTY

    //    RecipeInput    //

    override fun getStackInSlot(slot: Int): ItemStack = itemInputs.getOrNull(slot) ?: ItemStack.EMPTY

    override fun getSize(): Int = itemInputs.size

    override fun isEmpty(): Boolean {
        val bool1: Boolean = itemInputs.isEmpty() || itemInputs.all(ItemStack::isEmpty)
        val bool2: Boolean = fluidInputs.isEmpty() || fluidInputs.all(HTFluidVariantStack::isEmpty)
        return bool1 && bool2 && catalyst.isEmpty
    }

    //    Builder    //

    class Builder(private val itemInputs: MutableList<ItemStack>, private val fluidInputs: MutableList<HTFluidVariantStack>) {
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
        fun add(fluid: Fluid, amount: Long): Builder = add(HTFluidVariantStack(fluid, amount))

        /**
         * 液体のインプットを追加します。
         */
        fun add(variant: FluidVariant, amount: Long): Builder = add(HTFluidVariantStack(variant, amount))

        /**
         * 液体のインプットを追加します。
         */
        fun add(stack: HTFluidVariantStack): Builder = apply {
            fluidInputs.add(stack)
        }
    }
}
