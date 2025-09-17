package hiiragi283.ragium.setup

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTLootTicketTargets
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.DyeColor
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
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>,
    ): Supplier<DataComponentType<T>> = REGISTER.registerComponentType<T>(name) { builder: DataComponentType.Builder<T> ->
        builder.persistent(codec).networkSynchronized(streamCodec)
    }

    @JvmStatic
    private fun <T : Any> register(name: String, codec: BiCodec<in RegistryFriendlyByteBuf, T>): Supplier<DataComponentType<T>> =
        register(name, codec.codec, codec.streamCodec)

    @JvmField
    val ANTI_GRAVITY: Supplier<DataComponentType<Boolean>> = register("anti_gravity", BiCodec.BOOL)

    @JvmField
    val BLAST_POWER: Supplier<DataComponentType<Float>> = register("blast_power", BiCodecs.POSITIVE_FLOAT)

    @JvmField
    val COLOR: Supplier<DataComponentType<DyeColor>> = register("color", BiCodecs.COLOR)

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
    val ENERGY: Supplier<DataComponentType<Int>> = register("energy", BiCodecs.NON_NEGATIVE_INT)

    @JvmField
    val FLUID_CONTENT: Supplier<DataComponentType<SimpleFluidContent>> =
        register("fluid_content", SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)

    @JvmField
    val IMMUNE_DAMAGE_TYPES: Supplier<DataComponentType<HTKeyOrTagEntry<DamageType>>> =
        register("immune_damage_types", HTKeyOrTagHelper.INSTANCE.codec(Registries.DAMAGE_TYPE))

    @JvmField
    val INTRINSIC_ENCHANTMENT: Supplier<DataComponentType<HTIntrinsicEnchantment>> =
        register("intrinsic_enchantment", HTIntrinsicEnchantment.CODEC)

    @JvmField
    val LOOT_TICKET: Supplier<DataComponentType<HTLootTicketTargets>> = register("loot_ticket", HTLootTicketTargets.CODEC)

    @JvmField
    val TELEPORT_POS: Supplier<DataComponentType<HTTeleportPos>> = register("teleport_pos", HTTeleportPos.CODEC)
}
