package hiiragi283.ragium.common.resource

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
import net.minecraft.registry.RegistryWrapper

data class HTHardModeResourceCondition(val value: Boolean) : ResourceCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTHardModeResourceCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Codec.BOOL.optionalFieldOf("value", false).forGetter(HTHardModeResourceCondition::value),
                ).apply(instance, ::HTHardModeResourceCondition)
        }

        @JvmField
        val TYPE: ResourceConditionType<HTHardModeResourceCondition> = ResourceConditionType.create(
            RagiumAPI.id("hard_mode"),
            CODEC,
        )

        init {
            ResourceConditions.register(TYPE)
        }
    }

    override fun getType(): ResourceConditionType<*> = TYPE

    override fun test(registryLookup: RegistryWrapper.WrapperLookup?): Boolean = value == RagiumAPI.getInstance().isHardMode
}
