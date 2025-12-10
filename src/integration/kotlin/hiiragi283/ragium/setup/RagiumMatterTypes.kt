package hiiragi283.ragium.setup

import com.buuz135.replication.ReplicationRegistry
import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.common.material.RagiumEssenceType
import net.minecraft.resources.ResourceLocation
import java.awt.Color
import java.util.function.Supplier

object RagiumMatterTypes {
    @JvmField
    val REGISTER: HTDeferredRegister<IMatterType> = HTDeferredRegister(ReplicationRegistry.MATTER_TYPES_KEY, RagiumAPI.MOD_ID)

    @JvmField
    val MATTER_TYPES: Map<RagiumEssenceType, HTDeferredHolder<IMatterType, IMatterType>> =
        RagiumEssenceType.entries.associateWith { type: RagiumEssenceType ->
            registerType(type.asMaterialName(), type.color)
        }

    @JvmStatic
    private fun registerType(name: String, color: Color, max: Int = 128): HTDeferredHolder<IMatterType, IMatterType> =
        REGISTER.register(name) { id: ResourceLocation ->
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

    @JvmStatic
    fun getTypeHolder(essence: RagiumEssenceType): HTDeferredHolder<IMatterType, IMatterType> = MATTER_TYPES[essence]!!

    @JvmStatic
    fun getType(essence: RagiumEssenceType): IMatterType = getTypeHolder(essence).get()
}
