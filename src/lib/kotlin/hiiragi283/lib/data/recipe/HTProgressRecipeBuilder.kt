package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import net.minecraft.world.item.crafting.Recipe

/**
 * [HTProgressData]を使用するレシピ向けの，[HTRecipeBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTProgressRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String) : HTRecipeBuilder<RECIPE>(prefix) {
    protected var progressData: HTProgressData = HTProgressData.time(20 * 10)
        private set

    var energy: Int by object : ReadWriteProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int = when (progressData) {
            is HTProgressData.Energy -> (progressData as HTProgressData.Energy).value
            is HTProgressData.Time -> error("Cannot get energy amount")
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            progressData = HTProgressData.energy(value)
        }
    }
    var time: Int by object : ReadWriteProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int = when (progressData) {
            is HTProgressData.Energy -> 20 * 10
            is HTProgressData.Time -> (progressData as HTProgressData.Time).value
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            progressData = HTProgressData.time(value)
        }
    }
}
