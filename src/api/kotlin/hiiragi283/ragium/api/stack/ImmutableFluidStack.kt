package hiiragi283.ragium.api.stack

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
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
        @JvmField
        val CODEC_NON_EMPTY: BiCodec<RegistryFriendlyByteBuf, ImmutableFluidStack> = BiCodec.composite(
            VanillaBiCodecs.holder(Registries.FLUID).fieldOf("id"),
            ImmutableFluidStack::holder,
            BiCodecs.POSITIVE_INT.fieldOf("amount"),
            ImmutableFluidStack::amountAsInt,
            VanillaBiCodecs.COMPONENT_PATCH.optionalFieldOf("components", DataComponentPatch.EMPTY),
            ImmutableFluidStack::componentsPatch,
        ) { holder: Holder<Fluid>, amount: Int, components: DataComponentPatch ->
            FluidStack(holder, amount, components).toImmutable()
        }

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, ImmutableFluidStack> =
            CODEC_NON_EMPTY.toOptional().xmap(
                { optional: Optional<ImmutableFluidStack> -> optional.orElse(EMPTY) },
                { stack: ImmutableFluidStack -> Optional.of(stack).filter(ImmutableFluidStack::isNotEmpty) },
            )

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

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getComponents(): DataComponentMap = stack.components

    override fun getText(): Component = stack.hoverName
}
