package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.data.HTLanguageType
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.entity.BlockEntity

interface HTVariantKey : StringRepresentable {
    fun translate(type: HTLanguageType, value: String): String

    interface Tagged<T : Any> : HTVariantKey {
        val tagKey: TagKey<T>
    }

    interface WithBE<BE : BlockEntity> : HTVariantKey {
        val blockEntityHolder: HTDeferredBlockEntityType<out BE>
    }
}
