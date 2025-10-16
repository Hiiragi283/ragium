package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityType
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
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
     * [Block]を保持する[HTVariantKey]の拡張インターフェース
     * @param BLOCK [Block]のクラス
     */
    interface WithBlock<BLOCK : Block> :
        HTVariantKey,
        ItemLike {
        val blockHolder: HTDeferredBlock<BLOCK, *>

        override fun asItem(): Item = blockHolder.asItem()

        fun toStack(count: Int = 1): ItemStack = blockHolder.toStack(count)
    }

    /**
     * [BlockEntity]を保持する[HTVariantKey]の拡張インターフェース
     * @param BE [BlockEntity]のクラス
     */
    interface WithBE<BE : BlockEntity> : HTVariantKey {
        val blockEntityHolder: HTDeferredBlockEntityType<out BE>
    }

    /**
     * [Entity]を保持する[HTVariantKey]の拡張インターフェース
     * @param ENTITY [Entity]のクラス
     */
    interface WithEntity<ENTITY : Entity> : HTVariantKey {
        val entityHolder: HTDeferredEntityType<out ENTITY>
    }
}
