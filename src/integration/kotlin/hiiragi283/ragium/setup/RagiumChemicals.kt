package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import mekanism.common.registration.impl.ChemicalDeferredRegister
import mekanism.common.registration.impl.DeferredChemical
import net.neoforged.bus.api.IEventBus

object RagiumChemicals {
    @JvmField
    val REGISTER = ChemicalDeferredRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.addAlias(RagiumAPI.id("raginite"), RagiumAPI.id("ragium"))

        REGISTER.register(eventBus)
    }

    @JvmField
    val CHEMICALS: Map<HTMaterialKey, DeferredChemical<*>> = buildMap {
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            val key: HTMaterialKey = essenceType.asMaterialKey()
            this[key] = REGISTER.registerInfuse(key.name, essenceType.color.rgb)
        }

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            this[data.asMaterialKey()] = REGISTER.register(data.molten.getPath(), data.color)
        }
    }

    @JvmStatic
    fun getChemical(material: HTMaterialLike): DeferredChemical<*> =
        CHEMICALS[material.asMaterialKey()] ?: error("Unknown chemical for ${material.asMaterialName()}")
}
