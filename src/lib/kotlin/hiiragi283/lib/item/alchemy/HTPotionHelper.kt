package hiiragi283.lib.item.alchemy

import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.text.Text
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
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import java.util.Optional

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
data object HTPotionHelper {
    /**
     * 指定した[contents]が空かどうか判定します。
     * @return [contents]が[PotionContents.EMPTY]と同じ場合，またはポーションもカスタムエフェクトもない場合は`true`
     * @since 26.1.6
     */
    @JvmStatic
    fun isEmpty(contents: PotionContents): Boolean =
        contents == PotionContents.EMPTY || (contents.potion().isEmpty && contents.customEffects.isEmpty())

    //    Data Component    //

    /**
     * 指定した[getter]から[PotionContents]を取得します。
     * @return 値を保持していない場合は[PotionContents.EMPTY]
     */
    @JvmStatic
    fun getContents(getter: DataComponentGetter): PotionContents =
        getter.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun createPotionPatch(potion: Holder<Potion>): DataComponentPatch = createPotionPatch(PotionContents(potion))

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun createPotionPatch(contents: PotionContents): DataComponentPatch =
        buildDataPatch { set(DataComponents.POTION_CONTENTS, contents) }

    // Potion Id

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun getPotionId(getter: DataComponentGetter): Optional<Identifier> = getContents(getter).let(::getPotionId)

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun getPotionId(contents: PotionContents): Optional<Identifier> = contents
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .map(ResourceKey<Potion>::identifier)

    // Name

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun getPotionName(getter: DataComponentGetter, bottleType: HTBottleType): Text =
        getPotionName(getContents(getter), bottleType)

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun getPotionName(contents: PotionContents, bottleType: HTBottleType): Text =
        contents.getName("${bottleType.filledItem.descriptionId}.effect.")

    //    ItemStack    //

    // Filled Bottle

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun createFilled(getter: DataComponentGetter, bottleType: HTBottleType): ItemStackTemplate =
        createFilled(getContents(getter), bottleType)

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun createFilled(potion: Holder<Potion>, bottleType: HTBottleType): ItemStackTemplate =
        createFilled(PotionContents(potion), bottleType)

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun createFilled(contents: PotionContents, bottleType: HTBottleType): ItemStackTemplate =
        ItemStackTemplate(bottleType.filledItem, 1, createPotionPatch(contents))

    // Bucket

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun createBuket(getter: DataComponentGetter): ItemStackTemplate = getContents(getter).let(::createBuket)

    /**
     * @since 26.1.6
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    fun createBuket(potion: Holder<Potion>): ItemStackTemplate = createBuket(PotionContents(potion))

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun createBuket(contents: PotionContents): ItemStackTemplate = when {
        contents.`is`(Potions.WATER) -> ItemStackTemplate(Items.WATER_BUCKET)
        else -> HTPotionFluidAccess.INSTANCE.fluidContent.bucketHolder.toTemplate(patch = createPotionPatch(contents))!!
    }

    //    FluidStack    //

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun createFluid(getter: DataComponentGetter, amount: Int = FluidType.BUCKET_VOLUME): FluidStack =
        createFluid(getContents(getter), amount)

    /**
     * @since 26.1.6
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    fun createFluid(potion: Holder<Potion>, amount: Int = FluidType.BUCKET_VOLUME): FluidStack =
        createFluid(PotionContents(potion), amount)

    /**
     * @since 26.1.6
     */
    @JvmStatic
    fun createFluid(contents: PotionContents, amount: Int = FluidType.BUCKET_VOLUME): FluidStack = when {
        contents.`is`(Potions.WATER) -> FluidStack(Fluids.WATER, amount)
        else -> HTPotionFluidAccess.INSTANCE.fluidContent.toStack(amount, createPotionPatch(contents))
    }
}
