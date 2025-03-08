package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.common.component.HTSpawnerData
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.Unit
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumComponentTypes {
    @JvmField
    val REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    fun registerFlag(path: String): DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
        REGISTER.registerComponentType(path) { builder: DataComponentType.Builder<Unit> ->
            builder
                .persistent(Unit.CODEC)
                .networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
        }

    //    Fluid    //

    //    Item    //

    @JvmField
    val ENERGY_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
        REGISTER.registerComponentType("energy_content") { builder: DataComponentType.Builder<Int> ->
            builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
        }

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

    // Tools
    @JvmField
    val IS_ACTIVE: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> = registerFlag("is_active")

    @JvmField
    val EFFECT_RANGE: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
        REGISTER.registerComponentType("effect_range") { builder: DataComponentType.Builder<Int> ->
            builder
                .persistent(ExtraCodecs.POSITIVE_INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT)
        }

    @JvmField
    val SPAWNER_DATA: DeferredHolder<DataComponentType<*>, DataComponentType<HTSpawnerData>> =
        REGISTER.registerComponentType("spawner_data") { builder: DataComponentType.Builder<HTSpawnerData> ->
            builder
                .persistent(HTSpawnerData.CODEC)
                .networkSynchronized(HTSpawnerData.STREAM_CODEC)
        }

    @JvmField
    val EXPLOSION_POWER: DeferredHolder<DataComponentType<*>, DataComponentType<Float>> =
        REGISTER.registerComponentType("explosion_power") { builder: DataComponentType.Builder<Float> ->
            builder
                .persistent(Codec.floatRange(0f, 16f))
                .networkSynchronized(ByteBufCodecs.FLOAT)
        }
}
