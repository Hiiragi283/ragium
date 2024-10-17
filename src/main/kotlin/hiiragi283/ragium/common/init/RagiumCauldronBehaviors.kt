package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.block.BlockState
import net.minecraft.block.LeveledCauldronBlock
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumCauldronBehaviors {
    @JvmStatic
    fun init() {
        register(
            CauldronBehavior.WATER_CAULDRON_BEHAVIOR,
            RagiumContents.Dusts.CRUDE_RAGINITE,
        ) { state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, _: Hand, stack: ItemStack ->
            if (stack.isOf(RagiumContents.Dusts.CRUDE_RAGINITE)) {
                if (!world.isClient) {
                    val count: Int = stack.count
                    stack.count = -1
                    dropStackAt(player, ItemStack(RagiumContents.Dusts.RAGINITE, count))
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos)
                }
                ItemActionResult.success(world.isClient)
            } else {
                ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            }
        }
    }

    @JvmStatic
    private fun register(map: CauldronBehavior.CauldronBehaviorMap, item: ItemConvertible, behavior: CauldronBehavior) {
        map.map[item.asItem()] = behavior
    }
}
