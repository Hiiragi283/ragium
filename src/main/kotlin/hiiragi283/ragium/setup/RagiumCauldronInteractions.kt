package hiiragi283.ragium.setup

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.registry.HTFluidContent
import net.minecraft.core.BlockPos
import net.minecraft.core.cauldron.CauldronInteraction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.registries.DeferredBlock

object RagiumCauldronInteractions {
    @JvmField
    val CRIMSON_SAP: CauldronInteraction.InteractionMap = CauldronInteraction.newInteractionMap("crimson_sap")

    @JvmField
    val WARPED_SAP: CauldronInteraction.InteractionMap = CauldronInteraction.newInteractionMap("warped_sap")

    @JvmStatic
    fun initDefaultInteractions() {
        initDefaultInteraction(CRIMSON_SAP, RagiumFluidContents.CRIMSON_SAP, RagiumBlocks.CRIMSON_SAP_CAULDRON)
        initDefaultInteraction(WARPED_SAP, RagiumFluidContents.WARPED_SAP, RagiumBlocks.WARPED_SAP_CAULDRON)

        CRIMSON_SAP.map.put(
            RagiumItems.RAGI_ALLOY_NUGGET.get(),
        ) { state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack ->
            if (state.getValue(LayeredCauldronBlock.LEVEL) != 3) {
                return@put ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            } else {
                if (!level.isClientSide) {
                    stack.consume(1, player)
                    dropStackAt(player, RagiumItems.CRIMSON_CRYSTAL.toStack())
                    emptyCauldron(RagiumFluidContents.CRIMSON_SAP, level, pos, player, stack)
                }
                ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
        }

        WARPED_SAP.map.put(
            RagiumItems.AZURE_STEEL_NUGGET.get(),
        ) { state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack ->
            if (state.getValue(LayeredCauldronBlock.LEVEL) != 3) {
                return@put ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            } else {
                if (!level.isClientSide) {
                    stack.consume(1, player)
                    dropStackAt(player, RagiumItems.WARPED_CRYSTAL.toStack())
                    emptyCauldron(RagiumFluidContents.WARPED_SAP, level, pos, player, stack)
                }
                ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
        }
    }

    @JvmStatic
    private fun initDefaultInteraction(
        interactions: CauldronInteraction.InteractionMap,
        content: HTFluidContent<*, *, *>,
        cauldron: DeferredBlock<*>,
    ) {
        // バケツへの搬出を登録
        interactions.map.put(
            Items.BUCKET,
        ) { state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack ->
            if (state.getValue(LayeredCauldronBlock.LEVEL) != 3) {
                return@put ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            } else {
                if (!level.isClientSide) {
                    player.setItemInHand(
                        hand,
                        ItemUtils.createFilledResult(stack, player, content.bucketHolder.toStack()),
                    )
                    emptyCauldron(content, level, pos, player, stack)
                }
            }
            ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        // バケツでの搬入を登録
        CauldronInteraction.EMPTY.map.put(
            content.getBucket(),
        ) { state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack ->
            if (!level.isClientSide) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, ItemStack(Items.BUCKET)))
                player.awardStat(Stats.FILL_CAULDRON)
                player.awardStat(Stats.ITEM_USED.get(stack.item))
                level.setBlockAndUpdate(pos, cauldron.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3))
                level.playSound(
                    null,
                    pos,
                    content.getType().getSound(SoundActions.BUCKET_EMPTY) ?: SoundEvents.BUCKET_EMPTY,
                    SoundSource.BLOCKS,
                )
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos)
            }
            ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
    }

    @JvmStatic
    private fun emptyCauldron(
        content: HTFluidContent<*, *, *>,
        level: Level,
        pos: BlockPos,
        player: Player,
        stack: ItemStack,
    ) {
        player.awardStat(Stats.USE_CAULDRON)
        player.awardStat(Stats.ITEM_USED.get(stack.item))
        level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState())
        level.playSound(
            null,
            pos,
            content.getType().getSound(SoundActions.BUCKET_FILL) ?: SoundEvents.BUCKET_FILL,
            SoundSource.BLOCKS,
        )
        level.gameEvent(null, GameEvent.FLUID_PICKUP, pos)
    }
}
