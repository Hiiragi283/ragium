package hiiragi283.ragium.common.integration.mekanism.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.storage.ImmutableStack
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component

/**
 * [ChemicalStack]向けの[ImmutableStack]の実装
 */
@JvmInline
value class ImmutableChemicalStack private constructor(val stack: ChemicalStack) : ImmutableStack<Chemical, ImmutableChemicalStack> {
    companion object {
        @JvmField
        val RAW_CODEC: BiCodec<RegistryFriendlyByteBuf, ChemicalStack> = BiCodec.of(
            ChemicalStack.OPTIONAL_CODEC,
            ChemicalStack.OPTIONAL_STREAM_CODEC,
        )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, ImmutableChemicalStack> =
            RAW_CODEC.xmap(::of, ImmutableChemicalStack::stack)

        val EMPTY = ImmutableChemicalStack(ChemicalStack.EMPTY)

        @JvmStatic
        fun of(stack: ChemicalStack): ImmutableChemicalStack = when (stack.isEmpty) {
            true -> EMPTY
            false -> ImmutableChemicalStack(stack)
        }
    }

    override fun isEmpty(): Boolean = stack.isEmpty

    override fun value(): Chemical = stack.chemical

    override fun holder(): Holder<Chemical> = stack.chemicalHolder

    override fun amountAsInt(): Int = Ints.saturatedCast(amountAsLong())

    override fun amountAsLong(): Long = stack.amount

    override fun copy(): ImmutableChemicalStack = ImmutableChemicalStack(stack)

    override fun copyWithAmount(amount: Int): ImmutableChemicalStack = copyWithAmount(amount.toLong())

    override fun copyWithAmount(amount: Long): ImmutableChemicalStack = ImmutableChemicalStack(stack.copyWithAmount(amount))

    override fun componentsPatch(): DataComponentPatch = DataComponentPatch.EMPTY

    override fun getComponents(): DataComponentMap = DataComponentMap.EMPTY

    override fun getText(): Component = stack.textComponent
}
