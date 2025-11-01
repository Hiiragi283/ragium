package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.HTAmountView
import hiiragi283.ragium.api.storage.experience.HTExperienceHandler
import hiiragi283.ragium.api.storage.experience.IExperienceHandler
import hiiragi283.ragium.api.storage.experience.IExperienceHandlerItem
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability

object HTExperienceCapabilities : HTAmountViewCapability<IExperienceHandler, IExperienceHandlerItem, Long> {
    private val id: ResourceLocation = RagiumAPI.id("experience")
    override val block: BlockCapability<IExperienceHandler, Direction?> = BlockCapability.createSided(id, IExperienceHandler::class.java)
    override val entity: EntityCapability<IExperienceHandler, Direction?> = EntityCapability.createSided(id, IExperienceHandler::class.java)
    override val item: ItemCapability<IExperienceHandlerItem, Void?> = ItemCapability.createVoid(id, IExperienceHandlerItem::class.java)

    override fun apply(handler: IExperienceHandler, context: Direction?): List<HTAmountView<Long>> = if (handler is HTExperienceHandler) {
        handler.getExpTanks(context)
    } else {
        (0..<handler.getExperienceTanks()).map { tank: Int ->
            object : HTAmountView.LongSized {
                override fun getAmount(): Long = handler.getExperienceAmount(tank)

                override fun getCapacity(): Long = handler.getExperienceCapacity(tank)
            }
        }
    }
}
