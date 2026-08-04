package hiiragi283.lib.serialization

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.getOrElse
import hiiragi283.lib.util.kotlin
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack

//    ValueOutput    //

/**
 * この[ValueOutput][this]に液体を書き込みます。
 * @param stack 保存する液体
 * @param alloyEmpty 空の[FluidStack]を許可するか
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun ValueOutput.putFluid(stack: FluidStack, alloyEmpty: Boolean = true) {
    this.store(HTConstants.FLUID, if (alloyEmpty) FluidStack.OPTIONAL_CODEC else FluidStack.CODEC, stack)
}

/**
 * この[ValueOutput][this]にアイテムを書き込みます。
 * @param stack 保存するアイテム
 * @param alloyEmpty 空の[ItemStack]を許可するか
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun ValueOutput.putItem(stack: ItemStack, alloyEmpty: Boolean = true) {
    this.store(HTConstants.ITEM, if (alloyEmpty) ItemStack.OPTIONAL_CODEC else ItemStack.CODEC, stack)
}

//    ValueInput    //

/**
 * [ValueInput.read]を[Option]に変換して返します。
 * @param T 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any> ValueInput.readOption(name: String, codec: Codec<T>): Option<T> = this.read(name, codec).kotlin

/**
 * この[ValueInput][this]から液体を読み取ります。
 * @param alloyEmpty 空の[FluidStack]を許可するか
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun ValueInput.getFluid(alloyEmpty: Boolean = true): Option<FluidStack> = this.readOption(HTConstants.FLUID, if (alloyEmpty) FluidStack.OPTIONAL_CODEC else FluidStack.CODEC)

/**
 * この[ValueInput][this]からアイテムを読み取ります。
 * @param alloyEmpty 空の[FluidStack]を許可するか
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun ValueInput.getItem(alloyEmpty: Boolean = true): Option<ItemStack> = this.readOption(HTConstants.ITEM, if (alloyEmpty) ItemStack.OPTIONAL_CODEC else ItemStack.CODEC)

/**
 * この[ValueInput][this]から液体を読み取ります。
 * @param alloyEmpty 空の[FluidStack]を許可するか
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun ValueInput.getFluidOrEmpty(alloyEmpty: Boolean = true): FluidStack = this.getFluid(alloyEmpty).getOrElse(FluidStack::EMPTY)

/**
 * この[ValueInput][this]からアイテムを読み取ります。
 * @param alloyEmpty 空の[FluidStack]を許可するか
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun ValueInput.getItemOrEmpty(alloyEmpty: Boolean = true): ItemStack = this.getItem(alloyEmpty).getOrElse(ItemStack::EMPTY)
