package hiiragi283.ragium.integration.replication

import com.buuz135.replication.ReplicationRegistry
import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

@HTAddon(RagiumConst.REPLICATION)
object RagiumReplicationAddon : RagiumAddon {
    //    Matter    //

    @JvmField
    val MATTER_REGISTER: DeferredRegister<IMatterType> =
        DeferredRegister.create(ReplicationRegistry.MATTER_TYPES_KEY, RagiumAPI.MOD_ID)

    @JvmField
    val MATTER_RAGIUM: DeferredHolder<IMatterType, IMatterType> = matter(RagiumMatterType.RAGIUM)

    @JvmStatic
    private fun matter(type: IMatterType): DeferredHolder<IMatterType, IMatterType> =
        MATTER_REGISTER.register(type.name) { _: ResourceLocation -> type }

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        MATTER_REGISTER.register(eventBus)
    }
}
