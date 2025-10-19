package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import hiiragi283.ragium.api.util.access.HTAccessConfigSetter
import hiiragi283.ragium.api.util.access.HTAccessConfiguration
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.Tags

/**
 * 搬入出の面を制御可能な[HTBlockEntity]の拡張クラス
 * @see [mekanism.common.tile.prefab.TileEntityConfigurableMachine]
 */
abstract class HTConfigurableBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(
        type,
        pos,
        state,
    ),
    HTAccessConfigGetter,
    HTAccessConfigSetter {
    companion object {
        @JvmStatic
        private val CONFIG_CODEC: BiCodec<FriendlyByteBuf, Map<Direction, HTAccessConfiguration>> =
            BiCodecs.mapOf(VanillaBiCodecs.DIRECTION, HTAccessConfiguration.CODEC)
    }

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        if (!accessConfigCache.isEmpty()) {
            output.store(RagiumConst.ACCESS_CONFIG, CONFIG_CODEC, accessConfigCache)
        }
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        input.read(RagiumConst.ACCESS_CONFIG, CONFIG_CODEC)?.forEach(accessConfigCache::put)
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        if (stack.`is`(Tags.Items.TOOLS_WRENCH)) {
            RagiumMenuTypes.ACCESS_CONFIG.openMenu(player, name, this, ::writeExtraContainerData)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.onRightClickedWithItem(stack, state, level, pos, player, hand, hitResult)
    }

    //    HTAccessConfiguration    //

    private val accessConfigCache: MutableMap<Direction, HTAccessConfiguration> = hashMapOf()

    final override fun getAccessConfig(side: Direction): HTAccessConfiguration =
        accessConfigCache.computeIfAbsent(side) { _: Direction -> HTAccessConfiguration.BOTH }

    final override fun setAccessConfig(side: Direction, value: HTAccessConfiguration) {
        val old: HTAccessConfiguration? = accessConfigCache.put(side, value)
        RagiumAPI.LOGGER.debug("Updated access config: {} -> {}", old, value)
    }
}
