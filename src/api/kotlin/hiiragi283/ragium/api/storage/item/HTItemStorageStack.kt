package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.storage.HTStorageStack
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

@JvmInline
value class HTItemStorageStack private constructor(val stack: ItemStack) : HTStorageStack<Item> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemStorageStack> =
            VanillaBiCodecs.itemStack(true).xmap(::of, HTItemStorageStack::stack)

        val EMPTY = HTItemStorageStack(ItemStack.EMPTY)

        @JvmStatic
        fun of(stack: ItemStack): HTItemStorageStack = when (stack.isEmpty) {
            true -> EMPTY
            false -> HTItemStorageStack(stack)
        }

        @JvmStatic
        fun isSameItemSameComponents(first: HTItemStorageStack, second: HTItemStorageStack): Boolean =
            ItemStack.isSameItemSameComponents(first.stack, second.stack)
    }

    override fun isEmpty(): Boolean = stack.isEmpty

    override fun value(): Item = stack.item

    override fun holder(): Holder<Item> = stack.itemHolder

    override fun amountAsInt(): Int = stack.count

    override fun copy(): HTItemStorageStack = of(stack.copy())

    override fun copyWithAmount(amount: Int): HTItemStorageStack = of(stack.copyWithCount(amount))

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getComponents(): DataComponentMap = stack.components
}
