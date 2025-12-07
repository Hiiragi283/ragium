package hiiragi283.ragium.api.stack

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import java.util.Optional

/**
 * [FluidStack]向けの[ImmutableStack]の実装
 */
@JvmInline
value class ImmutableFluidStack private constructor(private val stack: FluidStack) : ImmutableComponentStack<Fluid, ImmutableFluidStack> {
    companion object {
        @JvmField
        val OPTIONAL_CODEC: BiCodec<RegistryFriendlyByteBuf, Optional<ImmutableFluidStack>> =
            VanillaBiCodecs.FLUID_STACK.xmap(
                { stack: FluidStack -> Optional.ofNullable(stack.toImmutable()) },
                { optional: Optional<ImmutableFluidStack> -> optional.map(ImmutableFluidStack::stack).orElse(FluidStack.EMPTY) },
            )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, ImmutableFluidStack> =
            VanillaBiCodecs.FLUID_STACK_NON_EMPTY.flatXmap(FluidStack::toImmutableOrThrow, ImmutableFluidStack::stack)

        @JvmStatic
        fun of(fluid: Fluid, amount: Int): ImmutableFluidStack = FluidStack(fluid, amount).toImmutableOrThrow()

        @JvmStatic
        fun ofNullable(fluid: Fluid, amount: Int): ImmutableFluidStack? = FluidStack(fluid, amount).toImmutable()

        /**
         * [FluidStack]を[ImmutableFluidStack]に変換します。
         * @return [FluidStack.isEmpty]が`true`の場合は`null`を返します。
         */
        @JvmStatic
        fun of(stack: FluidStack): ImmutableFluidStack? = when (stack.isEmpty) {
            true -> null
            false -> ImmutableFluidStack(stack.copy())
        }
    }

    /**
     * 保持している[FluidStack]のコピーを返します。
     */
    fun unwrap(): FluidStack = stack.copy()

    fun fluidType(): FluidType = stack.fluidType

    fun isOf(fluid: Fluid): Boolean = stack.`is`(fluid)

    fun isOf(tagKey: TagKey<Fluid>): Boolean = stack.`is`(tagKey)

    fun isOf(holder: Holder<Fluid>): Boolean = stack.`is`(holder)

    fun isOf(holderSet: HolderSet<Fluid>): Boolean = stack.`is`(holderSet)

    //    ImmutableStack    //

    override fun value(): Fluid = stack.fluid

    override fun holder(): Holder<Fluid> = stack.fluidHolder

    override fun amount(): Int = stack.amount

    override fun copyWithAmount(amount: Int): ImmutableFluidStack? = of(stack.copyWithAmount(amount))

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getComponents(): DataComponentMap = stack.components

    override fun getText(): Component = stack.hoverName
}
