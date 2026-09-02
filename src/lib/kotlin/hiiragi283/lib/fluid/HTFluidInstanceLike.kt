package hiiragi283.lib.fluid

import net.minecraft.core.component.DataComponentPatch
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

/**
 * [FluidStackTemplate]や[FluidStack]に変換可能なオブジェクトを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTFluidInstanceLike {
    /**
     * 新しい[FluidStackTemplate]のインスタンスを作成します。
     */
    fun toTemplate(
        amount: Int = FluidType.BUCKET_VOLUME,
        patch: DataComponentPatch = DataComponentPatch.EMPTY
    ): FluidStackTemplate?

    /**
     * 新しい[FluidStack]のインスタンスを作成します。
     */
    fun toStack(amount: Int = FluidType.BUCKET_VOLUME, patch: DataComponentPatch = DataComponentPatch.EMPTY): FluidStack
}
