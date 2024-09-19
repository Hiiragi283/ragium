package hiiragi283.ragium.common.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.common.Ragium
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.registry.RegistryWrapper

class HTHardModeResourceCondition private constructor(private val isHardMode: Boolean) : ResourceCondition {
    companion object {
        @JvmField
        val TRUE = HTHardModeResourceCondition(true)

        @JvmField
        val FALSE = HTHardModeResourceCondition(false)

        @JvmStatic
        fun fromBool(isHard: Boolean): HTHardModeResourceCondition = when (isHard) {
            true -> TRUE
            false -> FALSE
        }

        @JvmField
        val TYPE: ResourceConditionType<HTHardModeResourceCondition> =
            ResourceConditionType.create(
                Ragium.id("hard_mode"),
                RecordCodecBuilder.mapCodec { instance ->
                    instance
                        .group(Codec.BOOL.fieldOf("value").forGetter { it.isHardMode })
                        .apply(instance, Companion::fromBool)
                },
            )

        @JvmStatic
        fun init() {
            ResourceConditions.register(TYPE)
        }
    }

    override fun getType(): ResourceConditionType<*> = TYPE

    override fun test(registryLookup: RegistryWrapper.WrapperLookup?): Boolean =
        Ragium.config.isHardMode == isHardMode || FabricLoader.getInstance().isDevelopmentEnvironment
}
