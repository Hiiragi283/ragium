package hiiragi283.ragium.api.data.map

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * Ragiumで使用する[DataMapType]へのアクセス
 * @see [mekanism.api.datamaps.IMekanismDataMapTypes]
 */
interface RagiumDataMaps {
    companion object {
        /**
         * [RagiumDataMaps]の実装のインスタンス
         */
        @JvmField
        val INSTANCE: RagiumDataMaps = RagiumAPI.getService()

        @JvmField
        val THERMAL_FUEL: DataMapType<Fluid, HTFluidFuelData> = INSTANCE.thermalFuelType

        @JvmField
        val COMBUSTION_FUEL: DataMapType<Fluid, HTFluidFuelData> = INSTANCE.combustionFuelType

        @JvmField
        val NUCLEAR_FUEL: DataMapType<Fluid, HTFluidFuelData> = INSTANCE.nuclearFuelType

        @JvmField
        val SOLAR_POWER: DataMapType<Block, HTSolarPower> = INSTANCE.solarPowerType

        @JvmField
        val BREWING_EFFECT: DataMapType<Item, HTBrewingEffect> = INSTANCE.brewingEffectType

        @JvmField
        val MOB_HEAD: DataMapType<EntityType<*>, HTMobHead> = INSTANCE.mobHeadType
    }

    val thermalFuelType: DataMapType<Fluid, HTFluidFuelData>
    val combustionFuelType: DataMapType<Fluid, HTFluidFuelData>
    val nuclearFuelType: DataMapType<Fluid, HTFluidFuelData>
    val solarPowerType: DataMapType<Block, HTSolarPower>

    val brewingEffectType: DataMapType<Item, HTBrewingEffect>

    val mobHeadType: DataMapType<EntityType<*>, HTMobHead>

    /**
     * 指定した引数からデータを取得します。
     * @param TYPE レジストリの種類のクラス
     * @param DATA 取得するデータのクラス
     * @param access すべてのレジストリへのアクセス
     * @param registryKey レジストリのキー
     * @param holder [TYPE]の値を保持する[Holder]
     * @param type [DATA]を取得するための[DataMapType]
     * @return 見つからない場合は`null`
     */
    fun <TYPE : Any, DATA : Any> getData(
        access: RegistryAccess,
        registryKey: RegistryKey<TYPE>,
        holder: Holder<TYPE>,
        type: DataMapType<TYPE, DATA>,
    ): DATA?

    /**
     * 指定した値から火力発電機の液体燃料の消費量を取得します。
     */
    fun getThermalFuel(access: RegistryAccess, holder: Holder<Fluid>): Int =
        getData(access, Registries.FLUID, holder, thermalFuelType)?.amount ?: 0

    /**
     * 指定した値から燃焼発電機の液体燃料の消費量を取得します。
     */
    fun getCombustionFuel(access: RegistryAccess, holder: Holder<Fluid>): Int =
        getData(access, Registries.FLUID, holder, combustionFuelType)?.amount ?: 0

    /**
     * 指定した値から原子炉の液体燃料の消費量を取得します。
     */
    fun getNuclearFuel(access: RegistryAccess, holder: Holder<Fluid>): Int =
        getData(access, Registries.FLUID, holder, nuclearFuelType)?.amount ?: 0

    fun getSolarPower(access: RegistryAccess, holder: Holder<Block>): Float? =
        getData(access, Registries.BLOCK, holder, solarPowerType)?.multiplier

    /**
     * 指定した値から醸造機で作成するポーションの効果を取得します。
     */
    fun getBrewingEffect(access: RegistryAccess, holder: Holder<Item>): HTBrewingEffect? =
        getData(access, Registries.ITEM, holder, brewingEffectType)

    /**
     * 指定した値からエンチャントでドロップするモブの頭を取得します。
     */
    fun getMobHead(access: RegistryAccess, holder: Holder<EntityType<*>>): ItemStack =
        getData(access, Registries.ENTITY_TYPE, holder, mobHeadType)?.toStack() ?: ItemStack.EMPTY
}
