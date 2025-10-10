package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.idOrThrow
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

interface HTItemHolderLike :
    ItemLike,
    HTHolderLike {
    companion object {
        @JvmStatic
        fun fromId(id: ResourceLocation): HTItemHolderLike = fromKey(ResourceKey.create(Registries.ITEM, id))

        @JvmStatic
        fun fromKey(key: ResourceKey<Item>): HTItemHolderLike = object : HTItemHolderLike {
            override fun asItem(): Item = BuiltInRegistries.ITEM.getOrThrow(key)

            override fun getId(): ResourceLocation = key.location()
        }

        @JvmStatic
        fun fromHolder(holder: Holder<Item>): HTItemHolderLike = object : HTItemHolderLike {
            override fun asItem(): Item = holder.value()

            override fun getId(): ResourceLocation = holder.idOrThrow
        }

        @JvmStatic
        fun fromItem(item: ItemLike): HTItemHolderLike = object : HTItemHolderLike {
            private val holder: HTHolderLike = HTHolderLike.fromItem(item)

            override fun asItem(): Item = item.asItem()

            override fun getId(): ResourceLocation = holder.getId()
        }

        @JvmStatic
        fun fromStack(stack: ItemStack): HTItemHolderLike = fromHolder(stack.itemHolder)
    }

    fun toStack(count: Int = 1): ItemStack {
        val stack: ItemStack = asItem().defaultInstance
        check(!stack.isEmpty) { "Obtained empty item stack; incorrect getDefaultInstance() call?" }
        stack.count = count
        return stack
    }

    fun toStack(count: Int = 1, components: DataComponentPatch): ItemStack {
        val stack: ItemStack = toStack(count)
        stack.applyComponents(components)
        return stack
    }

    fun isOf(stack: ItemStack): Boolean = stack.`is`(asItem())

    fun isOf(stack: HTItemStorageStack): Boolean = stack.isOf(asItem())
}
