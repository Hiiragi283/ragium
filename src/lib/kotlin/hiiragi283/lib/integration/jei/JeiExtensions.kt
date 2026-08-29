package hiiragi283.lib.integration.jei

import hiiragi283.lib.recipe.ingredient.HTStackPreview
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

//    IIngredientAcceptor    //

// Fluid
/**
 * 液体を登録します。
 * @param T 液体の登録先
 * @param stack 登録する液体
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : IIngredientAcceptor<T>> T.add(stack: FluidStack): T = this.add(stack.fluid, stack.amount.toLong(), stack.componentsPatch)

/**
 * 液体を登録します。
 * @param T 液体の登録先
 * @param stacks 登録する液体の一覧
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : IIngredientAcceptor<T>> T.addFluidStacks(stacks: Iterable<FluidStack>): T = this.addIngredients(NeoForgeTypes.FLUID_STACK, stacks.toList())

/**
 * 液体を登録します。
 * @param T 液体の登録先
 * @param ingredient 登録する液体の一覧
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmName("addFluids")
fun <T : IIngredientAcceptor<T>> T.add(ingredient: HTStackPreview<FluidStack>): T = this.addFluidStacks(ingredient.getPreviewStacks(this.contextMap))

/**
 * 液体を登録します。
 * @param T 液体の登録先
 * @param result 登録する液体
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : IIngredientAcceptor<T>> T.add(result: HTFluidResult): T = this.add(result.create())

// Item
/**
 * アイテムを登録します。
 * @param T アイテムの登録先
 * @param ingredient 登録するアイテムの一覧
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmName("addItems")
fun <T : IIngredientAcceptor<T>> T.add(ingredient: HTStackPreview<ItemStack>): T = this.addItemStacks(ingredient.getPreviewStacks(this.contextMap))

/**
 * アイテムを登録します。
 * @param T アイテムの登録先
 * @param result 登録するアイテム
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : IIngredientAcceptor<T>> T.add(result: HTItemResult): T = this.add(result.create())
