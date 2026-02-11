package hiiragi283.ragium.common.recipe.input

import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTSingleCatalystRecipeInput private constructor(private val content: Either<ItemStack, FluidStack>, val catalyst: ItemStack) :
    HTFluidRecipeInput {
        constructor(item: ItemStack, catalyst: ItemStack) : this(Either.Left(item), catalyst)

        constructor(item: FluidStack, catalyst: ItemStack) : this(Either.Right(item), catalyst)

        override fun getFluid(index: Int): FluidStack = content.getRight() ?: FluidStack.EMPTY

        override fun getFluidSize(): Int = when {
            content.isRight() -> 1
            else -> 0
        }

        override fun getItem(index: Int): ItemStack = content.getLeft() ?: ItemStack.EMPTY

        override fun size(): Int = when {
            content.isLeft() -> 1
            else -> 0
        }

        override fun isEmpty(): Boolean = content.map(ItemStack::isEmpty, FluidStack::isEmpty) && catalyst.isEmpty
    }
