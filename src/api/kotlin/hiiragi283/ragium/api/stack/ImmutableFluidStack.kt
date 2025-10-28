package hiiragi283.ragium.api.stack

import hiiragi283.ragium.api.serialization.codec.BiCodec
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

/**
 * [FluidStack]向けの[ImmutableStack]の実装
 */
@JvmInline
value class ImmutableFluidStack private constructor(val stack: FluidStack) : ImmutableStack<Fluid, ImmutableFluidStack> {
    companion object {
        @JvmStatic
        private val FLUID_STACK_CODEC: BiCodec<RegistryFriendlyByteBuf, FluidStack> = BiCodec.of(FluidStack.CODEC, FluidStack.STREAM_CODEC)

        @JvmField
        val OPTIONAL_CODEC: BiCodec<RegistryFriendlyByteBuf, Optional<ImmutableFluidStack>> = FLUID_STACK_CODEC
            .xmap(
                { stack: FluidStack -> Optional.ofNullable(stack.toImmutable()) },
                { optional: Optional<ImmutableFluidStack> -> optional.map(ImmutableFluidStack::stack).orElse(FluidStack.EMPTY) },
            )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, ImmutableFluidStack> =
            FLUID_STACK_CODEC.comapFlatMap(
                { stack: FluidStack ->
                    when (val immutable: ImmutableFluidStack? = stack.toImmutable()) {
                        null -> Result.failure(error("FluidStack must not be empty"))
                        else -> Result.success(immutable)
                    }
                },
                ImmutableFluidStack::stack,
            )

        @JvmStatic
        fun of(fluid: Fluid, amount: Int): ImmutableFluidStack = of(FluidStack(fluid, amount)) ?: error("FluidStack must not be empty")

        /**
         * [FluidStack]を[ImmutableFluidStack]に変換します。
         * @return [FluidStack.isEmpty]が`true`の場合は`null`を返します。
         */
        @JvmStatic
        fun of(stack: FluidStack): ImmutableFluidStack? = when (stack.isEmpty) {
            true -> null
            false -> ImmutableFluidStack(stack)
        }
    }

    override fun value(): Fluid = stack.fluid

    override fun holder(): Holder<Fluid> = stack.fluidHolder

    override fun amountAsInt(): Int = stack.amount

    override fun copy(): ImmutableFluidStack = ImmutableFluidStack(stack.copy())

    override fun copyWithAmount(amount: Int): ImmutableFluidStack? = of(stack.copyWithAmount(amount))

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getComponents(): DataComponentMap = stack.components

    override fun getText(): Component = stack.hoverName
}
