package hiiragi283.ragium.impl.data.map

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.HTBrewingEffect
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTMobHead
import hiiragi283.ragium.api.data.map.HTSolarPower
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.jvm.optionals.getOrNull

class RagiumDataMapsImpl : RagiumDataMaps {
    companion object {
        @JvmStatic
        private fun <T : Any, R : Any> create(path: String, registryKey: ResourceKey<Registry<R>>, codec: Codec<T>): DataMapType<R, T> =
            DataMapType
                .builder(RagiumAPI.id(path), registryKey, codec)
                .synced(codec, false)
                .build()

        @JvmStatic
        private fun createFuel(path: String): DataMapType<Fluid, HTFluidFuelData> =
            create("fuel/$path", Registries.FLUID, HTFluidFuelData.CODEC)
    }

    override val thermalFuelType: DataMapType<Fluid, HTFluidFuelData> = createFuel("thermal")
    override val combustionFuelType: DataMapType<Fluid, HTFluidFuelData> = createFuel("combustion")
    override val nuclearFuelType: DataMapType<Fluid, HTFluidFuelData> = createFuel("nuclear")
    override val solarPowerType: DataMapType<Block, HTSolarPower> = create("solar_power", Registries.BLOCK, HTSolarPower.CODEC)

    override val brewingEffectType: DataMapType<Item, HTBrewingEffect> = create("brewing/effect", Registries.ITEM, HTBrewingEffect.CODEC)

    override val mobHeadType: DataMapType<EntityType<*>, HTMobHead> = create("mob_head", Registries.ENTITY_TYPE, HTMobHead.CODEC)

    override fun <TYPE : Any, DATA : Any> getData(
        access: RegistryAccess,
        registryKey: RegistryKey<TYPE>,
        holder: Holder<TYPE>,
        type: DataMapType<TYPE, DATA>,
    ): DATA? = when (Holder.Kind.REFERENCE) {
        holder.kind() -> holder.getData(type)
        else ->
            access
                .registry(registryKey)
                .getOrNull()
                ?.wrapAsHolder(holder.value())
                ?.getData(type)
    }
}
