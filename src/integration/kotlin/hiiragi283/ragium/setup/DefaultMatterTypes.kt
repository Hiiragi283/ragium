package hiiragi283.ragium.setup

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.impl.HTDeferredMatterType
import hiiragi283.ragium.api.registry.toId

object DefaultMatterTypes {
    @JvmField
    val EMPTY = HTDeferredMatterType<IMatterType>(RagiumConst.REPLICATION.toId("empty"))

    @JvmField
    val METALLIC = HTDeferredMatterType<IMatterType>(RagiumConst.REPLICATION.toId("metallic"))

    @JvmField
    val EARTH = HTDeferredMatterType<IMatterType>(RagiumConst.REPLICATION.toId("earth"))

    @JvmField
    val NETHER = HTDeferredMatterType<IMatterType>(RagiumConst.REPLICATION.toId("nether"))

    @JvmField
    val ORGANIC = HTDeferredMatterType<IMatterType>(RagiumConst.REPLICATION.toId("organic"))

    @JvmField
    val ENDER = HTDeferredMatterType<IMatterType>(RagiumConst.REPLICATION.toId("ender"))

    @JvmField
    val PRECIOUS = HTDeferredMatterType<IMatterType>(RagiumConst.REPLICATION.toId("precious"))

    @JvmField
    val QUANTUM = HTDeferredMatterType<IMatterType>(RagiumConst.REPLICATION.toId("quantum"))

    @JvmField
    val LIVING = HTDeferredMatterType<IMatterType>(RagiumConst.REPLICATION.toId("living"))
}
