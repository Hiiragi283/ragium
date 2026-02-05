package hiiragi283.ragium.common.recipe.input

import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTChemicalRecipeInput(
    items: Map<HTItemResourceType, Int>,
    val fluids: Map<HTFluidResourceType, Int>,
    val catalyst: HTItemResourceType?,
) : HTShapelessRecipeInput(items),
    HTFluidRecipeInput {
    constructor(
        items: Map<HTItemResourceType, Int>,
        fluids: Map<HTFluidResourceType, Int>,
        catalyst: ItemStack,
    ) : this(items, fluids, catalyst.toResource())

    constructor(items: Map<HTItemResourceType, Int>, fluids: Map<HTFluidResourceType, Int>) : this(items, fluids, null)

    override fun getFluid(index: Int): FluidStack {
        val (resource: HTFluidResourceType, amount: Int) = fluids.entries.elementAt(index)
        return resource.toStack(amount)
    }

    override fun getFluidSize(): Int = fluids.size

    override fun isEmpty(): Boolean = super<HTFluidRecipeInput>.isEmpty && fluids.isEmpty()
}
