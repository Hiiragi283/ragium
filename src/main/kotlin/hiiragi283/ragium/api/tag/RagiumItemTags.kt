package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object RagiumItemTags {
    //    Common    //

    @JvmField
    val DOUGH: TagKey<Item> = itemTagKey(commonId("foods/dough"))

    @JvmField
    val COAL_COKE: TagKey<Item> = itemTagKey(commonId("coal_coke"))

    @JvmField
    val PLASTICS: TagKey<Item> = itemTagKey(commonId("plastics"))

    //    Ragium    //

    @JvmField
    val SOLAR_PANELS: TagKey<Item> = itemTagKey(RagiumAPI.id("solar_panels"))
}
