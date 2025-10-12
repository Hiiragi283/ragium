package hiiragi283.ragium.common.integration.replication

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.common.material.RagiumEssenceType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus

object RagiumReplicationAddon : RagiumAddon {
    //    Matter    //

    @JvmField
    val MATTER_REGISTER = HTDeferredMatterTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val MATTER_MAP: Map<RagiumEssenceType, HTDeferredMatterType<IMatterType>> = RagiumEssenceType.entries.associateWith { type ->
        MATTER_REGISTER.registerType(type.materialName(), type.color, 128)
    }

    @JvmStatic
    fun getMatterType(essence: RagiumEssenceType): HTDeferredMatterType<IMatterType> = MATTER_MAP[essence]!!

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        MATTER_REGISTER.register(eventBus)
    }
}
