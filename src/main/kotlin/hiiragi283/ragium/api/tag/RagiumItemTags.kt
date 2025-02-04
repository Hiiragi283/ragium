package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object RagiumItemTags {
    //    Common    //

    @JvmField
    val ALKALI_REAGENTS: TagKey<Item> = itemTagKey(commonId("reagents/alkali"))

    @JvmField
    val COAL_COKE: TagKey<Item> = itemTagKey(commonId("coal_coke"))

    @JvmField
    val DOUGH: TagKey<Item> = itemTagKey(commonId("foods/dough"))

    @JvmField
    val PLASTICS: TagKey<Item> = itemTagKey(commonId("plastics"))

    @JvmField
    val SLAG: TagKey<Item> = itemTagKey(commonId("slag"))

    //    Ragium    //

    @JvmField
    val GEAR_MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/gear"))

    @JvmField
    val PLATE_MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/plate"))

    @JvmField
    val ROD_MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/rod"))

    @JvmField
    val WIRE_MOLDS: TagKey<Item> = itemTagKey(RagiumAPI.id("molds/wire"))

    @JvmField
    val SOLAR_PANELS: TagKey<Item> = itemTagKey(RagiumAPI.id("solar_panels"))
}
