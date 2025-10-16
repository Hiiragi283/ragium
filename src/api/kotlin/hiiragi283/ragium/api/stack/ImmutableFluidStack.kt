package hiiragi283.ragium.api.stack

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [FluidStack]向けの[ImmutableStack]の実装
 */
@JvmInline
value class ImmutableFluidStack private constructor(val stack: FluidStack) : ImmutableStack<Fluid, ImmutableFluidStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, ImmutableFluidStack> =
            VanillaBiCodecs.fluidStack(true).xmap(::of, ImmutableFluidStack::stack)

        /**
         * 空の[ImmutableFluidStack]
         */
        val EMPTY = ImmutableFluidStack(FluidStack.EMPTY)

        @JvmStatic
        fun of(fluid: Fluid, amount: Int): ImmutableFluidStack = of(FluidStack(fluid, amount))

        /**
         * [FluidStack]を[ImmutableFluidStack]に変換します。
         * @return [FluidStack.isEmpty]が`true`の場合は[EMPTY]を返します。
         */
        @JvmStatic
        fun of(stack: FluidStack): ImmutableFluidStack = when (stack.isEmpty) {
            true -> EMPTY
            false -> ImmutableFluidStack(stack)
        }
    }

    override fun isEmpty(): Boolean = stack.isEmpty

    override fun value(): Fluid = stack.fluid

    override fun holder(): Holder<Fluid> = stack.fluidHolder

    override fun amountAsInt(): Int = stack.amount

    override fun copy(): ImmutableFluidStack = of(stack.copy())

    override fun copyWithAmount(amount: Int): ImmutableFluidStack = of(stack.copyWithAmount(amount))

    override fun copyWithAmount(amount: Long): ImmutableFluidStack = copyWithAmount(Ints.saturatedCast(amount))

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getComponents(): DataComponentMap = stack.components

    override fun getText(): Component = stack.hoverName
}
