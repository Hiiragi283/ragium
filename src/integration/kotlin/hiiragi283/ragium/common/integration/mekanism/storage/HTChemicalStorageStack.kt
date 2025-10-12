package hiiragi283.ragium.common.integration.mekanism.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.storage.HTStorageStack
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component

/**
 * [ChemicalStack]向けの[HTStorageStack]の実装
 */
@JvmInline
value class HTChemicalStorageStack private constructor(val stack: ChemicalStack) : HTStorageStack<Chemical, HTChemicalStorageStack> {
    companion object {
        @JvmField
        val RAW_CODEC: BiCodec<RegistryFriendlyByteBuf, ChemicalStack> = BiCodec.of(
            ChemicalStack.OPTIONAL_CODEC,
            ChemicalStack.OPTIONAL_STREAM_CODEC,
        )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTChemicalStorageStack> =
            RAW_CODEC.xmap(::of, HTChemicalStorageStack::stack)

        val EMPTY = HTChemicalStorageStack(ChemicalStack.EMPTY)

        @JvmStatic
        fun of(stack: ChemicalStack): HTChemicalStorageStack = when (stack.isEmpty) {
            true -> EMPTY
            false -> HTChemicalStorageStack(stack)
        }
    }

    override fun isEmpty(): Boolean = stack.isEmpty

    override fun value(): Chemical = stack.chemical

    override fun holder(): Holder<Chemical> = stack.chemicalHolder

    override fun amountAsInt(): Int = Ints.saturatedCast(amountAsLong())

    override fun amountAsLong(): Long = stack.amount

    override fun copy(): HTChemicalStorageStack = HTChemicalStorageStack(stack)

    override fun copyWithAmount(amount: Int): HTChemicalStorageStack = HTChemicalStorageStack(stack.copyWithAmount(amountAsLong()))

    override fun componentsPatch(): DataComponentPatch = DataComponentPatch.EMPTY

    override fun getComponents(): DataComponentMap = DataComponentMap.EMPTY

    override fun getText(): Component = stack.textComponent
}
