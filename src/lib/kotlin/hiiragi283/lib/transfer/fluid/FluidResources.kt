package hiiragi283.lib.transfer.fluid

import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler

//    FluidResource    //

/**
 * この[FluidStack][this]を[FluidResource]と個数に分解します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStack.toResourcePair(): Pair<FluidResource, Int> = FluidResource.of(this) to this.amount

/**
 * この[FluidStackTemplate][this]を[FluidResource]と個数に分解します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStackTemplate.toResourcePair(): Pair<FluidResource, Int> = FluidResource.of(this) to this.amount

//    ResourceHandler    //

/**
 * [FluidResource]向けの[ResourceHandler]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias FluidResourceHandler = ResourceHandler<FluidResource>

/**
 * [FluidStack]のコピーを取得します。
 * @param index [FluidStack]を取得するスロットのインデックス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidResourceHandler.getFluidStack(index: Int): FluidStack = this.getResource(index).toStack(this.getAmountAsInt(index))

/**
 * この[FluidStacksResourceHandler][this]の中身を直接置き換えます。
 * @param index 置き換えるスロットのインデックス
 * @param stack 置き換え後の液体
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun FluidStacksResourceHandler.set(index: Int, stack: FluidStack) {
    val (resource: FluidResource, amount: Int) = stack.toResourcePair()
    this.set(index, resource, amount)
}
