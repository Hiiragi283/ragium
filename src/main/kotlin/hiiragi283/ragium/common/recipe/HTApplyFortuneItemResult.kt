package hiiragi283.ragium.common.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.base.HTItemResult
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.ItemLike

@ConsistentCopyVisibility
data class HTApplyFortuneItemResult private constructor(val item: Holder<Item>, val count: Int, val addition: Int) : HTItemResult {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTApplyFortuneItemResult> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    ItemStack.ITEM_NON_AIR_CODEC.fieldOf("item").forGetter(HTApplyFortuneItemResult::item),
                    Codec.intRange(1, 99).optionalFieldOf("count", 1).forGetter(HTApplyFortuneItemResult::count),
                    Codec.intRange(1, Int.MAX_VALUE).fieldOf("addition").forGetter(HTApplyFortuneItemResult::addition),
                ).apply(instance, ::HTApplyFortuneItemResult)
        }
    }

    constructor(item: ItemLike, count: Int, addition: Int) : this(item.asHolder(), count, addition)

    override fun getCodec(): MapCodec<out HTItemResult> = CODEC

    override fun getResultId(): ResourceLocation = item.idOrThrow

    override fun getItem(enchantments: ItemEnchantments): ItemStack {
        val fortune: Int = enchantments.getLevel(Enchantments.FORTUNE)
        val stackCount: Int = count + (addition * fortune)
        return ItemStack(item, stackCount)
    }
}
