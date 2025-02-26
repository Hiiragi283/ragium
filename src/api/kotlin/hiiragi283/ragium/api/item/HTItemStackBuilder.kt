package hiiragi283.ragium.api.item

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.MutableDataComponentHolder

/**
 * コンポーネント付きの[ItemStack]のインスタンスを作るビルダー
 */
class HTItemStackBuilder : MutableDataComponentHolder {
    private val stack: ItemStack

    constructor(item: ItemLike, count: Int = 1) {
        stack = ItemStack(item, count)
    }

    constructor(item: Holder<Item>, count: Int = 1) {
        stack = ItemStack(item, count)
    }

    fun build(): ItemStack = stack

    fun <T : Any> put(componentType: DataComponentType<in T>, value: T?): HTItemStackBuilder = apply {
        set(componentType, value)
    }

    //    MutableDataComponentHolder    //

    override fun <T : Any> set(componentType: DataComponentType<in T>, value: T?): T? = stack.set(componentType, value)

    override fun <T : Any> remove(componentType: DataComponentType<out T>): T? = stack.remove(componentType)

    override fun applyComponents(patch: DataComponentPatch) {
        stack.applyComponents(patch)
    }

    override fun applyComponents(components: DataComponentMap) {
        stack.applyComponents(components)
    }

    override fun getComponents(): DataComponentMap = stack.components
}
