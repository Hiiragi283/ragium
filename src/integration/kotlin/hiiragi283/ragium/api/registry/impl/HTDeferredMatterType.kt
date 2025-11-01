package hiiragi283.ragium.api.registry.impl

import com.buuz135.replication.ReplicationRegistry
import com.buuz135.replication.api.IMatterType
import com.buuz135.replication.calculation.MatterValue
import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class HTDeferredMatterType<TYPE : IMatterType> :
    HTDeferredHolder<IMatterType, TYPE>,
    IMatterType {
    constructor(key: ResourceKey<IMatterType>) : super(key)

    constructor(id: ResourceLocation) : super(ReplicationRegistry.MATTER_TYPES_KEY, id)

    override fun getName(): String = get().name

    override fun getColor(): Supplier<FloatArray> = get().color

    override fun getMax(): Int = get().max

    fun toValue(amount: Double): MatterValue = MatterValue(get(), amount)
}
