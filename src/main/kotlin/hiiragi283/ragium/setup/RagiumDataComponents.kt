package hiiragi283.ragium.setup

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTPotionBundle
import net.minecraft.core.GlobalPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.util.ExtraCodecs
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
    private fun <T : Any> register(
        name: String,
        codec: BiCodec<RegistryFriendlyByteBuf, T>,
    ): Supplier<DataComponentType<T>> = register(name, codec.codec, codec.streamCodec)
    
    @JvmField
    val BLAST_POWER: Supplier<DataComponentType<Float>> =
        register("blast_power", ExtraCodecs.POSITIVE_FLOAT, ByteBufCodecs.FLOAT.cast())

    @JvmField
    val ENERGY: Supplier<DataComponentType<Int>> =
        register("energy", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT.cast())

    @JvmField
    val FLUID_CONTENT: Supplier<DataComponentType<SimpleFluidContent>> =
        register("fluid_content", SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)

    @JvmField
    val INTRINSIC_ENCHANTMENT: Supplier<DataComponentType<HTIntrinsicEnchantment>> =
        register("intrinsic_enchantment", HTIntrinsicEnchantment.CODEC.cast())

    @JvmField
    val IS_ACTIVE: Supplier<DataComponentType<Boolean>> = register("is_active", Codec.BOOL, ByteBufCodecs.BOOL.cast())

    @JvmField
    val LOOT_TABLE_ID: Supplier<DataComponentType<ResourceKey<LootTable>>> = register(
        "loot_table_id",
        ResourceKey.codec(Registries.LOOT_TABLE),
        ResourceKey.streamCodec(Registries.LOOT_TABLE).cast(),
    )

    @JvmField
    val POTION_BUNDLE: Supplier<DataComponentType<HTPotionBundle>> = register("potion_bundle", HTPotionBundle.CODEC_NEW)

    @JvmField
    val TELEPORT_POS: Supplier<DataComponentType<GlobalPos>> =
        register("teleport_pos", GlobalPos.CODEC, GlobalPos.STREAM_CODEC.cast())
}
