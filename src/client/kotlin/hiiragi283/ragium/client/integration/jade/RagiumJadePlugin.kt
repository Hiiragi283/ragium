package hiiragi283.ragium.client.integration.jade

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.util.Identifier
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.IWailaPlugin

object RagiumJadePlugin : IWailaPlugin {
    @JvmField
    val BURNING_BOX: Identifier = RagiumAPI.id("burning_box")

    @JvmField
    val MACHINE: Identifier = RagiumAPI.id("machine")

    override fun register(registration: IWailaCommonRegistration) {
        // registration.registerBlock(HTBurningBoxProvider, RagiumContents.BURNING_BOX)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        // registration.registerBlock(HTBurningBoxProvider, RagiumContents.BURNING_BOX)
    }
}
