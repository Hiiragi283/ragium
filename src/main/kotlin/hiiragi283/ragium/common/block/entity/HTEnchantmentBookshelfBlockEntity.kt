package hiiragi283.ragium.common.block.entity

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.getStackInActiveHand
import hiiragi283.ragium.api.extension.longRangeCodec
import hiiragi283.ragium.api.extension.mappedCodecOf
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ItemEnchantmentsComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentLevelEntry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.log2
import kotlin.math.pow

class HTEnchantmentBookshelfBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.ENCHANTMENT_BOOKSHELF, pos, state) {
    companion object {
        @JvmField
        val CODEC: Codec<Map<RegistryEntry<Enchantment>, Long>> =
            mappedCodecOf(
                Enchantment.ENTRY_CODEC.fieldOf("enchantment"),
                longRangeCodec(0, Long.MAX_VALUE).fieldOf("value"),
            )
    }

    private val enchantMap: MutableMap<RegistryEntry<Enchantment>, Long> = mutableMapOf()

    private fun levelToValue(level: Int): Long = 2.0.pow(level - 1).toLong()

    private fun valueToLevel(value: Long): Int = log2(value.toDouble()).toInt() + 1

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        CODEC
            .encodeStart(NbtOps.INSTANCE, enchantMap)
            .ifSuccess { nbt.put("Map", it) }
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        CODEC
            .parse(NbtOps.INSTANCE, nbt.get("Map"))
            .ifSuccess(enchantMap::putAll)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val mainStack: ItemStack = player.getStackInActiveHand()
        return if (mainStack.isOf(Items.ENCHANTED_BOOK)) {
            val storedEnchantment: ItemEnchantmentsComponent =
                mainStack.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)
            storedEnchantment.enchantmentEntries.forEach { (entry: RegistryEntry<Enchantment>, level: Int) ->
                enchantMap.compute(entry) { _: RegistryEntry<Enchantment>, value: Long? ->
                    (value ?: 0) + levelToValue(level)
                }
            }
            mainStack.decrement(1)
            ActionResult.success(world.isClient)
        } else {
            super.onUse(state, world, pos, player, hit)
        }
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        enchantMap
            .map { (entry: RegistryEntry<Enchantment>, value: Long) ->
                EnchantedBookItem.forEnchantment(
                    EnchantmentLevelEntry(entry, valueToLevel(value)),
                )
            }.forEach { ItemScatterer.spawn(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), it) }
        super.onStateReplaced(state, world, pos, newState, moved)
    }
}
