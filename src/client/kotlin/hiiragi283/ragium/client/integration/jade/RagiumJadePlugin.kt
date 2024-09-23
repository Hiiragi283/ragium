package hiiragi283.ragium.client.integration.jade

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.util.Identifier
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.IWailaPlugin

object RagiumJadePlugin : IWailaPlugin {
    @JvmField
    val BURNING_BOX: Identifier = Ragium.id("burning_box")

    @JvmField
    val MACHINE: Identifier = Ragium.id("machine")

    override fun register(registration: IWailaCommonRegistration) {
        registration.registerBlock(HTBurningBoxProvider, RagiumContents.BURNING_BOX)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlock(HTBurningBoxProvider, RagiumContents.BURNING_BOX)
    }
}
