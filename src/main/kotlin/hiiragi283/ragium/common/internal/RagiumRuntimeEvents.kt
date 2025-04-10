package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilder
import hiiragi283.ragium.api.event.HTRecipesUpdatedEvent
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.property.HTPropertyMap
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Bee
import net.minecraft.world.entity.npc.WanderingTrader
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.LootTableLoadEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Item Interaction    //

    @SubscribeEvent
    fun onClickedEntity(event: PlayerInteractEvent.EntityInteract) {
        // イベントがキャンセルされている場合はパス
        if (event.isCanceled) return
        // アイテムがガラス瓶でにあ場合はパス
        val stack: ItemStack = event.itemStack
        if (!stack.`is`(Items.GLASS_BOTTLE)) return
        // 対象がハチでない場合はパス
        val target: Bee = event.target as? Bee ?: return
        if (target.isAlive) {
            val player: Player = event.entity
            target.level().playSound(player, target, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1f, 1f)
            // ハチを瓶に詰める
            if (!player.level().isClientSide) {
                target.discard()
                stack.shrink(1)
                dropStackAt(player, RagiumItems.BOTTLED_BEE)
            }
            event.cancellationResult = InteractionResult.sidedSuccess(player.level().isClientSide)
        }
    }

    @SubscribeEvent
    fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
        if (event.isCanceled) return
        val stack: ItemStack = event.itemStack
        if (stack.isEmpty) return
        val player: Player = event.entity
        val level: Level = player.level()
        // エンダーバンドルの場合はGUIを開く
        if (stack.`is`(RagiumItems.ENDER_BUNDLE)) {
            // SEを再生する
            level.playSound(null, player.blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS)
            // GUiを開く
            player.openMenu(
                SimpleMenuProvider(
                    { containerId: Int, inventory: Inventory, playerIn: Player ->
                        ChestMenu.threeRows(containerId, inventory, playerIn.enderChestInventory)
                    },
                    Component.translatable("container.enderchest"),
                ),
            )
            player.awardStat(Stats.OPEN_ENDERCHEST)
            event.cancellationResult = InteractionResult.sidedSuccess(level.isClientSide)
            return
        }
        // 行商人のカタログの場合もGUIを開く
        if (stack.`is`(RagiumItems.TRADER_CATALOG)) {
            event.cancellationResult =
                WanderingTrader(EntityType.WANDERING_TRADER, level).interact(player, InteractionHand.MAIN_HAND)
            return
        }
        // 経験値ベリーの場合は経験値を与える
        if (stack.`is`(RagiumItems.EXP_BERRIES)) {
            player.giveExperiencePoints(8)
            stack.consume(1, player)
            level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS)
            event.cancellationResult = InteractionResult.sidedSuccess(level.isClientSide)
            return
        }
    }

    @SubscribeEvent
    fun onFinishUsingItem(event: LivingEntityUseItemEvent.Finish) {
        val stack: ItemStack = event.item
        if (stack.isEmpty) return
        val result: ItemStack = event.resultStack
        val user: LivingEntity = event.entity
        val player: Player? = user as? Player
        val level: Level = user.level()
        // アンブロシアの場合は個数を減らさない
        if (stack.`is`(RagiumItems.AMBROSIA)) {
            if (result.isEmpty) {
                event.resultStack = stack.copy()
            } else {
                result.grow(1)
            }
            return
        }
        // アイスクリームの場合は火を消す
        if (stack.`is`(RagiumItems.ICE_CREAM)) {
            if (!level.isClientSide) {
                user.extinguishFire()
            }
        }
        // ゆがんだウォートの場合はデバフをランダムに一つだけ消す
        if (stack.`is`(RagiumItems.WARPED_WART)) {
            val badEffect: Holder<MobEffect> = user.activeEffects
                .map(MobEffectInstance::getEffect)
                .filter { it.value().category == MobEffectCategory.HARMFUL }
                .randomOrNull()
                ?: return
            user.removeEffect(badEffect)
        }
    }

    //    Loot Table Load    //

    @SubscribeEvent
    fun onLoadLootTable(event: LootTableLoadEvent) {
        val loot: LootTable = event.table

        fun modify(entityType: EntityType<*>, function: LootTable.() -> Unit) {
            if (loot.lootTableId == entityType.defaultLootTable.location()) {
                function(loot)
            }
        }

        // 行商人がカタログを落とすように
        modify(EntityType.WANDERING_TRADER) {
            addPool(
                LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1f))
                    .add(LootItem.lootTableItem(RagiumItems.TRADER_CATALOG))
                    .build(),
            )
            LOGGER.info("Modified loot table for Wandering Trader!")
        }
    }

    //    Machine Recipe    //

    @SubscribeEvent
    fun onMachineRecipesUpdated(event: HTRecipesUpdatedEvent) {
        crushing(event)
    }

    @JvmStatic
    private fun crushing(event: HTRecipesUpdatedEvent) {
        val registry: HTMaterialRegistry = RagiumAPI.getInstance().getMaterialRegistry()
        for ((key: HTMaterialKey, properties: HTPropertyMap) in registry) {
            val name: String = key.name
            val type: HTMaterialType = properties.getOrDefault(HTMaterialPropertyKeys.MATERIAL_TYPE)
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            val resultPrefix: HTTagPrefix? = type.getOreResultPrefix()
            // Ore
            if (resultPrefix != null) {
                event.register(
                    RagiumRecipes.CRUSHING,
                    RagiumAPI.id("${name}_dust_from_ore"),
                ) { lookup: HolderGetter<Item> ->
                    val result: Item = event.getFirstItem(resultPrefix, key) ?: return@register null
                    HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                        .itemInput(HTTagPrefixes.ORE, key)
                        .itemOutput(result, properties.getOrDefault(HTMaterialPropertyKeys.ORE_CRUSHED_COUNT))
                        .createRecipe()
                }
            }
            val dust: Item = event.getFirstItem(HTTagPrefixes.DUST, key) ?: continue
            // Gem/Ingot
            if (mainPrefix != null) {
                event.register(
                    RagiumRecipes.CRUSHING,
                    RagiumAPI.id("${name}_dust_from_main"),
                ) { lookup: HolderGetter<Item> ->
                    HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                        .itemInput(mainPrefix, key)
                        .itemOutput(dust)
                        .createRecipe()
                }
            }
            // Gear
            event.register(
                RagiumRecipes.CRUSHING,
                RagiumAPI.id("${name}_dust_from_gear"),
            ) { lookup: HolderGetter<Item> ->
                HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                    .itemInput(HTTagPrefixes.GEAR, key)
                    .itemOutput(dust, 4)
                    .createRecipe()
            }
            // Plate
            event.register(
                RagiumRecipes.CRUSHING,
                RagiumAPI.id("${name}_dust_from_plate"),
            ) { lookup: HolderGetter<Item> ->
                HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                    .itemInput(HTTagPrefixes.PLATE, key)
                    .itemOutput(dust)
                    .createRecipe()
            }
            // Raw
            event.register(
                RagiumRecipes.CRUSHING,
                RagiumAPI.id("${name}_dust_from_raw"),
            ) { lookup: HolderGetter<Item> ->
                HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
                    .itemInput(HTTagPrefixes.RAW_MATERIAL, key)
                    .itemOutput(dust, 2)
                    .createRecipe()
            }
        }
    }
}
