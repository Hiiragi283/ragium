package hiiragi283.lib.item.alchemy

import com.mojang.serialization.DataResult
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.fold
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType
import kotlin.jvm.optionals.getOrNull

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
data object HTPotionHelper {
    const val BOTTLE_AMOUNT: Int = FluidType.BUCKET_VOLUME / 4

    //    Data Component    //

    /**
     * 指定した[getter]から[PotionContents]を取得します。
     * @return 値を保持していない場合は[PotionContents.EMPTY]
     */
    @JvmStatic
    fun getContentsOrEmpty(getter: DataComponentGetter): PotionContents = getter.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)

    /**
     * 指定した[getter]から[PotionContents]を取得します。
     * @return 取得した[PotionContents]が空の場合は`null`
     * @since 26.1.8
     */
    @JvmStatic
    fun getContentsNotEmpty(getter: DataComponentGetter): PotionContents? = getContentsOrEmpty(getter).takeUnless(::isEmpty)

    /**
     * 指定した[contents]が空かどうか判定します。
     * @return [contents]が[PotionContents.EMPTY]と同じ場合，またはポーションもカスタムエフェクトもない場合は`true`
     */
    @JvmStatic
    fun isEmpty(contents: PotionContents): Boolean =
        contents == PotionContents.EMPTY || (contents.potion().isEmpty && contents.customEffects.isEmpty())

    @JvmStatic
    fun hasAnyEffect(getter: DataComponentGetter): Boolean = !getContentsOrEmpty(getter).let(::isEmpty)

    /**
     * @since 26.1.11
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    fun isWater(potion: Holder<Potion>): Boolean = potion.`is`(HTConstants.Keys.WATER) || potion.`is`(Potions.WATER)

    /**
     * @since 26.1.11
     */
    fun isWater(contents: PotionContents): Boolean = contents.potion().fold({ false }, ::isWater)

    // Patch
    @JvmStatic
    fun createPotionPatch(getter: DataComponentGetter): DataComponentPatch =
        getContentsNotEmpty(getter)?.let(::createPotionPatch) ?: DataComponentPatch.EMPTY

    @JvmStatic
    fun createPotionPatch(potion: Holder<Potion>): DataComponentPatch = createPotionPatch(PotionContents(potion))

    @JvmStatic
    fun createPotionPatch(contents: PotionContents): DataComponentPatch = when {
        isEmpty(contents) -> DataComponentPatch.EMPTY
        else -> buildDataPatch { set(DataComponents.POTION_CONTENTS, contents) }
    }

    // Potion Id

    @JvmStatic
    fun getPotionId(getter: DataComponentGetter): Identifier? = getContentsOrEmpty(getter).let(::getPotionId)

    @JvmStatic
    fun getPotionId(contents: PotionContents): Identifier? = contents
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .map(ResourceKey<Potion>::identifier)
        .getOrNull()

    // Name

    @JvmStatic
    fun getPotionName(getter: DataComponentGetter, bottleType: HTBottleType): Text = getPotionName(getContentsOrEmpty(getter), bottleType)

    @JvmStatic
    fun getPotionName(contents: PotionContents, bottleType: HTBottleType): Text = contents.getName("${bottleType.filledItem.translationKey}.effect.")

    //    ItemStack    //

    // Filled Bottle

    @JvmStatic
    fun createFilled(getter: DataComponentGetter, bottleType: HTBottleType): DataResult<ItemStackTemplate> =
        createFilled(getContentsOrEmpty(getter), bottleType)

    @JvmStatic
    fun createFilled(potion: Holder<Potion>, bottleType: HTBottleType): DataResult<ItemStackTemplate> =
        createFilled(PotionContents(potion), bottleType)

    @JvmStatic
    fun createFilled(contents: PotionContents, bottleType: HTBottleType): DataResult<ItemStackTemplate> =
        bottleType.filledItem.asTemplate(patch = createPotionPatch(contents))

    // Bucket

    @JvmStatic
    fun createBuket(getter: DataComponentGetter): DataResult<ItemStackTemplate> = getContentsOrEmpty(getter).let(::createBuket)

    @JvmStatic
    fun createBuket(potion: Holder<Potion>): DataResult<ItemStackTemplate> = createBuket(PotionContents(potion))

    @JvmStatic
    fun createBuket(contents: PotionContents): DataResult<ItemStackTemplate> = when {
        isEmpty(contents) -> DataResult.error { "Could not create Filled Bucket for empty potion" }

        isWater(contents) -> DataResult.success(ItemStackTemplate(Items.WATER_BUCKET))

        else -> HTPotionFluidAccess.INSTANCE.fluidContent.bucketHolder.asTemplate(
            patch = createPotionPatch(contents),
        )
    }

    //    FluidStack    //

    @JvmStatic
    fun createFluid(getter: DataComponentGetter, amount: Int = FluidType.BUCKET_VOLUME): DataResult<FluidStackTemplate> =
        createFluid(getContentsOrEmpty(getter), amount)

    @JvmStatic
    fun createFluid(potion: Holder<Potion>, amount: Int = FluidType.BUCKET_VOLUME): DataResult<FluidStackTemplate> = when {
        isWater(potion) -> DataResult.success(FluidStackTemplate(Fluids.WATER, amount))
        else -> HTPotionFluidAccess.INSTANCE.fluidContent.asTemplate(amount, createPotionPatch(potion))
    }

    @JvmStatic
    fun createFluid(contents: PotionContents, amount: Int = FluidType.BUCKET_VOLUME): DataResult<FluidStackTemplate> = when {
        isEmpty(contents) -> DataResult.error { "Could not create FluidStack for empty potion" }
        isWater(contents) -> DataResult.success(FluidStackTemplate(Fluids.WATER, amount))
        else -> HTPotionFluidAccess.INSTANCE.fluidContent.asTemplate(amount, createPotionPatch(contents))
    }
}
