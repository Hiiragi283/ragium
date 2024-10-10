package hiiragi283.ragium.api.recipe.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.component.ComponentType
import net.minecraft.entity.EntityType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome

object HTRecipeComponentTypes {
    @JvmField
    val BIOME: ComponentType<RegistryKey<Biome>> =
        registerId("biome", { RegistryKey.of(RegistryKeys.BIOME, it) }, RegistryKey<Biome>::getValue)

    @JvmField
    val ENTITY_TYPE: ComponentType<EntityType<*>> =
        registerId("entity_type", Registries.ENTITY_TYPE::get, Registries.ENTITY_TYPE::getId)

    @JvmField
    val REQUIRE_SCAN: ComponentType<Boolean> = register("require_scan", Codec.BOOL, PacketCodecs.BOOL)

    @JvmStatic
    private fun <T : Any> register(name: String, codec: Codec<T>, packetCodec: PacketCodec<in RegistryByteBuf, T>): ComponentType<T> =
        Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            RagiumAPI.id(name),
            ComponentType
                .builder<T>()
                .codec(codec)
                .packetCodec(packetCodec)
                .build(),
        )

    @JvmStatic
    private fun <T : Any> registerId(name: String, to: (Identifier) -> T, from: (T) -> Identifier): ComponentType<T> =
        register(name, Identifier.CODEC.xmap(to, from), Identifier.PACKET_CODEC.xmap(to, from))
}
