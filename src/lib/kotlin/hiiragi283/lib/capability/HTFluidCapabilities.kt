package hiiragi283.lib.capability

import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.fluid.FluidResource

/**
 * [FluidResource]向けの[HTResourceMultiCapability]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTFluidCapabilities : HTResourceMultiCapability<FluidResource> {
    override val block: BlockCapability<FluidResourceHandler, Direction?> = Capabilities.Fluid.BLOCK
    override val entity: EntityCapability<FluidResourceHandler, Direction?> = Capabilities.Fluid.ENTITY
    override val item: ItemCapability<FluidResourceHandler, ItemAccess> = Capabilities.Fluid.ITEM
}
