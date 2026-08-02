package hiiragi283.ragium.api.tag

import hiiragi283.core.api.tag.HTTagPrefix

/**
 * 参照 : [Hiiragi Core - CommonTagPrefixes][hiiragi283.core.api.tag.CommonTagPrefixes]
 */
data object RagiumTagPrefixes {
    @JvmField
    val PELLET: HTTagPrefix = HTTagPrefix("pellets", "pellets/%s")
}
