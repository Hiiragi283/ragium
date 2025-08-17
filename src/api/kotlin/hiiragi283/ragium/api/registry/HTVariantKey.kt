package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.data.HTLanguageType
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.registries.DeferredBlock

interface HTVariantKey : StringRepresentable {
    fun translate(type: HTLanguageType, value: String): String

    interface Tagged<T : Any> : HTVariantKey {
        val tagKey: TagKey<T>
    }

    interface WithBE<BE : BlockEntity> :
        HTVariantKey,
        ItemLike {
        val blockHolder: DeferredBlock<*>
        val blockEntityHolder: HTDeferredBlockEntityType<out BE>

        override fun asItem(): Item = blockHolder.asItem()
    }
}
