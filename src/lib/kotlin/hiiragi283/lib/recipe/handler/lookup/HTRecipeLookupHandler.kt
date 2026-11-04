package hiiragi283.lib.recipe.handler.lookup

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import net.minecraft.world.level.Level

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTRecipeLookupHandler {
    fun getLevel(): Level?

    fun createLookupContext(): HTRecipeLookup.Context = HTPhysicalSideHelper.createLookupContext(getLevel())

    fun setOnlySave()
}
