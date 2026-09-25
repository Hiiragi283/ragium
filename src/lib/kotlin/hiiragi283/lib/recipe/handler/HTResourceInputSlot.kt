package hiiragi283.lib.recipe.handler

import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.HTTransferAccess
import net.minecraft.core.TypedInstance
import net.neoforged.neoforge.transfer.resource.RegisteredResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [HTResourceSlot]に基づいた[HTInputSlot]の抽象クラスです。
 *
 * 参考 : [Mekanism - InputResourceHandler](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/recipes/inputs/InputResourceHandler.java)
 * @param TYPE 材料の種類のクラス
 * @param RESOURCE [slot]が保持するリソースのクラス
 * @param INSTANCE 材料の種類を保持するインスタンスのクラス
 * @param slot 入力を保持するスロット
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
abstract class HTResourceInputSlot<
    TYPE : Any,
    RESOURCE : RegisteredResource<TYPE>,
    INSTANCE : TypedInstance<TYPE>
    >(
    protected val slot: HTResourceSlot<RESOURCE>
) : HTInputSlot<TYPE, INSTANCE> {
    /**
     * 空の入力を取得します。
     */
    protected abstract fun getEmptyStack(): INSTANCE

    /**
     * 入力から数量を取得します。
     */
    protected abstract fun getAmount(instance: INSTANCE): Int

    /**
     * 入力をリソースに変換します。
     */
    protected abstract fun asResource(instance: INSTANCE): RESOURCE

    final override fun getRequiredInput(ingredient: HTIngredient<TYPE, INSTANCE>): INSTANCE {
        if (slot.isEmpty) {
            return getEmptyStack()
        }
        return ingredient.getMatchingStack(getStoredInput())
    }

    final override fun use(input: INSTANCE?, transaction: TransactionContext): HTInputSlot.UseResult {
        if (input == null || isEmpty(input)) {
            return HTInputSlot.UseResult.EMPTY
        }
        val amount: Int = getAmount(input)
        val extracted: Int = slot.extract(asResource(input), amount, transaction, HTTransferAccess.INTERNAL)
        return when {
            extracted == amount -> HTInputSlot.UseResult.CONSUMED
            else -> HTInputSlot.UseResult.NOT_CONSUMED
        }
    }
}
