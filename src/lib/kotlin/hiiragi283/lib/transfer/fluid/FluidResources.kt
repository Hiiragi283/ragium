package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.HTResourceView
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource

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
 * [FluidResource]向けの[HTResourceView]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTFluidView = HTResourceView<FluidResource>

/**
 * [FluidResource]向けの[HTResourceSlot]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTFluidTank = HTResourceSlot<FluidResource>

/**
 * この[HTFluidView][this]から[FluidStack]を取得します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun HTFluidView.getFluidStack(): FluidStack = this.resource.toStack(this.amount)

/**
 * [FluidResource]向けの[ResourceHandler]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias FluidResourceHandler = ResourceHandler<FluidResource>
