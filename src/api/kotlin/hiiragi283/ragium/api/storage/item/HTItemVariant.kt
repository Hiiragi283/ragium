package hiiragi283.ragium.api.storage.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.HTRegistryCodecs
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.asReference
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.storage.HTVariant
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

@ConsistentCopyVisibility
data class HTItemVariant private constructor(override val holder: Holder.Reference<Item>, override val components: DataComponentPatch) :
    HTVariant<Item> {
        companion object {
            @JvmField
            val CODEC: Codec<HTItemVariant> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        HTRegistryCodecs.ITEM_HOLDER.fieldOf("id").forGetter(HTItemVariant::holder),
                        DataComponentPatch.CODEC
                            .optionalFieldOf("components", DataComponentPatch.EMPTY)
                            .forGetter(HTItemVariant::components),
                    ).apply(instance, ::of)
            }

            @JvmStatic
            private val simpleCache: MutableMap<Holder.Reference<Item>, HTItemVariant> = mutableMapOf()

            @JvmField
            val EMPTY: HTItemVariant = of(Items.AIR)

            @JvmStatic
            fun of(stack: ItemStack): HTItemVariant = of(stack.item, stack.componentsPatch)

            @JvmStatic
            fun of(item: ItemLike, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemVariant =
                of(item.asItemHolder(), components)

            @JvmStatic
            fun of(holder: Holder<Item>, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemVariant {
                val reference: Holder.Reference<Item> =
                    holder as? Holder.Reference<Item> ?: holder.asReference(BuiltInRegistries.ITEM)
                return when {
                    components.isEmpty -> simpleCache.computeIfAbsent(reference) { holder: Holder.Reference<Item> ->
                        HTItemVariant(holder, DataComponentPatch.EMPTY)
                    }

                    else -> HTItemVariant(reference, components)
                }
            }
        }

        override val isEmpty: Boolean
            get() = holder.isOf(Items.AIR)

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
