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
    const val BOTTLE_AMOUNT: Int = FluidType.BUCKET_VOLUME / 4

    //    Data Component    //

    /**
     * 指定した[getter]から[PotionContents]を取得します。
     * @return 値を保持していない場合は[PotionContents.EMPTY]
     */
    @JvmStatic
    fun getContents(getter: DataComponentGetter): PotionContents =
        getter.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)

    @JvmStatic
    fun isEmpty(getter: DataComponentGetter): Boolean = getContents(getter).let(::isEmpty)

    /**
     * 指定した[contents]が空かどうか判定します。
     * @return [contents]が[PotionContents.EMPTY]と同じ場合，またはポーションもカスタムエフェクトもない場合は`true`
     */
    @JvmStatic
    fun isEmpty(contents: PotionContents): Boolean =
        contents == PotionContents.EMPTY || (contents.potion().isEmpty && contents.customEffects.isEmpty())

    @JvmStatic
    fun hasAnyEffect(getter: DataComponentGetter): Boolean = !getContents(getter).let(::isEmpty)

    // Patch
    @JvmStatic
    fun createPotionPatch(getter: DataComponentGetter): DataComponentPatch =
        getContents(getter).let(::createPotionPatch)

    @JvmStatic
    fun createPotionPatch(potion: Holder<Potion>): DataComponentPatch = createPotionPatch(PotionContents(potion))

    @JvmStatic
    fun createPotionPatch(contents: PotionContents): DataComponentPatch =
        buildDataPatch { set(DataComponents.POTION_CONTENTS, contents) }

    // Potion Id

    @JvmStatic
    fun getPotionId(getter: DataComponentGetter): Optional<Identifier> = getContents(getter).let(::getPotionId)

    @JvmStatic
    fun getPotionId(contents: PotionContents): Optional<Identifier> = contents
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .map(ResourceKey<Potion>::identifier)

    // Name

    @JvmStatic
    fun getPotionName(getter: DataComponentGetter, bottleType: HTBottleType): Text =
        getPotionName(getContents(getter), bottleType)

    @JvmStatic
    fun getPotionName(contents: PotionContents, bottleType: HTBottleType): Text =
        contents.getName("${bottleType.filledItem.descriptionId}.effect.")

    //    ItemStack    //

    // Filled Bottle

    @JvmStatic
    fun createFilled(getter: DataComponentGetter, bottleType: HTBottleType): ItemStackTemplate =
        createFilled(getContents(getter), bottleType)

    @JvmStatic
    fun createFilled(potion: Holder<Potion>, bottleType: HTBottleType): ItemStackTemplate =
        createFilled(PotionContents(potion), bottleType)

    @JvmStatic
    fun createFilled(contents: PotionContents, bottleType: HTBottleType): ItemStackTemplate =
        ItemStackTemplate(bottleType.filledItem, 1, createPotionPatch(contents))

    // Bucket

    @JvmStatic
    fun createBuket(getter: DataComponentGetter): ItemStackTemplate = getContents(getter).let(::createBuket)

    @Suppress("DEPRECATION")
    @JvmStatic
    fun createBuket(potion: Holder<Potion>): ItemStackTemplate = createBuket(PotionContents(potion))

    @JvmStatic
    fun createBuket(contents: PotionContents): ItemStackTemplate = when {
        contents.`is`(Potions.WATER) -> ItemStackTemplate(Items.WATER_BUCKET)
        else -> HTPotionFluidAccess.INSTANCE.fluidContent.bucketHolder.toTemplate(patch = createPotionPatch(contents))!!
    }

    //    FluidStack    //

    @JvmStatic
    fun createFluid(getter: DataComponentGetter, amount: Int = FluidType.BUCKET_VOLUME): FluidStack =
        createFluid(getContents(getter), amount)

    @Suppress("DEPRECATION")
    @JvmStatic
    fun createFluid(potion: Holder<Potion>, amount: Int = FluidType.BUCKET_VOLUME): FluidStack =
        createFluid(PotionContents(potion), amount)

    @JvmStatic
    fun createFluid(contents: PotionContents, amount: Int = FluidType.BUCKET_VOLUME): FluidStack = when {
        contents.`is`(Potions.WATER) -> FluidStack(Fluids.WATER, amount)
        else -> HTPotionFluidAccess.INSTANCE.fluidContent.toStack(amount, createPotionPatch(contents))
    }
}
