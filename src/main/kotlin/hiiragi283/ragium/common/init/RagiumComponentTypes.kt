package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.common.item.component.HTSpawnerContent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumComponentTypes {
    @JvmField
    val REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    //    Fluid    //

    @JvmField
    val MOLTEN_MATERIAL: DeferredHolder<DataComponentType<*>, DataComponentType<HTMaterialKey>> =
        REGISTER.registerComponentType("molten_material") { builder: DataComponentType.Builder<HTMaterialKey> ->
            builder.persistent(HTMaterialKey.CODEC).networkSynchronized(HTMaterialKey.STREAM_CODEC)
        }

    //    Item    //

    @JvmField
    val FLUID_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<SimpleFluidContent>> =
        REGISTER.registerComponentType("fluid_content") { builder: DataComponentType.Builder<SimpleFluidContent> ->
            builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC)
        }

    @JvmField
    val POTION_BUNDLE_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<List<PotionContents>>> =
        REGISTER.registerComponentType("potion_bundle_content") { builder: DataComponentType.Builder<List<PotionContents>> ->
            builder
                .persistent(PotionContents.CODEC.listOf(0, 9))
                .networkSynchronized(PotionContents.STREAM_CODEC.toList())
        }

    @JvmField
    val SPAWNER_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<HTSpawnerContent>> =
        REGISTER.registerComponentType("spawner") { builder: DataComponentType.Builder<HTSpawnerContent> ->
            builder.persistent(HTSpawnerContent.CODEC).networkSynchronized(HTSpawnerContent.STREAM_CODEC)
        }
}
