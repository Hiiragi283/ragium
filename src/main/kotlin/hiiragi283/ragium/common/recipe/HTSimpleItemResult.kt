package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.base.HTItemResult
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.ItemLike

@ConsistentCopyVisibility
data class HTSimpleItemResult private constructor(val item: Holder<Item>, val count: Int, val components: DataComponentPatch) :
    HTItemResult {
        companion object {
            @JvmField
            val CODEC: MapCodec<HTSimpleItemResult> = RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        ItemStack.ITEM_NON_AIR_CODEC.fieldOf("item").forGetter(HTSimpleItemResult::item),
                        ExtraCodecs.intRange(1, 99).optionalFieldOf("count", 1).forGetter(HTSimpleItemResult::count),
                        DataComponentPatch.CODEC
                            .optionalFieldOf(
                                "components",
                                DataComponentPatch.EMPTY,
                            ).forGetter(HTSimpleItemResult::components),
                    ).apply(instance, ::HTSimpleItemResult)
            }
        }

        constructor(item: ItemLike, count: Int, components: DataComponentPatch) : this(item.asHolder(), count, components)

        override fun getCodec(): MapCodec<out HTItemResult> = CODEC

        override fun getResultId(): ResourceLocation = item.idOrThrow

        override fun getItem(enchantments: ItemEnchantments): ItemStack = ItemStack(item, count, components)
    }
