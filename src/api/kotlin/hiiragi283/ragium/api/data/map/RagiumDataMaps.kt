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

        // Entity Type
        @JvmField
        val MOB_HEAD: DataMapType<EntityType<*>, HTMobHead> = INSTANCE.mobHeadType

        // Fluid
        @JvmField
        val COOLANT: DataMapType<Fluid, HTFluidCoolantData> = INSTANCE.coolantType

        @JvmField
        val MAGMATIC_FUEL: DataMapType<Fluid, HTFluidFuelData> = INSTANCE.magmaticFuelType

        @JvmField
        val COMBUSTION_FUEL: DataMapType<Fluid, HTFluidFuelData> = INSTANCE.combustionFuelType

        // Item
        @JvmField
        val ARMOR_EQUIP: DataMapType<Item, HTEquipAction> = INSTANCE.armorEquipType

        @JvmField
        val SUB_ENTITY_INGREDIENT: DataMapType<Item, HTSubEntityTypeIngredient> = INSTANCE.subEntityIngredientType

        // Recipe Type
        @JvmField
        val MATERIAL_RECIPE: IdMapDataMap<RecipeType<*>, HTRuntimeRecipeProvider> = INSTANCE.materialRecipeType
    }

    val mobHeadType: DataMapType<EntityType<*>, HTMobHead>

    val coolantType: DataMapType<Fluid, HTFluidCoolantData>
    val magmaticFuelType: DataMapType<Fluid, HTFluidFuelData>
    val combustionFuelType: DataMapType<Fluid, HTFluidFuelData>

    val armorEquipType: DataMapType<Item, HTEquipAction>
    val subEntityIngredientType: DataMapType<Item, HTSubEntityTypeIngredient>

    val materialRecipeType: IdMapDataMap<RecipeType<*>, HTRuntimeRecipeProvider>

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
     * 指定した値からエンチャントでドロップするモブの頭を取得します。
     */
    fun getMobHead(access: RegistryAccess, holder: Holder<EntityType<*>>): ItemStack =
        getData(access, Registries.ENTITY_TYPE, holder, mobHeadType)?.toStack() ?: ItemStack.EMPTY

    /**
     * 指定した値から高温の液体燃料による発電量を取得します。
     */
    fun getCoolantAmount(access: RegistryAccess, holder: Holder<Fluid>): Int =
        getData(access, Registries.FLUID, holder, coolantType)?.amount ?: 0

    /**
     * 指定した値から高温の液体燃料による発電量を取得します。
     */
    fun getEnergyFromMagmatic(access: RegistryAccess, holder: Holder<Fluid>): Int =
        getData(access, Registries.FLUID, holder, magmaticFuelType)?.energy ?: 0

    /**
     * 指定した値から液体燃料による発電量を取得します。
     */
    fun getEnergyFromCombustion(access: RegistryAccess, holder: Holder<Fluid>): Int =
        getData(access, Registries.FLUID, holder, combustionFuelType)?.energy ?: 0
}
