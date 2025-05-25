package hiiragi283.ragium.setup

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.util.RagiumConstantValues
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
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent
import net.neoforged.neoforge.registries.DeferredBlock

object RagiumCauldronInteractions {
    @JvmField
    val HONEY: CauldronInteraction.InteractionMap =
        CauldronInteraction.newInteractionMap(RagiumConstantValues.HONEY)

    @JvmField
    val MUSHROOM_STEW: CauldronInteraction.InteractionMap =
        CauldronInteraction.newInteractionMap(RagiumConstantValues.MUSHROOM_STEW)

    @JvmField
    val CRIMSON_SAP: CauldronInteraction.InteractionMap =
        CauldronInteraction.newInteractionMap(RagiumConstantValues.CRIMSON_SAP)

    @JvmField
    val WARPED_SAP: CauldronInteraction.InteractionMap =
        CauldronInteraction.newInteractionMap(RagiumConstantValues.WARPED_SAP)

    @JvmStatic
    fun registerCauldronContents(event: RegisterCauldronFluidContentEvent) {
        fun register(cauldron: DeferredBlock<*>, content: HTFluidContent<*, *, *>) {
            event.register(
                cauldron.get(),
                content.get(),
                FluidType.BUCKET_VOLUME,
                LayeredCauldronBlock.LEVEL,
            )
        }

        register(RagiumBlocks.HONEY_CAULDRON, RagiumFluidContents.HONEY)
        register(RagiumBlocks.MUSHROOM_STEW_CAULDRON, RagiumFluidContents.MUSHROOM_STEW)
        register(RagiumBlocks.CRIMSON_SAP_CAULDRON, RagiumFluidContents.CRIMSON_SAP)
        register(RagiumBlocks.WARPED_SAP_CAULDRON, RagiumFluidContents.WARPED_SAP)
    }

    @JvmStatic
    fun initDefaultInteractions() {
        initDefaultInteraction(HONEY, RagiumFluidContents.HONEY, RagiumBlocks.HONEY_CAULDRON)
        initDefaultInteraction(MUSHROOM_STEW, RagiumFluidContents.MUSHROOM_STEW, RagiumBlocks.MUSHROOM_STEW_CAULDRON)
        initDefaultInteraction(CRIMSON_SAP, RagiumFluidContents.CRIMSON_SAP, RagiumBlocks.CRIMSON_SAP_CAULDRON)
        initDefaultInteraction(WARPED_SAP, RagiumFluidContents.WARPED_SAP, RagiumBlocks.WARPED_SAP_CAULDRON)

        MUSHROOM_STEW.map.put(
            Items.BOWL,
        ) { state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack ->
            if (!level.isClientSide) {
                stack.consume(1, player)
                dropStackAt(player, Items.MUSHROOM_STEW)
                player.awardStat(Stats.USE_CAULDRON)
                player.awardStat(Stats.ITEM_USED.get(stack.item))
                LayeredCauldronBlock.lowerFillLevel(state, level, pos)
                level.playSound(
                    null,
                    pos,
                    SoundEvents.BOTTLE_FILL,
                    SoundSource.BLOCKS,
                )
                level.gameEvent(null, GameEvent.FLUID_PICKUP, pos)
            }
            ItemInteractionResult.sidedSuccess(level.isClientSide)
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
