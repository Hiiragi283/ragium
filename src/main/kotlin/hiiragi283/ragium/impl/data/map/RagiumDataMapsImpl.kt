package hiiragi283.ragium.impl.data.map

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.HTEquipAction
import hiiragi283.ragium.api.data.map.HTFluidCoolantData
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.HTMobHead
import hiiragi283.ragium.api.data.map.HTRuntimeRecipeProvider
import hiiragi283.ragium.api.data.map.HTSubEntityTypeIngredient
import hiiragi283.ragium.api.data.map.IdMapDataMap
import hiiragi283.ragium.api.data.map.MapDataMapValueRemover
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.registry.RegistryKey
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger
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

    override val mobHeadType: DataMapType<EntityType<*>, HTMobHead> = create("mob_head", Registries.ENTITY_TYPE, HTMobHead.CODEC)

    override val coolantType: DataMapType<Fluid, HTFluidCoolantData> =
        create("coolant", Registries.FLUID, HTFluidCoolantData.CODEC)
    override val magmaticFuelType: DataMapType<Fluid, HTFluidFuelData> = createFuel("magmatic")
    override val combustionFuelType: DataMapType<Fluid, HTFluidFuelData> = createFuel("combustion")

    override val armorEquipType: DataMapType<Item, HTEquipAction> = create("armor_equip", Registries.ITEM, HTEquipAction.CODEC)
    override val subEntityIngredientType: DataMapType<Item, HTSubEntityTypeIngredient> =
        create("sub_entity_ingredient", Registries.ITEM, HTSubEntityTypeIngredient.CODEC)

    override val materialRecipeType: IdMapDataMap<RecipeType<*>, HTRuntimeRecipeProvider> =
        AdvancedDataMapType
            .builder(RagiumAPI.id("runtime_recipe"), Registries.RECIPE_TYPE, HTRuntimeRecipeProvider.ID_MAP_CODEC)
            .synced(HTRuntimeRecipeProvider.ID_MAP_CODEC, false)
            .merger(DataMapValueMerger.mapMerger())
            .remover(MapDataMapValueRemover.codec())
            .build()

    override fun <TYPE : Any, DATA : Any> getData(
        access: RegistryAccess,
        registryKey: RegistryKey<TYPE>,
        holder: Holder<TYPE>,
        type: DataMapType<TYPE, DATA>,
    ): DATA? = when (holder.kind()) {
        Holder.Kind.REFERENCE -> holder.getData(type)
        else ->
            access
                .registry(registryKey)
                .getOrNull()
                ?.wrapAsHolder(holder.value())
                ?.getData(type)
    }
}
