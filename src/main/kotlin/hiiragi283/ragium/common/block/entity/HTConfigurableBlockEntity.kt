package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.block.entity.HTBlockInteractContext
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import hiiragi283.ragium.api.util.access.HTAccessConfigSetter
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags

/**
 * 搬入出の面を制御可能な[HTBlockEntity]の拡張クラス
 * @see mekanism.common.tile.prefab.TileEntityConfigurableMachine
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
        private val CONFIG_CODEC: BiCodec<FriendlyByteBuf, Map<Direction, HTAccessConfig>> =
            BiCodecs.mapOf(VanillaBiCodecs.DIRECTION, HTAccessConfig.CODEC)
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

    override fun onRightClickedWithItem(context: HTBlockInteractContext, stack: ItemStack, hand: InteractionHand): ItemInteractionResult {
        if (stack.`is`(Tags.Items.TOOLS_WRENCH)) {
            RagiumMenuTypes.ACCESS_CONFIG.openMenu(context.player, name, this, ::writeExtraContainerData)
            return ItemInteractionResult.sidedSuccess(context.level.isClientSide)
        }
        return super.onRightClickedWithItem(context, stack, hand)
    }

    //    HTAccessConfiguration    //

    private val accessConfigCache: MutableMap<Direction, HTAccessConfig> = hashMapOf()

    final override fun getAccessConfig(side: Direction): HTAccessConfig =
        accessConfigCache.computeIfAbsent(side) { _: Direction -> HTAccessConfig.BOTH }

    final override fun setAccessConfig(side: Direction, value: HTAccessConfig) {
        val old: HTAccessConfig? = accessConfigCache.put(side, value)
        RagiumAPI.LOGGER.debug("Updated access config: {} -> {}", old, value)
    }
}
