package hiiragi283.ragium.setup

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.impl.HTDeferredMatterType
import hiiragi283.ragium.api.registry.toId

object DefaultMatterTypes {
    @JvmStatic
    fun create(name: String): HTDeferredMatterType<IMatterType> = HTDeferredMatterType(RagiumConst.REPLICATION.toId(name))

    @JvmField
    val EMPTY: HTDeferredMatterType<IMatterType> = create("empty")

    @JvmField
    val METALLIC: HTDeferredMatterType<IMatterType> = create("metallic")

    @JvmField
    val EARTH: HTDeferredMatterType<IMatterType> = create("earth")

    @JvmField
    val NETHER: HTDeferredMatterType<IMatterType> = create("nether")

    @JvmField
    val ORGANIC: HTDeferredMatterType<IMatterType> = create("organic")

    @JvmField
    val ENDER: HTDeferredMatterType<IMatterType> = create("ender")

    @JvmField
    val PRECIOUS: HTDeferredMatterType<IMatterType> = create("precious")

    @JvmField
    val QUANTUM: HTDeferredMatterType<IMatterType> = create("quantum")

    @JvmField
    val LIVING: HTDeferredMatterType<IMatterType> = create("living")
}
