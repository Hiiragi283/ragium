package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.entity.typeHolder
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger

typealias IdMapDataMap<R, V> = AdvancedDataMapType<R, Map<ResourceLocation, V>, MapDataMapValueRemover<R, V>>

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
    val SUB_ENTITY_INGREDIENT: DataMapType<Item, HTSubEntityTypeIngredient> =
        create("sub_entity_ingredient", Registries.ITEM, HTSubEntityTypeIngredient.CODEC)

    // Recipe Type
    @JvmField
    val MATERIAL_RECIPE: IdMapDataMap<RecipeType<*>, HTRuntimeRecipeProvider> =
        AdvancedDataMapType
            .builder(RagiumAPI.id("runtime_recipe"), Registries.RECIPE_TYPE, HTRuntimeRecipeProvider.ID_MAP_CODEC)
            .synced(HTRuntimeRecipeProvider.ID_MAP_CODEC, false)
            .merger(DataMapValueMerger.mapMerger())
            .remover(MapDataMapValueRemover.codec())
            .build()

    //    Extensions    //

    /**
     * 指定した値からエンチャントでドロップするモブの頭を取得します。
     */
    fun getMobHead(entity: Entity): ItemStack = entity.typeHolder.getData(MOB_HEAD)?.toStack() ?: ItemStack.EMPTY

    /**
     * 指定した値から，一度の処理に必要な冷却材の使用量を取得します。
     */
    fun getCoolantAmount(stack: ImmutableFluidStack): Int = stack.getData(COOLANT)?.amount ?: 0

    /**
     * 指定した値から，1000 mbの高温の液体による燃焼時間を取得します。
     */
    fun getTimeFromMagmatic(stack: ImmutableFluidStack): Int = stack.getData(MAGMATIC_FUEL)?.time ?: 0

    /**
     * 指定した値から，1000 mbの液体燃料による燃焼時間を取得します。
     */
    fun getTimeFromCombustion(stack: ImmutableFluidStack): Int = stack.getData(COMBUSTION_FUEL)?.time ?: 0

    fun getEquipAction(stack: ItemStack): HTEquipAction? = stack.itemHolder.getData(ARMOR_EQUIP)

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
