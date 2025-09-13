package hiiragi283.ragium.api.curio

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.extension.toId
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

enum class HTCurioSlots : StringRepresentable {
    BACK,
    BELT,
    BODY,
    BRACELET,
    CHARM,
    CURIO,
    HANDS,
    HEAD,
    NECKLACE,
    RING,
    ;

    val slotTag: TagKey<Item> = itemTagKey(RagiumConst.CURIOS.toId(serializedName))

    override fun getSerializedName(): String = name.lowercase()
}
