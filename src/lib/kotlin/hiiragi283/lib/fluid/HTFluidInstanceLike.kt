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
    fun asTemplate(
        amount: Int = FluidType.BUCKET_VOLUME,
        patch: DataComponentPatch = DataComponentPatch.EMPTY
    ): Result<FluidStackTemplate>

    /**
     * 新しい[FluidStack]のインスタンスを作成します。
     */
    fun asStack(
        amount: Int = FluidType.BUCKET_VOLUME,
        patch: DataComponentPatch = DataComponentPatch.EMPTY
    ): Result<FluidStack> = asTemplate(amount, patch).map(FluidStackTemplate::create)

    /**
     * 新しい[FluidStack]のインスタンスを作成します。
     * @return [FluidStack]のインスタンスを作成できなかった場合は[FluidStack.EMPTY]
     */
    fun asStackOrEmpty(
        amount: Int = FluidType.BUCKET_VOLUME,
        patch: DataComponentPatch = DataComponentPatch.EMPTY
    ): FluidStack = asStack(amount, patch).getOrElse { FluidStack.EMPTY }
}
