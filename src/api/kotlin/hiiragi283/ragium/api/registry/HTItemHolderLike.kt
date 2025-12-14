package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

/**
 * [ItemLike]を継承した[HTHolderLike]の拡張インターフェース
 */
interface HTItemHolderLike :
    ItemLike,
    HTHolderLike {
    companion object {
        @JvmStatic
        fun fromId(id: ResourceLocation): HTItemHolderLike = fromKey(Registries.ITEM.createKey(id))

        /**
         * [ResourceKey]を[HTItemHolderLike]に変換します。
         */
        @JvmStatic
        fun fromKey(key: ResourceKey<Item>): HTItemHolderLike = object : HTItemHolderLike {
            override fun asItem(): Item = BuiltInRegistries.ITEM.getOrThrow(key)

            override fun getId(): ResourceLocation = key.location()
        }

        @JvmStatic
        fun fromHolder(holder: DeferredHolder<Item, *>): HTItemHolderLike = when (holder) {
            is HTItemHolderLike -> holder
            else -> object : HTItemHolderLike {
                override fun asItem(): Item = holder.get()

                override fun getId(): ResourceLocation = holder.id
            }
        }

        @JvmStatic
        fun fromItem(item: Supplier<out Item>): HTItemHolderLike = when (item) {
            is HTItemHolderLike -> item
            else -> fromItem(ItemLike(item::get))
        }

        /**
         * [ItemLike]を[HTItemHolderLike]に変換します。
         */
        @Suppress("DEPRECATION")
        @JvmStatic
        fun fromItem(item: ItemLike): HTItemHolderLike = when (item) {
            is HTItemHolderLike -> item
            else -> object : HTItemHolderLike {
                override fun asItem(): Item = item.asItem()

                override fun getId(): ResourceLocation = item.builtInRegistryHolder().idOrThrow
            }
        }

        /**
         * [Holder]を[HTItemHolderLike]に変換します。
         */
        @JvmStatic
        fun fromHolder(holder: Holder<Item>): HTItemHolderLike = when (holder) {
            is HTItemHolderLike -> holder
            else -> object : HTItemHolderLike {
                override fun asItem(): Item = holder.value()

                override fun getId(): ResourceLocation = holder.idOrThrow
            }
        }
    }

    fun isOf(stack: ItemStack): Boolean = stack.`is`(this.asItem())

    fun isOf(stack: ImmutableItemStack?): Boolean = stack?.isOf(this.asItem()) ?: false

    /**
     * 指定した[count]から[ItemStack]を返します。
     */
    fun toStack(count: Int = 1): ItemStack {
        val stack: ItemStack = asItem().defaultInstance
        check(!stack.isEmpty) { "Obtained empty item stack; incorrect getDefaultInstance() call?" }
        stack.count = count
        return stack
    }

    /**
     * 指定した[count]から[ImmutableItemStack]を返します。
     */
    fun toImmutableStack(count: Int = 1): ImmutableItemStack? = toStack(count).toImmutable()

    fun interface Delegate : HTItemHolderLike {
        fun getDelegate(): HTItemHolderLike

        override fun asItem(): Item = getDelegate().asItem()

        override fun getId(): ResourceLocation = getDelegate().getId()
    }
}
