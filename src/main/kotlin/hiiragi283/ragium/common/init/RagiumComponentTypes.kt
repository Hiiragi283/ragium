package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.GlobalPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.Unit
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumComponentTypes {
    @JvmField
    val REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Any> register(
        name: String,
        codec: Codec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
    ): DeferredHolder<DataComponentType<*>, DataComponentType<T>> =
        REGISTER.registerComponentType<T>(name) { builder: DataComponentType.Builder<T> ->
            builder.persistent(codec).networkSynchronized(streamCodec)
        }

    @JvmStatic
    fun registerFlag(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
        register(name, Unit.CODEC, StreamCodec.unit(Unit.INSTANCE))

    @JvmField
    val FLUID_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<SimpleFluidContent>> =
        register("fluid_content", SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)

    @JvmField
    val IS_ACTIVE: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> = registerFlag("is_active")

    @JvmField
    val EFFECT_RANGE: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
        register("effect_range", ExtraCodecs.POSITIVE_INT, ByteBufCodecs.VAR_INT.cast<RegistryFriendlyByteBuf>())

    @JvmField
    val TELEPORT_POS: DeferredHolder<DataComponentType<*>, DataComponentType<GlobalPos>> =
        register("teleport_pos", GlobalPos.CODEC, GlobalPos.STREAM_CODEC.cast<RegistryFriendlyByteBuf>())

    @JvmField
    val LOOT_TABLE_ID: DeferredHolder<DataComponentType<*>, DataComponentType<ResourceLocation>> =
        register("loot_table_id", ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC.cast<RegistryFriendlyByteBuf>())
}
