package hiiragi283.ragium.setup

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.impl.HTDeferredMatterType
import hiiragi283.ragium.api.registry.impl.HTDeferredMatterTypeRegister
import hiiragi283.ragium.common.material.RagiumEssenceType

object RagiumMatterTypes {
    @JvmField
    val REGISTER = HTDeferredMatterTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val MATTER_TYPES: Map<RagiumEssenceType, HTDeferredMatterType<IMatterType>> =
        RagiumEssenceType.entries.associateWith { type: RagiumEssenceType ->
            REGISTER.registerType(type.asMaterialName(), type.color, 128)
        }

    @JvmStatic
    fun getType(essence: RagiumEssenceType): HTDeferredMatterType<IMatterType> = MATTER_TYPES[essence]!!
}
