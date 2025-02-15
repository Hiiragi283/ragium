package hiiragi283.ragium.integration

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalBuilder
import mekanism.common.registration.impl.ChemicalDeferredRegister
import mekanism.common.registration.impl.SlurryRegistryObject
import org.slf4j.Logger

object RagiumMekIntegration {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Chemical    //

    @JvmField
    val CHEMICAL_REGISTER = ChemicalDeferredRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGINITE_SLURRY: SlurryRegistryObject<Chemical, Chemical> =
        CHEMICAL_REGISTER.registerSlurry("raginite") { builder: ChemicalBuilder ->
            builder.tint(0xff003f).ore(HTTagPrefix.ORE.createTag(RagiumMaterials.RAGINITE))
        }
}
