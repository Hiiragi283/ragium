package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.data.lang.HTTranslationProvider
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * さまざまな要素のキーとなるインターフェース
 */
interface HTVariantKey : HTTranslationProvider {
    fun variantName(): String

    /**
     * [Block]を保持する[HTVariantKey]の拡張インターフェース
     * @param BLOCK [Block]のクラス
     */
    interface WithBlock<BLOCK : Block> :
        HTVariantKey,
        ItemLike {
        val blockHolder: HTDeferredBlock<BLOCK, *>

        fun getBlock(): BLOCK = blockHolder.get()

        override fun asItem(): Item = blockHolder.asItem()

        fun toStack(count: Int = 1): ItemStack = blockHolder.toStack(count)
    }

    /**
     * [BlockEntity]を保持する[HTVariantKey]の拡張インターフェース
     * @param BE [BlockEntity]のクラス
     */
    interface WithBE<BE : BlockEntity> : HTVariantKey {
        val blockEntityHolder: HTDeferredBlockEntityType<out BE>

        fun getBlockEntityType(): BlockEntityType<out BE> = blockEntityHolder.get()
    }

    interface WithBlockAndBE<BLOCK : Block, BE : BlockEntity> :
        WithBlock<BLOCK>,
        WithBE<BE>
}
