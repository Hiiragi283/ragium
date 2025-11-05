package hiiragi283.ragium.common.storage.experience

import hiiragi283.ragium.api.storage.experience.HTExperienceHandler
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.storage.experience.IExperienceHandlerItem
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack

/**
 * [HTExperienceHandler]に基づいたコンポーネント向けの実装
 */
class HTComponentExperienceHandler(private val parent: ItemStack, private val tank: HTExperienceTank) :
    IExperienceHandlerItem,
    HTExperienceHandler {
    override fun getContainer(): ItemStack = parent

    override fun getExpTanks(side: Direction?): List<HTExperienceTank> = listOf(tank)
}
