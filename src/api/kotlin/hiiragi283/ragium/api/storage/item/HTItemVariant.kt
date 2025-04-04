package hiiragi283.ragium.api.storage.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.storage.HTVariant
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import java.util.*

@ConsistentCopyVisibility
data class HTItemVariant private constructor(val item: Item, override val components: DataComponentPatch = DataComponentPatch.EMPTY) :
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
                    ).apply(instance, ::of)
            }

            @JvmField
            val CODEC: Codec<HTItemVariant> = ExtraCodecs.optionalEmptyMap(RAW_CODEC).xmap(
                { optional: Optional<HTItemVariant> -> optional.orElse(EMPTY) },
                { variant: HTItemVariant -> if (variant.isEmpty) Optional.empty() else Optional.of(variant) },
            )

            @JvmStatic
            private val simpleCache: MutableMap<Item, HTItemVariant> = mutableMapOf()

            @JvmField
            val EMPTY: HTItemVariant = of(Items.AIR)

            @JvmStatic
            fun of(stack: ItemStack): HTItemVariant = of(stack.item, stack.componentsPatch)

            @JvmStatic
            fun of(holder: Holder<Item>, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemVariant =
                of(holder.value(), components)

            @JvmStatic
            fun of(item: ItemLike, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemVariant = of(item.asItem(), components)

            @JvmStatic
            private fun of(item: Item, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemVariant {
                if (components.isEmpty) {
                    return simpleCache.computeIfAbsent(item, ::HTItemVariant)
                }
                return HTItemVariant(item, components)
            }
        }

        override val holder: Holder.Reference<Item>
            get() = item.asItemHolder()

        override val isEmpty: Boolean
            get() = isOf(Items.AIR)

        fun isOf(stack: ItemStack): Boolean {
            if (stack.isEmpty) return this.isEmpty
            return isOf(stack.item) && stack.componentsPatch == this.components
        }

        fun isOf(item: ItemLike): Boolean = isOf(item.asItem())

        fun toStack(count: Int = 1): ItemStack = when {
            isEmpty -> ItemStack.EMPTY
            else -> ItemStack(holder, count, components)
        }
    }
