package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object RagiumItemTags {
    @JvmField
    val DOUGH: TagKey<Item> = itemTagKey(commonId("foods/dough"))

    @JvmField
    val COAL_COKES: TagKey<Item> = itemTagKey(commonId("coal_cokes"))

    @JvmField
    val PLASTIC_PLATES: TagKey<Item> = itemTagKey(commonId("plates/plastic"))
}
