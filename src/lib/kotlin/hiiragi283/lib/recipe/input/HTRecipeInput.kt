package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * Hiiragi Seriesで使用される[HTFluidRecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
@JvmRecord
data class HTRecipeInput(val items: List<ItemStack>, val fluids: List<FluidStack>, val customKeys: Set<String>) :
    HTFluidRecipeInput {
    constructor(
        item: ItemStack,
        customKeys: Set<String>
    ) : this(item, listOf(), customKeys)

    constructor(
        item: ItemStack,
        fluids: List<FluidStack> = listOf(),
        customKeys: Set<String>
    ) : this(listOf(item), fluids, customKeys)

    constructor(
        fluid: FluidStack,
        customKeys: Set<String>
    ) : this(
        fluid,
        listOf(),
        customKeys
    )

    constructor(
        fluid: FluidStack,
        items: List<ItemStack> = listOf(),
        customKeys: Set<String>
    ) : this(
        items,
        listOf(fluid),
        customKeys
    )

    constructor(
        item: ItemStack,
        fluid: FluidStack,
        customKeys: Set<String>
    ) : this(
        listOf(item),
        listOf(fluid),
        customKeys
    )

    override val fluidSize: Int get() = fluids.size

    override fun getFluid(index: Int): FluidStack = fluids[index]

    override fun asFluidList(): List<FluidStack> = fluids

    override fun getItem(index: Int): ItemStack = items[index]

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = super.isEmpty() && customKeys.isEmpty()
}
