package hiiragi283.ragium.api.storage.item

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.storage.ImmutableStack
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * [ItemStack]向けの[ImmutableStack]の実装
 */
@JvmInline
value class ImmutableItemStack private constructor(val stack: ItemStack) : ImmutableStack<Item, ImmutableItemStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, ImmutableItemStack> =
            VanillaBiCodecs.itemStack(true).xmap(::of, ImmutableItemStack::stack)

        /**
         * 空の[ImmutableItemStack]
         */
        val EMPTY = ImmutableItemStack(ItemStack.EMPTY)

        /**
         * [ItemStack]を[ImmutableItemStack]に変換します。
         * @return [ItemStack.isEmpty]が`true`の場合は[EMPTY]を返します。
         */
        @JvmStatic
        fun of(stack: ItemStack): ImmutableItemStack = when (stack.isEmpty) {
            true -> EMPTY
            false -> ImmutableItemStack(stack)
        }
    }

    override fun isEmpty(): Boolean = stack.isEmpty

    override fun value(): Item = stack.item

    override fun holder(): Holder<Item> = stack.itemHolder

    override fun amountAsInt(): Int = stack.count

    override fun copy(): ImmutableItemStack = of(stack.copy())

    override fun copyWithAmount(amount: Int): ImmutableItemStack = of(stack.copyWithCount(amount))

    override fun copyWithAmount(amount: Long): ImmutableItemStack = copyWithAmount(Ints.saturatedCast(amount))

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getComponents(): DataComponentMap = stack.components

    override fun getText(): Component = stack.hoverName
}
