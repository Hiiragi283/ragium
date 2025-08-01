package hiiragi283.ragium.api.data.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

object HTResultHelper {
    //    Item    //

    fun item(item: ItemLike, count: Int = 1): HTItemResult = item(ItemStack(item, count))

    fun item(stack: ItemStack): HTItemResult = item(stack.itemHolder.idOrThrow, stack.count, stack.componentsPatch)

    fun item(id: ResourceLocation, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult =
        HTItemResult(Either.left(id), count, component)

    fun item(tagKey: TagKey<Item>, count: Int = 1): HTItemResult = HTItemResult(Either.right(tagKey), count, DataComponentPatch.EMPTY)
}
