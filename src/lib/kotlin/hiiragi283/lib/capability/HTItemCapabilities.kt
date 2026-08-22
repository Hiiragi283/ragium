package hiiragi283.lib.capability

import hiiragi283.lib.transfer.item.ItemResourceHandler
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * [ItemResource]向けの[HTResourceMultiCapability]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTItemCapabilities : HTResourceMultiCapability<ItemResource> {
    override val block: BlockCapability<ItemResourceHandler, Direction?> = Capabilities.Item.BLOCK
    override val entity: EntityCapability<ItemResourceHandler, Direction?> = Capabilities.Item.ENTITY_AUTOMATION
    override val item: ItemCapability<ItemResourceHandler, ItemAccess> = Capabilities.Item.ITEM

    override fun getCapability(entity: Entity, side: Direction?): ResourceHandler<ItemResource>? {
        if (side == null) {
            val handler: ItemResourceHandler? = entity.getCapability(Capabilities.Item.ENTITY)
            if (handler != null) return handler
        }
        return super.getCapability(entity, side)
    }
}
