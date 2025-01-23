package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object RagiumItemTags {
    @JvmField
    val DOUGH: TagKey<Item> = itemTagKey(commonId("foods/dough"))

    @JvmField
    val MILK: TagKey<Item> = itemTagKey(commonId("foods/milk"))
}
