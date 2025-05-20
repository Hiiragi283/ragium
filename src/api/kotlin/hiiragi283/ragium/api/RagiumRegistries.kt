package hiiragi283.ragium.api

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * [net.neoforged.neoforge.registries.NeoForgeRegistries]
 */
object RagiumRegistries {
    @JvmField
    val BLOCK_ACTION_SERIALIZERS: Registry<MapCodec<out HTBlockAction>> =
        RegistryBuilder(Keys.BLOCK_ACTION_SERIALIZERS).sync(true).create()

    object Keys {
        @JvmField
        val BLOCK_ACTION_SERIALIZERS: ResourceKey<Registry<MapCodec<out HTBlockAction>>> =
            ResourceKey.createRegistryKey(RagiumAPI.id("block_action_serializers"))
    }
}
