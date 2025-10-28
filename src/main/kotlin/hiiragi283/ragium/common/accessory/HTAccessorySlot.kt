package hiiragi283.ragium.common.accessory

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * @see io.wispforest.accessories.api.data.AccessoriesBaseData
 */
enum class HTAccessorySlot {
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

    val slotTag: TagKey<Item> = Registries.ITEM.createTagKey(RagiumConst.ACCESSORIES.toId(name.lowercase()))
}
