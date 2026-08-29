package hiiragi283.lib.recipe.result

import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Ior
import java.util.Objects
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * アイテムと液体の完成品を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemAndFluidResult(val item: ItemStack, val fluid: FluidStack) {
    constructor(item: ItemStack) : this(item, FluidStack.EMPTY)

    constructor(fluid: FluidStack) : this(ItemStack.EMPTY, fluid)

    constructor(pair: Pair<ItemStack, FluidStack>) : this(pair.first, pair.second)

    constructor(either: Either<ItemStack, FluidStack>) : this(either.leftOrNull() ?: ItemStack.EMPTY, either.getOrNull() ?: FluidStack.EMPTY)

    constructor(ior: Ior<ItemStack, FluidStack>) : this(ior.getLeft() ?: ItemStack.EMPTY, ior.getRight() ?: FluidStack.EMPTY)

    override fun equals(other: Any?): Boolean = (other as? HTItemAndFluidResult)?.let {
        ItemStack.isSameItemSameComponents(it.item, this.item) && FluidStack.isSameFluid(it.fluid, this.fluid)
    } ?: false

    override fun hashCode(): Int = Objects.hash(ItemStack.hashItemAndComponents(item), FluidStack.hashFluidAndComponents(fluid))
}
