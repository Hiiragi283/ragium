package hiiragi283.lib.recipe.input

import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

/**
 * Hiiragi Seriesで使用される[HTFluidRecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
@JvmRecord
data class HTRecipeInput(val items: List<ItemInstance>, val fluids: List<FluidInstance>, val customKeys: Set<String>) :
    HTFluidRecipeInput {
    constructor(
        item: ItemInstance,
        customKeys: Set<String>
    ) : this(item, listOf(), customKeys)

    constructor(
        item: ItemInstance,
        fluids: List<FluidInstance> = listOf(),
        customKeys: Set<String>
    ) : this(listOf(item), fluids, customKeys)

    constructor(
        fluid: FluidInstance,
        customKeys: Set<String>
    ) : this(
        fluid,
        listOf(),
        customKeys
    )

    constructor(
        fluid: FluidInstance,
        items: List<ItemInstance> = listOf(),
        customKeys: Set<String>
    ) : this(
        items,
        listOf(fluid),
        customKeys
    )

    constructor(
        item: ItemInstance,
        fluid: FluidInstance,
        customKeys: Set<String>
    ) : this(
        listOf(item),
        listOf(fluid),
        customKeys
    )

    override val fluidSize: Int get() = fluids.size

    override fun getFluid(index: Int): FluidStack = fluids[index].let(HTIngredientHelper::unwrap)

    override fun getItem(index: Int): ItemStack = items[index].let(HTIngredientHelper::unwrap)

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = super.isEmpty() && customKeys.isEmpty()
}
