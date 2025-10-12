package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * さまざまな要素のキーとなるインターフェース
 */
interface HTVariantKey {
    fun translate(type: HTLanguageType, value: String): String

    fun variantName(): String

    /**
     * [TagKey]を保持する[HTVariantKey]の拡張インターフェース
     */
    interface Tagged<TYPE : Any> : HTVariantKey {
        val tagKey: TagKey<TYPE>
    }

    /**
     * [BlockEntity]を保持する[HTVariantKey]の拡張インターフェース
     */
    interface WithBE<BE : BlockEntity> :
        HTVariantKey,
        HTItemHolderLike {
        val blockHolder: HTDeferredBlock<*, *>
        val blockEntityHolder: HTDeferredBlockEntityType<out BE>

        override fun getId(): ResourceLocation = blockHolder.id

        override fun asItem(): Item = blockHolder.asItem()
    }
}
