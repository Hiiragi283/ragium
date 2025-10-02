package hiiragi283.ragium.integration.replication

import com.buuz135.replication.ReplicationRegistry
import com.buuz135.replication.api.IMatterType
import com.buuz135.replication.calculation.MatterValue
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.extension.toId
import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class HTDeferredMatterType<TYPE : IMatterType> :
    HTDeferredHolder<IMatterType, TYPE>,
    IMatterType {
    companion object {
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

    constructor(key: ResourceKey<IMatterType>) : super(key)

    constructor(id: ResourceLocation) : super(ReplicationRegistry.MATTER_TYPES_KEY, id)

    override fun getName(): String = get().name

    override fun getColor(): Supplier<FloatArray> = get().color

    override fun getMax(): Int = get().max

    fun toValue(amount: Double): MatterValue = MatterValue(get(), amount)
}
