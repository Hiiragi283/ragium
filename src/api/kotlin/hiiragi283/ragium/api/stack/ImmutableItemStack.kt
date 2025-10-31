package hiiragi283.ragium.api.stack

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.capabilities.ItemCapability

/**
 * [ItemStack]向けの[ImmutableStack]の実装
 */
@JvmInline
value class ImmutableItemStack private constructor(private val stack: ItemStack) : ImmutableStack<Item, ImmutableItemStack> {
    companion object {
        @JvmStatic
        private val ITEM_STACK_CODEC: BiCodec<RegistryFriendlyByteBuf, ItemStack> =
            BiCodec.composite(
                VanillaBiCodecs.holder(Registries.ITEM).fieldOf("id"),
                ItemStack::getItemHolder,
                BiCodecs.POSITIVE_INT.optionalFieldOf("count") { 1 },
                ItemStack::getCount,
                VanillaBiCodecs.COMPONENT_PATCH.optionalFieldOf("components", DataComponentPatch.EMPTY),
                ItemStack::getComponentsPatch,
                ::ItemStack,
            )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, ImmutableItemStack> =
            ITEM_STACK_CODEC.comapFlatMap(
                { stack: ItemStack -> runCatching(stack::toImmutableOrThrow) },
                ImmutableItemStack::stack,
            )

        @JvmStatic
        fun of(item: ItemLike, count: Int = 1): ImmutableItemStack = ItemStack(item, count).toImmutableOrThrow()

        /**
         * [ItemStack]を[ImmutableItemStack]に変換します。
         * @return [ItemStack.isEmpty]が`true`の場合は`null`を返します。
         */
        @JvmStatic
        fun of(stack: ItemStack): ImmutableItemStack? = when (stack.isEmpty) {
            true -> null
            false -> ImmutableItemStack(stack.copy())
        }
    }

    /**
     * 保持している[ItemStack]のコピーを返します。
     */
    fun unwrap(): ItemStack = stack.copy()

    fun isOf(item: Item): Boolean = stack.`is`(item)

    fun isOf(tagKey: TagKey<Item>): Boolean = stack.`is`(tagKey)

    fun isOf(holder: Holder<Item>): Boolean = stack.`is`(holder)

    fun isOf(holderSet: HolderSet<Item>): Boolean = stack.`is`(holderSet)

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun <T, C : Any> getCapability(capability: ItemCapability<T, C?>, context: C?): T? = stack.getCapability(capability, context)

    fun <T> getCapability(capability: ItemCapability<T, Void?>): T? = getCapability(capability, null)

    //    ImmutableStack    //

    override fun value(): Item = stack.item

    override fun holder(): Holder<Item> = stack.itemHolder

    override fun amount(): Int = stack.count

    override fun copyWithAmount(amount: Int): ImmutableItemStack? = of(stack.copyWithCount(amount))

    override fun componentsPatch(): DataComponentPatch = stack.componentsPatch

    override fun getComponents(): DataComponentMap = stack.components

    override fun getText(): Component = stack.hoverName
}
