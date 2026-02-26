package hiiragi283.ragium.api.tag

import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.tag.property.addNamePattern

/**
 * @see hiiragi283.core.api.tag.CommonTagPrefixes
 */
object RagiumTagPrefixes {
    @JvmField
    val PELLET: HTTagPrefix = HTTagPrefix.create("pellet", "pellets", "pellets/%s") {
        put(HTTagPropertyKeys.ID_PATTERN, "%s_pellet")

        addNamePattern("%s Pellet", "%sペレット")
    }
}
