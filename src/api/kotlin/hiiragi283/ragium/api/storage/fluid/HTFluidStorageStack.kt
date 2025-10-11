package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.storage.HTStorageStack
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

@JvmInline
value class HTFluidStorageStack private constructor(val stack: FluidStack) : HTStorageStack<Fluid, HTFluidStorageStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidStorageStack> =
            VanillaBiCodecs.fluidStack(true).xmap(::of, HTFluidStorageStack::stack)

        val EMPTY = HTFluidStorageStack(FluidStack.EMPTY)

        @JvmStatic
        fun of(stack: FluidStack): HTFluidStorageStack = when (stack.isEmpty) {
            true -> EMPTY
            false -> HTFluidStorageStack(stack)
        }
    }

    override fun isEmpty(): Boolean = stack.isEmpty

    override fun value(): Fluid = stack.fluid

    override fun holder(): Holder<Fluid> = stack.fluidHolder

    override fun amountAsInt(): Int = stack.amount

    override fun copy(): HTFluidStorageStack = of(stack.copy())

    override fun copyWithAmount(amount: Int): HTFluidStorageStack = of(stack.copyWithAmount(amount))

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun hoverName(): Component = stack.hoverName

    override fun getComponents(): DataComponentMap = stack.components
}
