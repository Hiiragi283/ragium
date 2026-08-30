package hiiragi283.lib.material.part.property

import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.tag.HTTagPrefix

val HTPart.tagPrefix: HTTagPrefix? get() = this[HTPartPropertyKeys.TAG_PREFIX]
