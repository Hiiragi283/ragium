package hiiragi283.ragium.setup

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTTeleportPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.storage.loot.LootTable
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumDataComponents {
    @JvmField
    val REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Any> register(
        name: String,
        codec: Codec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
    ): Supplier<DataComponentType<T>> = REGISTER.registerComponentType<T>(name) { builder: DataComponentType.Builder<T> ->
        builder.persistent(codec).networkSynchronized(streamCodec)
    }

    @JvmStatic
    private fun <T : Any> register(name: String, codec: BiCodec<RegistryFriendlyByteBuf, T>): Supplier<DataComponentType<T>> =
        register(name, codec.codec, codec.streamCodec)

    @JvmField
    val BLAST_POWER: Supplier<DataComponentType<Float>> = register("blast_power", BiCodecs.POSITIVE_FLOAT.cast())

    @JvmField
    val DRINK_SOUND: Supplier<DataComponentType<SoundEvent>> = register(
        "drinking_sound",
        BiCodecs.registryBased(BuiltInRegistries.SOUND_EVENT),
    )

    @JvmField
    val EAT_SOUND: Supplier<DataComponentType<SoundEvent>> = register(
        "eating_sound",
        BiCodecs.registryBased(BuiltInRegistries.SOUND_EVENT),
    )

    @JvmField
    val ENERGY: Supplier<DataComponentType<Int>> = register("energy", BiCodecs.NON_NEGATIVE_INT.cast())

    @JvmField
    val FLUID_CONTENT: Supplier<DataComponentType<SimpleFluidContent>> =
        register("fluid_content", SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)

    @JvmField
    val INTRINSIC_ENCHANTMENT: Supplier<DataComponentType<HTIntrinsicEnchantment>> =
        register("intrinsic_enchantment", HTIntrinsicEnchantment.CODEC.cast())

    @JvmField
    val IS_ACTIVE: Supplier<DataComponentType<Boolean>> = register("is_active", BiCodec.BOOL.cast())

    @JvmField
    val LOOT_TABLE_ID: Supplier<DataComponentType<ResourceKey<LootTable>>> =
        register("loot_table_id", BiCodecs.resourceKey(Registries.LOOT_TABLE).cast())

    @JvmField
    val TELEPORT_POS: Supplier<DataComponentType<HTTeleportPos>> = register("teleport_pos", HTTeleportPos.CODEC.cast())
}
