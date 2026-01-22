package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType
import org.apache.commons.lang3.math.Fraction

/**
 * Ragiumで使用する[DataMapType]へのアクセス
 * @see mekanism.api.datamaps.IMekanismDataMapTypes
 */
object RagiumDataMapTypes {
    // Block
    @JvmField
    val FERMENT_SOURCE: DataMapType<Block, Int> = create("ferment_source", Registries.BLOCK, BiCodecs.POSITIVE_INT)

    // Entity Type
    @JvmField
    val MOB_HEAD: DataMapType<EntityType<*>, HTItemHolderLike<*>> = create(
        "mob_head",
        Registries.ENTITY_TYPE,
        HTItemHolderLike.HOLDER_CODEC,
    )

    // Fluid
    @JvmField
    val COOLANT: DataMapType<Fluid, Int> = create("coolant", Registries.FLUID, BiCodecs.POSITIVE_INT)

    @JvmField
    val MAGMATIC_FUEL: DataMapType<Fluid, Int> = createFuel("magmatic")

    @JvmField
    val COMBUSTION_FUEL: DataMapType<Fluid, Int> = createFuel("combustion")

    @JvmField
    val FERTILIZER: DataMapType<Fluid, Fraction> = create("fertilizer", Registries.FLUID, BiCodecs.POSITIVE_FRACTION)

    // Item
    @JvmField
    val UPGRADE: DataMapType<Item, HTUpgradeData> =
        create("upgrade", Registries.ITEM, HTUpgradeData.CODEC)

    //    Extensions    //

    fun getFermentLevel(getter: BlockGetter, centerPos: BlockPos): Int = BlockPos
        .betweenClosed(centerPos.offset(-1, 0, -1), centerPos.offset(1, -1, 1))
        .asSequence()
        .map(getter::getBlockState)
        .mapNotNull(::getFermentLevel)
        .sum()

    fun getFermentLevel(state: BlockState): Int? = state.blockHolder.getData(FERMENT_SOURCE)

    /**
     * 指定した[entity]からエンチャントでドロップするモブの頭を取得します。
     */
    @Suppress("DEPRECATION")
    fun getMobHead(entity: Entity): ItemStack = entity.type
        .builtInRegistryHolder()
        .getData(MOB_HEAD)
        .let(::createItemStack)

    /**
     * 指定した[resource]から，一度の処理に必要な冷却材の使用量を取得します。
     */
    fun getCoolantAmount(resource: HTFluidResourceType): Int = resource.getData(COOLANT) ?: 0

    /**
     * 指定した[resource]から，100 mbの高温の液体による燃焼時間を取得します。
     */
    fun getTimeFromMagmatic(resource: HTFluidResourceType): Int = resource.getData(MAGMATIC_FUEL) ?: 0

    /**
     * 指定した[resource]から，100 mbの液体燃料による燃焼時間を取得します。
     */
    fun getTimeFromCombustion(resource: HTFluidResourceType): Int = resource.getData(COMBUSTION_FUEL) ?: 0

    fun getFluidFertilizer(resource: HTFluidResourceType): Fraction? = resource.getData(FERTILIZER)

    /**
     * 指定した[stack]から，アップグレードのデータを取得します。
     */
    fun getUpgradeData(stack: ItemStack): HTUpgradeData? = stack.itemHolder.getData(UPGRADE)

    /**
     * 指定した[resource]から，アップグレードのデータを取得します。
     */
    fun getUpgradeData(resource: HTItemResourceType): HTUpgradeData? = resource.getData(UPGRADE)

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
    private fun createFuel(path: String): DataMapType<Fluid, Int> = create("fuel/$path", Registries.FLUID, BiCodecs.POSITIVE_INT)
}
