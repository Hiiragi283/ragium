package hiiragi283.ragium.api

import com.mojang.logging.LogUtils
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.map.HTEquipAction
import hiiragi283.ragium.api.data.map.HTSubEntityTypeIngredient
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.api.registry.toId
import net.minecraft.core.Registry
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.RegistryBuilder
import org.slf4j.Logger
import java.util.ServiceLoader

/**
 * @see mekanism.api.MekanismAPI
 */
object RagiumAPI {
    /**
     * Ragiumで使用する名前空間
     */
    const val MOD_ID = "ragium"

    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    //    ResourceLocation    //

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun id(path: String): ResourceLocation = MOD_ID.toId(path)

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun id(vararg path: String): ResourceLocation = MOD_ID.toId(*path)

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun wrapId(other: ResourceLocation): ResourceLocation = when (other.namespace) {
        MOD_ID -> other
        else -> id(other.path)
    }

    //    Registry    //

    @JvmStatic
    private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(id(path))

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder<T>(key)
        .sync(true)
        .create()

    // Builtin
    @JvmField
    val EQUIP_ACTION_TYPE_KEY: ResourceKey<Registry<MapCodec<out HTEquipAction>>> = createKey("equip_action_type")

    @JvmField
    val EQUIP_ACTION_TYPE_REGISTRY: Registry<MapCodec<out HTEquipAction>> = createRegistry(EQUIP_ACTION_TYPE_KEY)

    @JvmField
    val SLOT_TYPE_KEY: ResourceKey<Registry<StreamCodec<RegistryFriendlyByteBuf, out HTSyncablePayload>>> = createKey("syncable_slot_type")

    @JvmField
    val SLOT_TYPE_REGISTRY: Registry<StreamCodec<RegistryFriendlyByteBuf, out HTSyncablePayload>> = createRegistry(SLOT_TYPE_KEY)

    @JvmField
    val SUB_ENTITY_INGREDIENT_TYPE_KEY: ResourceKey<Registry<MapCodec<out HTSubEntityTypeIngredient>>> = createKey("entity_ingredient_type")

    @JvmField
    val SUB_ENTITY_INGREDIENT_TYPE_REGISTRY: Registry<MapCodec<out HTSubEntityTypeIngredient>> = createRegistry(
        SUB_ENTITY_INGREDIENT_TYPE_KEY,
    )

    //    Service    //

    /**
     * @see mekanism.api.MekanismAPI.getService
     */
    @Suppress("UnstableApiUsage")
    @JvmStatic
    inline fun <reified SERVICE : Any> getService(): SERVICE =
        ServiceLoader.load(SERVICE::class.java, RagiumAPI::class.java.classLoader).first()
}
