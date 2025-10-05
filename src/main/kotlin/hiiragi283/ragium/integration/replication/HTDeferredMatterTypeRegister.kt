package hiiragi283.ragium.integration.replication

import com.buuz135.replication.ReplicationRegistry
import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.extension.RegistryKey
import hiiragi283.ragium.api.extension.createKey
import hiiragi283.ragium.api.registry.HTDeferredRegister
import net.minecraft.resources.ResourceLocation
import java.awt.Color
import java.util.function.Function
import java.util.function.Supplier

class HTDeferredMatterTypeRegister(namespace: String) :
    HTDeferredRegister<IMatterType>(ReplicationRegistry.MATTER_TYPES_KEY, namespace) {
    fun <TYPE : IMatterType> registerType(type: TYPE): HTDeferredMatterType<TYPE> = register(type.name) { _: ResourceLocation -> type }

    fun registerType(name: String, color: Color, max: Int): HTDeferredMatterType<IMatterType> = register(name) { id: ResourceLocation ->
        object : IMatterType {
            override fun getName(): String = id.path

            override fun getColor(): Supplier<FloatArray> = Supplier {
                floatArrayOf(
                    color.red / 255f,
                    color.green / 255f,
                    color.blue / 255f,
                    1f,
                )
            }

            override fun getMax(): Int = max
        }
    }

    //    HTDeferredRegister    //

    override fun asSequence(): Sequence<HTDeferredMatterType<*>> = super.asSequence().filterIsInstance<HTDeferredMatterType<*>>()

    override fun getEntries(): Collection<HTDeferredMatterType<*>> = super.getEntries().filterIsInstance<HTDeferredMatterType<*>>()

    override fun <I : IMatterType> register(name: String, func: Function<ResourceLocation, out I>): HTDeferredMatterType<I> =
        super.register(name, func) as HTDeferredMatterType<I>

    override fun <I : IMatterType> register(name: String, sup: Supplier<out I>): HTDeferredMatterType<I> =
        super.register(name, sup) as HTDeferredMatterType<I>

    override fun <I : IMatterType> createHolder(registryKey: RegistryKey<IMatterType>, key: ResourceLocation): HTDeferredMatterType<I> =
        HTDeferredMatterType(registryKey.createKey(key))
}
