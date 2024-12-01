package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.replaceBlockState
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World

object HTBrushingDropRegistry {
    private val dropRegistry: MutableMap<Block, Pair<BlockState, ItemStack>> = mutableMapOf()

    @JvmStatic
    fun addDrop(before: Block, drop: ItemConvertible, count: Int = 1) {
        addDrop(before, ItemStack(drop, count))
    }

    @JvmStatic
    fun addDrop(before: Block, drop: ItemStack) {
        addDrop(before, Blocks.AIR.defaultState, drop)
    }

    @JvmStatic
    fun addDrop(
        before: Block,
        after: BlockState,
        drop: ItemConvertible,
        count: Int = 1,
    ) {
        addDrop(before, after, ItemStack(drop, count))
    }

    @JvmStatic
    fun addDrop(before: Block, after: BlockState, drop: ItemStack) {
        check(dropRegistry.put(before, after to drop) == null) { "Duplicated brushing block: $before" }
    }

    @JvmStatic
    fun init() {
        addDrop(Blocks.CRYING_OBSIDIAN, Blocks.OBSIDIAN.defaultState, RagiumItems.OBSIDIAN_TEAR)
        addDrop(Blocks.GILDED_BLACKSTONE, Blocks.BLACKSTONE.defaultState, Items.GOLD_NUGGET, 3)

        HTBrushingCallback.EVENT.register { world: World, player: PlayerEntity, _: ItemStack, hitResult: BlockHitResult ->
            world.replaceBlockState(hitResult.blockPos, true) { state: BlockState ->
                dropRegistry[state.block]?.let { (after: BlockState, drop: ItemStack) ->
                    dropStackAt(player, drop)
                    after
                }
            }
        }
    }
}
