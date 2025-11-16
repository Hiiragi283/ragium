package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.text.HTTranslation

/**
 * @see mekanism.common.block.interfaces.IHasDescription
 */
fun interface HTBlockWithDescription {
    fun getDescription(): HTTranslation
}
