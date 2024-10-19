package hiiragi283.ragium.api.accessory

import net.minecraft.util.StringIdentifiable

enum class HTAccessorySlotTypes : StringIdentifiable {
    CHARM,
    HAT,
    FACE,
    CAPE,
    NECKLACE,
    BACK,
    HAND,
    RING,
    WRIST,
    BELT,
    ANKLET,
    SHOES,
    ;

    override fun asString(): String = name.lowercase()
}
