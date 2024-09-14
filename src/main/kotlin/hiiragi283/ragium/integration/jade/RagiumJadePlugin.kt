package hiiragi283.ragium.integration.jade

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumBlocks
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
        registration.registerBlock(HTBurningBoxProvider, RagiumBlocks.BURNING_BOX)
    }

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlock(HTBurningBoxProvider, RagiumBlocks.BURNING_BOX)
    }

}