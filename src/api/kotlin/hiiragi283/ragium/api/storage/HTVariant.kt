package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.extension.isOf
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

sealed class HTVariant<T : Any>(val holder: Holder<T>, val components: DataComponentPatch) {
    abstract val isEmpty: Boolean
}

class HTItemVariant(holder: Holder<Item>, components: DataComponentPatch) : HTVariant<Item>(holder, components) {
    companion object {
        @JvmField
        val EMPTY = HTItemVariant(ItemStack.EMPTY)
    }

    constructor(stack: ItemStack) : this(stack.itemHolder, stack.componentsPatch)

    override val isEmpty: Boolean
        get() = toStack().isEmpty

    fun isOf(stack: ItemStack): Boolean {
        if (stack.isEmpty) return this.isEmpty
        return holder.isOf(stack.item) && stack.componentsPatch == this.components
    }

    fun toStack(count: Int = 1): ItemStack = ItemStack(holder, count, components)
}

class HTFluidVariant(holder: Holder<Fluid>, components: DataComponentPatch) : HTVariant<Fluid>(holder, components) {
    companion object {
        @JvmField
        val EMPTY = HTFluidVariant(FluidStack.EMPTY)
    }

    constructor(stack: FluidStack) : this(stack.fluidHolder, stack.componentsPatch)

    override val isEmpty: Boolean
        get() = toStack(1).isEmpty

    fun toStack(count: Int): FluidStack = FluidStack(holder, count, components)
}
