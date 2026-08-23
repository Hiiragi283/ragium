package hiiragi283.lib.material

import hiiragi283.ragium.api.tag.HTItemPart

enum class HTMaterialCategory(val basePart: HTItemPart?) {
    FUEL(null),
    MINERAL(HTItemPart.DUST),
    GEM(HTItemPart.GEM),
    METAL(HTItemPart.INGOT),
    OTHER(null),
}
