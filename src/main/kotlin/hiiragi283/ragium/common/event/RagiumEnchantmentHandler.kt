package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.util.HTEnchantmentHelper
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCriteriaTriggers
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumEnchantmentHandler {
    @SubscribeEvent
    fun getEnchantmentLevel(event: GetEnchantmentLevelEvent) {
        event.stack
            .get(RagiumDataComponents.INTRINSIC_ENCHANTMENT)
            ?.useInstance(event.lookup, event.enchantments::set)
    }

    @SubscribeEvent
    fun onEntityDeath(event: LivingDeathEvent) {
        val entity: LivingEntity = event.entity
        val level: Level = entity.level()
        val source: DamageSource = event.source
        // サーバー側のみで実行する
        if (level.isClientSide) return
        generateResonantDebris(entity, level, source)
        lootMobHead(entity, level, source)
    }

    @JvmStatic
    private fun generateResonantDebris(entity: LivingEntity, level: Level, source: DamageSource) {
        // 対象が共振の残骸を生成しない場合はスキップ
        if (!entity.type.`is`(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS)) return
        // 半径4 m以内のブロックに対して変換を試みる
        val entityPos: BlockPos = entity.blockPosition()
        BlockPos.betweenClosed(entityPos.offset(-4, -4, -4), entityPos.offset(4, 4, 4)).forEach { pos: BlockPos ->
            val state: BlockState = level.getBlockState(pos)
            if (state.`is`(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES)) {
                if (entity.random.nextInt(15) == 0) {
                    level.destroyBlock(pos, false)
                    level.setBlockAndUpdate(pos, RagiumBlocks.RESONANT_DEBRIS.get().defaultBlockState())
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    private fun lootMobHead(entity: LivingEntity, level: Level, source: DamageSource) {
        // 武器にStrike効果が付いているか判定
        val weapon: ItemStack = source.weaponItem ?: return
        if (HTEnchantmentHelper.hasStrike(weapon)) {
            // 対象のモブに対応する頭をドロップする
            RagiumDataMaps.INSTANCE
                .getMobHead(level.registryAccess(), entity.type.builtInRegistryHolder())
                .let(entity::spawnAtLocation)

            val attacker: Entity? = source.entity
            if (attacker is ServerPlayer) {
                RagiumCriteriaTriggers.BEHEAD_MOB.trigger(attacker)
            }
        }
    }
}
