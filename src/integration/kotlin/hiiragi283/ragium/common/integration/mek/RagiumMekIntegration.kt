package hiiragi283.ragium.common.integration.mek

import net.neoforged.bus.api.IEventBus

data object RagiumMekIntegration {
    //    Setup    //

    @JvmStatic
    internal fun init(eventBus: IEventBus) {
        RagiumChemicals.REGISTER.register(eventBus)
        RagiumMekItems.REGISTER.register(eventBus)
    }
}
