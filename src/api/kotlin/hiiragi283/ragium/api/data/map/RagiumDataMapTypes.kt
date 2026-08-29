package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * Ragiumで使用する[DataMapType]へのアクセス
 * @see mekanism.api.datamaps.IMekanismDataMapTypes
 */
data object RagiumDataMapTypes {
    // Fluid
    @JvmField
    val COOLANT: DataMapType<Fluid, Int> = create("coolant", Registries.FLUID, HTCodecs.POSITIVE_INT)

    @JvmField
    val MAGMATIC_FUEL: DataMapType<Fluid, Int> = createFuel("magmatic")

    @JvmField
    val COMBUSTION_FUEL: DataMapType<Fluid, Int> = createFuel("combustion")

    //    Extensions    //

    /**
     * 指定した[resource]から，一度の処理に必要な冷却材の使用量を取得します。
     */
    @JvmStatic
    fun getCoolantAmount(resource: HTFluidResourceType): Int = resource.getData(COOLANT) ?: 0

    /**
     * 指定した[resource]から，100 mbの高温の液体による燃焼時間を取得します。
     */
    @JvmStatic
    fun getTimeFromMagmatic(resource: HTFluidResourceType): Int = resource.getData(MAGMATIC_FUEL) ?: 0

    /**
     * 指定した[resource]から，100 mbの液体燃料による燃焼時間を取得します。
     */
    @JvmStatic
    fun getTimeFromCombustion(resource: HTFluidResourceType): Int = resource.getData(COMBUSTION_FUEL) ?: 0

    @JvmStatic
    private fun <T : Any, R : Any> create(path: String, registryKey: ResourceKey<Registry<R>>, codec: Codec<T>): DataMapType<R, T> = DataMapType
        .builder(RagiumAPI.id(path), registryKey, codec)
        .synced(codec, false)
        .build()

    @JvmStatic
    private fun createFuel(path: String): DataMapType<Fluid, Int> = create("fuel/$path", Registries.FLUID, HTCodecs.POSITIVE_INT)
}
