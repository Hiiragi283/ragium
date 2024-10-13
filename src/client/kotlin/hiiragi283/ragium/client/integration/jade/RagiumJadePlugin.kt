package hiiragi283.ragium.client.integration.jade

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.util.Identifier
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.IWailaPlugin

object RagiumJadePlugin : IWailaPlugin {
    @JvmField
    val MACHINE: Identifier = RagiumAPI.id("machine")

    @JvmField
    val NETWORK_INTERFACE: Identifier = RagiumAPI.id("network_interface")

    override fun register(registration: IWailaCommonRegistration) {
        registration.registerBlock(HTMachineProvider, RagiumBlocks.META_MACHINE)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlock(HTMachineProvider, RagiumBlocks.META_MACHINE)
        registration.registerBlock(HTEnergyNetworkProvider, RagiumBlocks.NETWORK_INTERFACE)

        RagiumAPI.log { info("Jade integration enabled!") }
    }
}
