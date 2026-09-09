package hiiragi283.lib.fluid

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

//    FluidStackTemplate    //

/**
 * [FluidStackTemplate]が`null`の場合，[FluidStack.EMPTY]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStackTemplate?.createOrEmpty(): FluidStack = this?.create() ?: FluidStack.EMPTY

/**
 * この[FluidStackTemplate][this]をコピーします。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStackTemplate.transmuteCopy(newFluid: Fluid, newAmount: Int = this.amount()): FluidStackTemplate? = when {
    newFluid == Fluids.EMPTY -> null
    else -> FluidStackTemplate(newFluid, newAmount, this.components())
}

//    FluidStack    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
fun FluidStack(
    fluid: Fluid,
    amount: Int = FluidType.BUCKET_VOLUME,
    patch: DataComponentPatch = DataComponentPatch.EMPTY
): FluidStack {
    val stack = FluidStack(fluid, amount)
    stack.applyComponents(patch)
    return stack
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
fun FluidStack(
    fluid: Holder<Fluid>,
    amount: Int = FluidType.BUCKET_VOLUME,
    patch: DataComponentPatch = DataComponentPatch.EMPTY
): FluidStack {
    val stack = FluidStack(fluid, amount)
    stack.applyComponents(patch)
    return stack
}

/**
 * [FluidStack]を[FluidStackTemplate]に変換します。
 * @return [FluidStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStack.toTemplateOrNull(): FluidStackTemplate? = when {
    this.isEmpty -> null
    else -> FluidStackTemplate.fromNonEmptyStack(this)
}

/**
 * この[FluidStack][this]をコピーします。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStack.transmuteCopy(newFluid: Fluid, newAmount: Int = this.amount()): FluidStack = when {
    newFluid == Fluids.EMPTY -> FluidStack.EMPTY
    else -> FluidStack(newFluid, newAmount, this.componentsPatch)
}
