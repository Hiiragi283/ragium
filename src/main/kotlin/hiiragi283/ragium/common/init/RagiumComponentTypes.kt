package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.component.HTSpawnerContent
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
    val FLUID_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<SimpleFluidContent>> =
        REGISTER.registerComponentType("fluid_content") { builder: DataComponentType.Builder<SimpleFluidContent> ->
            builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC)
        }

    @JvmField
    val SPAWNER_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<HTSpawnerContent>> =
        REGISTER.registerComponentType("spawner") { builder: DataComponentType.Builder<HTSpawnerContent> ->
            builder.persistent(HTSpawnerContent.CODEC).networkSynchronized(HTSpawnerContent.STREAM_CODEC)
        }
}
