package hiiragi283.ragium.common.integration.mek

import hiiragi283.ragium.api.RagiumAPI
import mekanism.api.chemical.Chemical
import mekanism.common.registration.impl.ChemicalDeferredRegister
import mekanism.common.registration.impl.DeferredChemical

/**
 * @see mekanism.common.registries.MekanismChemicals
 */
data object RagiumChemicals {
    @JvmField
    val REGISTER = ChemicalDeferredRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGINITE: DeferredChemical<Chemical> = REGISTER.registerInfuse("raginite", 0xFF003F)
}
