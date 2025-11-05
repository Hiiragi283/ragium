package hiiragi283.ragium.api

import com.mojang.logging.LogUtils
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.map.HTMaterialRecipeData
import hiiragi283.ragium.api.data.registry.HTBrewingEffect
import hiiragi283.ragium.api.data.registry.HTSolarPower
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.toId
import net.minecraft.core.Registry
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

    /**
     * Ragiumの表示名
     */
    const val MOD_NAME = "Ragium"

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
    fun id(prefix: String, suffix: String): ResourceLocation = id("$prefix/$suffix")

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun wrapId(other: ResourceLocation): ResourceLocation = when (other.namespace) {
        MOD_ID -> other
        else -> id(other.path)
    }

    //    Registry    //

    @JvmField
    val BREWING_EFFECT_KEY: ResourceKey<Registry<HTBrewingEffect>> = ResourceKey.createRegistryKey(id("brewing_effect"))

    @JvmField
    val MATERIAL_PREFIX_KEY: ResourceKey<Registry<HTMaterialPrefix>> = ResourceKey.createRegistryKey(id("material_prefix"))

    @JvmField
    val MATERIAL_PREFIX_REGISTRY: Registry<HTMaterialPrefix> = RegistryBuilder(MATERIAL_PREFIX_KEY)
        .sync(true)
        .onAdd { _, _, key: ResourceKey<HTMaterialPrefix>, prefix: HTMaterialPrefix ->
            val id: ResourceLocation = key.location()
            check(id.namespace == RagiumConst.COMMON) { "Only allowed `common` namespace for Material Prefix" }
        }.create()

    @JvmField
    val MATERIAL_RECIPE_TYPE_KEY: ResourceKey<Registry<MapCodec<out HTMaterialRecipeData>>> = ResourceKey.createRegistryKey(
        id("material_recipe_type"),
    )

    @JvmField
    val MATERIAL_RECIPE_TYPE_REGISTRY: Registry<MapCodec<out HTMaterialRecipeData>> = RegistryBuilder(MATERIAL_RECIPE_TYPE_KEY)
        .sync(true)
        .create()

    @JvmField
    val SOLAR_POWER_KEY: ResourceKey<Registry<HTSolarPower>> = ResourceKey.createRegistryKey(id("solar_power"))

    //    Service    //

    /**
     * @see mekanism.api.MekanismAPI.getService
     */
    @Suppress("UnstableApiUsage")
    @JvmStatic
    inline fun <reified SERVICE : Any> getService(): SERVICE =
        ServiceLoader.load(SERVICE::class.java, RagiumAPI::class.java.classLoader).first()
}
