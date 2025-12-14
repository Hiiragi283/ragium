package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.entity.typeHolder
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * Ragiumで使用する[DataMapType]へのアクセス
 * @see mekanism.api.datamaps.IMekanismDataMapTypes
 */
object RagiumDataMapTypes {
    // Entity Type
    @JvmField
    val MOB_HEAD: DataMapType<EntityType<*>, HTMobHead> = create("mob_head", Registries.ENTITY_TYPE, HTMobHead.CODEC)

    // Fluid
    @JvmField
    val COOLANT: DataMapType<Fluid, HTFluidCoolantData> = create("coolant", Registries.FLUID, HTFluidCoolantData.CODEC)

    @JvmField
    val MAGMATIC_FUEL: DataMapType<Fluid, HTFluidFuelData> = createFuel("magmatic")

    @JvmField
    val COMBUSTION_FUEL: DataMapType<Fluid, HTFluidFuelData> = createFuel("combustion")

    // Item
    @JvmField
    val ARMOR_EQUIP: DataMapType<Item, HTEquipAction> =
        create("armor_equip", Registries.ITEM, HTEquipAction.CODEC)

    @JvmField
    val UPGRADE: DataMapType<Item, HTUpgradeData> =
        create("upgrade", Registries.ITEM, HTUpgradeData.CODEC)

    //    Extensions    //

    /**
     * 指定した[entity]からエンチャントでドロップするモブの頭を取得します。
     */
    fun getMobHead(entity: Entity): ItemStack = entity.typeHolder.getData(MOB_HEAD)?.toStack() ?: ItemStack.EMPTY

    /**
     * 指定した[stack]から，一度の処理に必要な冷却材の使用量を取得します。
     */
    fun getCoolantAmount(stack: ImmutableFluidStack): Int = stack.getData(COOLANT)?.amount ?: 0

    /**
     * 指定した[stack]から，1000 mbの高温の液体による燃焼時間を取得します。
     */
    fun getTimeFromMagmatic(stack: ImmutableFluidStack): Int = stack.getData(MAGMATIC_FUEL)?.time ?: 0

    /**
     * 指定した[stack]から，1000 mbの液体燃料による燃焼時間を取得します。
     */
    fun getTimeFromCombustion(stack: ImmutableFluidStack): Int = stack.getData(COMBUSTION_FUEL)?.time ?: 0

    fun getEquipAction(stack: ItemStack): HTEquipAction? = stack.itemHolder.getData(ARMOR_EQUIP)

    /**
     * 指定した[stack]から，アップグレードのデータを取得します。
     */
    fun getUpgradeData(stack: ItemStack): HTUpgradeData? = stack.itemHolder.getData(UPGRADE)

    /**
     * 指定した[stack]から，アップグレードのデータを取得します。
     */
    fun getUpgradeData(stack: ImmutableItemStack): HTUpgradeData? = stack.getData(UPGRADE)

    @JvmStatic
    private fun <T : Any, R : Any> create(path: String, registryKey: ResourceKey<Registry<R>>, codec: BiCodec<*, T>): DataMapType<R, T> =
        create(path, registryKey, codec.codec)

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
