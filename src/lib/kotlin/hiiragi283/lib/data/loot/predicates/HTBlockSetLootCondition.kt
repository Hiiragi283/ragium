package hiiragi283.lib.data.loot.predicates

import com.mojang.serialization.MapCodec
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition

/**
 * [Block]の[HolderSet]に基づいて判定する[LootItemCondition]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTBlockSetLootCondition(val blocks: HolderSet<Block>) : LootItemCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBlockSetLootCondition> = HTCodecs.holderSet(Registries.BLOCK)
            .fieldOf("blocks")
            .xmap(::HTBlockSetLootCondition, HTBlockSetLootCondition::blocks)
    }

    override fun codec(): MapCodec<out LootItemCondition> = CODEC

    override fun test(context: LootContext): Boolean =
        context.getOptionalParameter(LootContextParams.BLOCK_STATE)?.`is`(blocks) ?: false
}
