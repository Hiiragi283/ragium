package hiiragi283.ragium.common.integration.ae2

import appeng.api.AECapabilities
import hiiragi283.core.common.integration.ae2.storage.HTFluidTankMEStorage
import hiiragi283.core.common.integration.ae2.storage.HTItemSlotMEStorage
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

/**
 * @see hiiragi283.core.common.integration.ae2.HCAEIntegration
 */
data object RagiumAEIntegration {
    //    Setup    //

    @JvmStatic
    internal fun init(eventBus: IEventBus) {
        eventBus.addListener(::registerMeStorage)
    }

    @JvmStatic
    private fun registerMeStorage(event: RegisterCapabilitiesEvent) {
        listOf(RagiumBlockEntityTypes.CRATE, RagiumBlockEntityTypes.CREATIVE_CRATE)
            .forEach { blockEntityType ->
                event.registerBlockEntity(AECapabilities.ME_STORAGE, blockEntityType.get()) { blockEntity, _ ->
                    HTItemSlotMEStorage(blockEntity.slot, blockEntity.name)
                }
            }

        listOf(RagiumBlockEntityTypes.TANK, RagiumBlockEntityTypes.VOID_TANK, RagiumBlockEntityTypes.CREATIVE_TANK)
            .forEach { blockEntityType ->
                event.registerBlockEntity(AECapabilities.ME_STORAGE, blockEntityType.get()) { blockEntity, _ ->
                    HTFluidTankMEStorage(blockEntity.tank, blockEntity.name)
                }
            }
    }
}
