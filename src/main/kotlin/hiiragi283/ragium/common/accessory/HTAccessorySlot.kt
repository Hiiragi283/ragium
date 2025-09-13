package hiiragi283.ragium.common.accessory

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.extension.toId
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

/**
 * @see [io.wispforest.accessories.api.data.AccessoriesBaseData]
 */
enum class HTAccessorySlot : StringRepresentable {
    ANKLET,
    BACK,
    BELT,
    CAPE,
    CHARM,
    FACE,
    HAND,
    HAT,
    NECKLACE,
    RING,
    SHOES,
    WRIST,
    ;

    val slotTag: TagKey<Item> = itemTagKey(RagiumConst.ACCESSORIES.toId(serializedName))

    override fun getSerializedName(): String = name.lowercase()
}
