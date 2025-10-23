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
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.Optional

/**
 * [ItemStack]向けの[ImmutableStack]の実装
 */
@JvmInline
value class ImmutableItemStack private constructor(val stack: ItemStack) : ImmutableStack<Item, ImmutableItemStack> {
    companion object {
        @JvmField
        val CODEC_NON_EMPTY: BiCodec<RegistryFriendlyByteBuf, ImmutableItemStack> =
            BiCodec.composite(
                VanillaBiCodecs.holder(Registries.ITEM).fieldOf("id"),
                ImmutableItemStack::holder,
                BiCodecs.POSITIVE_INT.optionalFieldOf("count") { 1 },
                ImmutableItemStack::amountAsInt,
                VanillaBiCodecs.COMPONENT_PATCH.optionalFieldOf("components", DataComponentPatch.EMPTY),
                ImmutableItemStack::componentsPatch,
            ) { holder: Holder<Item>, count: Int, components: DataComponentPatch ->
                ItemStack(holder, count, components).toImmutable()
            }

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, ImmutableItemStack> =
            CODEC_NON_EMPTY.toOptional().xmap(
                { optional: Optional<ImmutableItemStack> -> optional.orElse(EMPTY) },
                { stack: ImmutableItemStack -> Optional.of(stack).filter(ImmutableItemStack::isNotEmpty) },
            )

        /**
         * 空の[ImmutableItemStack]
         */
        val EMPTY = ImmutableItemStack(ItemStack.EMPTY)

        @JvmStatic
        fun of(item: ItemLike, count: Int = 1): ImmutableItemStack = of(ItemStack(item, count))

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

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getComponents(): DataComponentMap = stack.components

    override fun getText(): Component = stack.hoverName
}
