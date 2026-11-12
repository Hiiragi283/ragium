package hiiragi283.lib.fluid

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

//    FluidStackTemplate    //

/**
 * [FluidStackTemplate]が`null`の場合，[FluidStack.EMPTY]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStackTemplate?.createOrEmpty(): FluidStack = this?.create() ?: FluidStack.EMPTY

//    FluidStack    //

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
 * この[FluidStack][this]から液体入りバケツの[ItemStack]を作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
fun FluidStack.createBucket(): ItemStack = this.fluidType.getBucket(this)
