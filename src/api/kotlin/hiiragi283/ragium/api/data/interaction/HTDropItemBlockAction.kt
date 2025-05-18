package hiiragi283.ragium.api.data.interaction

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.extension.dropStackAt
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ItemLike

class HTDropItemBlockAction(private val stack: ItemStack) : HTBlockAction.ItemPreview {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTDropItemBlockAction> =
            ItemStack.STRICT_CODEC.fieldOf("stack").xmap(::HTDropItemBlockAction, HTDropItemBlockAction::stack)
    }

    constructor(item: ItemLike, count: Int = 1) : this(ItemStack(item, count))

    override val codec: MapCodec<out HTBlockAction> = CODEC

    override fun applyAction(context: UseOnContext) {
        dropStackAt(context.level, context.clickedPos, stack)
    }

    override fun getPreviewStack(): ItemStack = stack.copy()
}
