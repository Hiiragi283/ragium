package hiiragi283.ragium.integration.replication

import com.buuz135.replication.ReplicationRegistry
import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.HTDeferredRegister
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus

@HTAddon(RagiumConst.REPLICATION)
object RagiumReplicationAddon : RagiumAddon {
    //    Matter    //

    @JvmField
    val MATTER_REGISTER: HTDeferredRegister<IMatterType> =
        HTDeferredRegister(ReplicationRegistry.MATTER_TYPES_KEY, RagiumAPI.MOD_ID)

    @JvmField
    val MATTER_RAGIUM: HTDeferredHolder<IMatterType, IMatterType> = matter(RagiumMatterType.RAGIUM)

    @JvmStatic
    private fun matter(type: IMatterType): HTDeferredHolder<IMatterType, IMatterType> =
        MATTER_REGISTER.register(type.name) { _: ResourceLocation -> type }

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        MATTER_REGISTER.register(eventBus)
    }
}
