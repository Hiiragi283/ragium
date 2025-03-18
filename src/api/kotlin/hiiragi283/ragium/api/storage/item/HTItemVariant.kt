package hiiragi283.ragium.api.storage.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.extension.wrapEmpty
import hiiragi283.ragium.api.storage.HTVariant
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

@ConsistentCopyVisibility
data class HTItemVariant private constructor(override val holder: Holder<Item>, override val components: DataComponentPatch) :
    HTVariant<Item> {
        companion object {
            @JvmStatic
            private val RAW_CODEC: Codec<HTItemVariant> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        ItemStack.ITEM_NON_AIR_CODEC.fieldOf("id").forGetter(HTItemVariant::holder),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTItemVariant::components),
                    ).apply(instance, ::HTItemVariant)
            }

            @JvmField
            val CODEC: Codec<HTItemVariant> = RAW_CODEC.wrapEmpty(::EMPTY)

            @JvmField
            val EMPTY: HTItemVariant = HTItemVariant(Items.AIR.asItemHolder(), DataComponentPatch.EMPTY)

            @JvmStatic
            fun of(item: ItemLike, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemVariant =
                of(ItemStack(item.asItemHolder(), 1, components))

            @JvmStatic
            fun of(stack: ItemStack): HTItemVariant = if (stack.isEmpty) EMPTY else HTItemVariant(stack.itemHolder, stack.componentsPatch)
        }

        override val isEmpty: Boolean
            get() = this == EMPTY || holder.isOf(Items.AIR)

        fun isOf(stack: ItemStack): Boolean {
            if (stack.isEmpty) return this.isEmpty
            return holder.isOf(stack.item) && stack.componentsPatch == this.components
        }

        fun isIn(tagKey: TagKey<Item>): Boolean = holder.`is`(tagKey)

        fun toStack(count: Int = 1): ItemStack = when {
            isEmpty -> ItemStack.EMPTY
            else -> ItemStack(holder, count, components)
        }
    }
