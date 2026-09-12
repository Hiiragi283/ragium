package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
data object HTOreSlurryDataHelper {
    @JvmStatic
    fun getHolder(getter: DataComponentGetter): Holder<HTOreSlurryData>? =
        getter.get(RagiumDataComponents.ORE_SLURRY_DATA)

    @JvmStatic
    fun getData(getter: DataComponentGetter): HTOreSlurryData? = getHolder(getter)?.value()

    @JvmStatic
    fun getTitle(getter: DataComponentGetter): Text? = getData(getter)?.title

    @JvmStatic
    fun createPatch(holder: Holder<HTOreSlurryData>): DataComponentPatch =
        buildDataPatch { set(RagiumDataComponents.ORE_SLURRY_DATA, holder) }

    @JvmStatic
    fun copyToPatch(getter: DataComponentGetter): DataComponentPatch? = getHolder(getter)?.let(::createPatch)

    @JvmStatic
    fun createFluid(holder: Holder<HTOreSlurryData>, amount: Int = FluidType.BUCKET_VOLUME): FluidStack =
        HTOreSlurryFluidAccess.INSTANCE.fluidContent.toStack(amount, createPatch(holder))

    @JvmStatic
    fun createFluid(getter: DataComponentGetter, amount: Int = FluidType.BUCKET_VOLUME): FluidStack? =
        getHolder(getter)?.let { createFluid(it, amount) }

    @JvmStatic
    fun createBucket(holder: Holder<HTOreSlurryData>): ItemStack =
        HTOreSlurryFluidAccess.INSTANCE.fluidContent.bucketHolder.toStack(patch = createPatch(holder))

    @JvmStatic
    fun createBucket(getter: DataComponentGetter): ItemStack? = getHolder(getter)?.let(::createBucket)
}
