package hiiragi283.ragium.api.data.map

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapType

typealias IdMapDataMap<R, V> = AdvancedDataMapType<R, Map<ResourceLocation, V>, MapDataMapValueRemover<R, V>>

/**
 * Ragiumで使用する[DataMapType]へのアクセス
 * @see mekanism.api.datamaps.IMekanismDataMapTypes
 */
interface RagiumDataMaps {
    companion object {
        /**
         * [RagiumDataMaps]の実装のインスタンス
         */
        @JvmField
        val INSTANCE: RagiumDataMaps = RagiumAPI.getService()

        @JvmField
        val ENCHANT_FUEL: DataMapType<Enchantment, LevelBasedValue> = INSTANCE.enchFuelType

        @JvmField
        val MOB_HEAD: DataMapType<EntityType<*>, HTMobHead> = INSTANCE.mobHeadType

        @JvmField
        val THERMAL_FUEL: DataMapType<Fluid, HTFluidFuelData> = INSTANCE.thermalFuelType

        @JvmField
        val COMBUSTION_FUEL: DataMapType<Fluid, HTFluidFuelData> = INSTANCE.combustionFuelType

        @JvmField
        val NUCLEAR_FUEL: DataMapType<Fluid, HTFluidFuelData> = INSTANCE.nuclearFuelType

        @JvmField
        val ARMOR_EQUIP: DataMapType<Item, HTEquipAction> = INSTANCE.armorEquipType

        @JvmField
        val MATERIAL_RECIPE: IdMapDataMap<RecipeType<*>, HTMaterialRecipeData> = INSTANCE.materialRecipeType
    }

    val enchFuelType: DataMapType<Enchantment, LevelBasedValue>

    val mobHeadType: DataMapType<EntityType<*>, HTMobHead>

    val thermalFuelType: DataMapType<Fluid, HTFluidFuelData>
    val combustionFuelType: DataMapType<Fluid, HTFluidFuelData>
    val nuclearFuelType: DataMapType<Fluid, HTFluidFuelData>

    val armorEquipType: DataMapType<Item, HTEquipAction>

    val materialRecipeType: IdMapDataMap<RecipeType<*>, HTMaterialRecipeData>

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

    fun getEnchBasedValue(access: RegistryAccess, holder: Holder<Enchantment>, level: Int): Int? =
        getData(access, Registries.ENCHANTMENT, holder, enchFuelType)?.calculate(level)?.toInt()

    /**
     * 指定した値からエンチャントでドロップするモブの頭を取得します。
     */
    fun getMobHead(access: RegistryAccess, holder: Holder<EntityType<*>>): ItemStack =
        getData(access, Registries.ENTITY_TYPE, holder, mobHeadType)?.toStack() ?: ItemStack.EMPTY
}
