package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * [HTProgressData]を使用するレシピ向けの，[HTRecipeBuilder]の拡張クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTProgressRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String) : HTRecipeBuilder<RECIPE>(prefix) {
    var progressData: HTProgressData by HTDelegates.onceInitialize { HTProgressData(20 * 10) }

    var time: Int by object : ReadWriteProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int = progressData.time

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            progressData = HTProgressData(value)
        }
    }

    /**
     * 単一の[HTRecipeResult]を持つレシピ向けの[HTProgressRecipeBuilder]の拡張クラスです。
     * @param RECIPE 生成するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    abstract class SingleResult<out RECIPE : Recipe<*>>(prefix: String) : HTProgressRecipeBuilder<RECIPE>(prefix) {
        abstract val result: HTRecipeResult<*>

        final override fun getRecipeId(): Identifier? = result.getId()
    }
}
