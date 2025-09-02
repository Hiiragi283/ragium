package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.entity.BlockEntity

interface HTVariantKey : StringRepresentable {
    fun translate(type: HTLanguageType, value: String): String

    interface Tagged<TYPE : Any> : HTVariantKey {
        val tagKey: TagKey<TYPE>
    }

    interface WithBE<BE : BlockEntity> :
        HTVariantKey,
        ItemLike {
        val blockHolder: HTDeferredBlock<*, *>
        val blockEntityHolder: HTDeferredBlockEntityType<out BE>

        override fun asItem(): Item = blockHolder.asItem()
    }
}
