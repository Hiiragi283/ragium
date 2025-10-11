package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import hiiragi283.ragium.api.storage.item.isOf
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * [ItemLike]を継承した[HTHolderLike]の拡張インターフェース
 */
interface HTItemHolderLike :
    ItemLike,
    HTHolderLike {
    companion object {
        /**
         * [ResourceLocation]を[HTItemHolderLike]に変換します。
         */
        @JvmStatic
        fun fromId(id: ResourceLocation): HTItemHolderLike = fromKey(ResourceKey.create(Registries.ITEM, id))

        /**
         * [ResourceKey]を[HTItemHolderLike]に変換します。
         */
        @JvmStatic
        fun fromKey(key: ResourceKey<Item>): HTItemHolderLike = object : HTItemHolderLike {
            override fun asItem(): Item = BuiltInRegistries.ITEM.getOrThrow(key)

            override fun getId(): ResourceLocation = key.location()
        }

        /**
         * [Holder]を[HTItemHolderLike]に変換します。
         */
        @JvmStatic
        fun fromHolder(holder: Holder<Item>): HTItemHolderLike = object : HTItemHolderLike {
            override fun asItem(): Item = holder.value()

            override fun getId(): ResourceLocation = holder.idOrThrow
        }

        /**
         * [ItemLike]を[HTItemHolderLike]に変換します。
         */
        @JvmStatic
        fun fromItem(item: ItemLike): HTItemHolderLike = object : HTItemHolderLike {
            private val holder: HTHolderLike = HTHolderLike.fromItem(item)

            override fun asItem(): Item = item.asItem()

            override fun getId(): ResourceLocation = holder.getId()
        }

        /**
         * [ItemStack]を[HTItemHolderLike]に変換します。
         */
        @JvmStatic
        fun fromStack(stack: ItemStack): HTItemHolderLike = fromHolder(stack.itemHolder)
    }

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
     * 指定した[count]と[components]から[ItemStack]を返します。
     */
    fun toStack(count: Int = 1, components: DataComponentPatch): ItemStack {
        val stack: ItemStack = toStack(count)
        stack.applyComponents(components)
        return stack
    }

    /**
     * 指定した[stack]にアイテムが一致するか判定します。
     */
    fun isOf(stack: ItemStack): Boolean = stack.`is`(asItem())

    /**
     * 指定した[stack]にアイテムが一致するか判定します。
     */
    fun isOf(stack: HTItemStorageStack): Boolean = stack.isOf(asItem())
}
