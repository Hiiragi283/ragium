package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumComponentTypes {
    @JvmField
    val REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val DRUM_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<SimpleFluidContent>> =
        REGISTER.registerComponentType("drum_content") { builder: DataComponentType.Builder<SimpleFluidContent> ->
            builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC)
        }

    @JvmField
    val MACHINE_TIER: DeferredHolder<DataComponentType<*>, DataComponentType<HTMachineTier>> =
        REGISTER.registerComponentType("tier") { builder: DataComponentType.Builder<HTMachineTier> ->
            builder.persistent(HTMachineTier.CODEC).networkSynchronized(HTMachineTier.STREAM_CODEC)
        }

    @JvmField
    val MATERIAL: DeferredHolder<DataComponentType<*>, DataComponentType<HTMaterialKey>> =
        REGISTER.registerComponentType("material") { builder: DataComponentType.Builder<HTMaterialKey> ->
            builder.persistent(HTMaterialKey.CODEC).networkSynchronized(HTMaterialKey.STREAM_CODEC)
        }

    @JvmField
    val TAG_PREFIX: DeferredHolder<DataComponentType<*>, DataComponentType<HTTagPrefix>> =
        REGISTER.registerComponentType("tag_prefix") { builder: DataComponentType.Builder<HTTagPrefix> ->
            builder.persistent(HTTagPrefix.CODEC).networkSynchronized(HTTagPrefix.STREAM_CODEC)
        }
}
