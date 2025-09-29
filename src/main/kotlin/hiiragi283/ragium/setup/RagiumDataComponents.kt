package hiiragi283.ragium.setup

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.item.component.HTLootTicketTargets
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.fluids.SimpleFluidContent

object RagiumDataComponents {
    @JvmField
    val REGISTER: HTDeferredRegister<DataComponentType<*>> =
        HTDeferredRegister(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Any> register(
        name: String,
        codec: Codec<T>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>,
    ): DataComponentType<T> {
        val type: DataComponentType<T> = DataComponentType
            .builder<T>()
            .persistent(codec)
            .networkSynchronized(streamCodec)
            .build()
        REGISTER.register(name) { _: ResourceLocation -> type }
        return type
    }

    @JvmStatic
    private fun <T : Any> register(name: String, codec: BiCodec<in RegistryFriendlyByteBuf, T>): DataComponentType<T> =
        register(name, codec.codec, codec.streamCodec)

    @JvmField
    val ANTI_GRAVITY: DataComponentType<Boolean> = register("anti_gravity", BiCodec.BOOL)

    @JvmField
    val BLAST_POWER: DataComponentType<Float> = register("blast_power", BiCodecs.POSITIVE_FLOAT)

    @JvmField
    val COLOR: DataComponentType<DyeColor> = register("color", BiCodecs.COLOR)

    @JvmField
    val DRINK_SOUND: DataComponentType<HTItemSoundEvent> = register("drinking_sound", HTItemSoundEvent.CODEC)

    @JvmField
    val EAT_SOUND: DataComponentType<HTItemSoundEvent> = register("eating_sound", HTItemSoundEvent.CODEC)

    @JvmField
    val ENERGY: DataComponentType<Int> = register("energy", BiCodecs.NON_NEGATIVE_INT)

    @JvmField
    val FLUID_CONTENT: DataComponentType<SimpleFluidContent> =
        register("fluid_content", SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)

    @JvmField
    val IMMUNE_DAMAGE_TYPES: DataComponentType<HTKeyOrTagEntry<DamageType>> =
        register("immune_damage_types", HTKeyOrTagHelper.INSTANCE.codec(Registries.DAMAGE_TYPE))

    @JvmField
    val INTRINSIC_ENCHANTMENT: DataComponentType<HTIntrinsicEnchantment> =
        register("intrinsic_enchantment", HTIntrinsicEnchantment.CODEC)

    @JvmField
    val LOOT_TICKET: DataComponentType<HTLootTicketTargets> = register("loot_ticket", HTLootTicketTargets.CODEC)

    @JvmField
    val TELEPORT_POS: DataComponentType<HTTeleportPos> = register("teleport_pos", HTTeleportPos.CODEC)
}
