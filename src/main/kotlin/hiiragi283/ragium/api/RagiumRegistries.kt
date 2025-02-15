package hiiragi283.ragium.api

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.recipe.base.HTItemResult
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * Ragiumが追加する[Registry]
 */
object RagiumRegistries {
    /**
     * [HTItemResult]の[MapCodec]の[Registry]
     */
    @JvmField
    val ITEM_RESULT: Registry<MapCodec<out HTItemResult>> =
        RegistryBuilder(Keys.ITEM_RESULT).sync(true).create()

    /**
     * Ragiumが追加する[Registry]の[ResourceKey]
     */
    object Keys {
        @JvmField
        val ITEM_RESULT: ResourceKey<Registry<MapCodec<out HTItemResult>>> =
            ResourceKey.createRegistryKey(RagiumAPI.id("item_result"))
    }
}
