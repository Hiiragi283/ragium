package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTAdvancementRewardCallback
import hiiragi283.ragium.api.event.HTModifyBlockDropsCallback
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.util.*
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.inventory.HTBackpackInventory
import hiiragi283.ragium.common.util.*
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumEventHandlers {
    @JvmStatic
    fun init() {
        // send title and floating item packet when unlock advancement
        HTAdvancementRewardCallback.EVENT.register { player: ServerPlayerEntity, entry: AdvancementEntry ->
            if (entry.id == RagiumAPI.id("tier1/root")) {
                player.sendTitle(Text.literal("Welcome to Heat Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.BRICKS)
            }
            if (entry.id == RagiumAPI.id("tier2/root")) {
                player.sendTitle(Text.literal("Welcome to Kinetic Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.POLISHED_BLACKSTONE_BRICKS)
            }
            if (entry.id == RagiumAPI.id("tier3/root")) {
                player.sendTitle(Text.literal("Welcome to Electric Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.END_STONE_BRICKS)
            }
            if (entry.id == RagiumAPI.id("tier4/root")) {
                player.sendTitle(Text.literal("Welcome to Alchemical Age!"))
                RagiumNetworks.sendFloatingItem(player, RagiumContents.Element.RAGIUM.dustItem)
            }
        }

        // modify drops
        HTModifyBlockDropsCallback.EVENT.register {
                _: BlockState,
                world: ServerWorld,
                _: BlockPos,
                _: BlockEntity?,
                breaker: Entity?,
                tool: ItemStack,
                drops: List<ItemStack>,
            ->
            when {
                hasEnchantment(RagiumEnchantments.SMELTING, world, tool) -> drops.map { drop: ItemStack ->
                    applyRecipe(drop, world, breaker, tool, RecipeType.SMELTING, ::SingleStackRecipeInput)
                }

                hasEnchantment(RagiumEnchantments.SLEDGE_HAMMER, world, tool) -> drops.map { drop: ItemStack ->
                    applyMachineRecipe(drop, world, breaker, tool, RagiumMachineTypes.Processor.GRINDER)
                }

                hasEnchantment(RagiumEnchantments.BUZZ_SAW, world, tool) -> drops.map { drop: ItemStack ->
                    applyMachineRecipe(drop, world, breaker, tool, RagiumMachineTypes.SAW_MILL)
                }

                else -> drops
            }
        }

        /*HTAllowSpawnCallback.EVENT.register { entityType: EntityType<*>, _: ServerWorldAccess, _: BlockPos, reason: SpawnReason ->
            if (entityType.spawnGroup == SpawnGroup.MONSTER && reason == SpawnReason.NATURAL) TriState.FALSE else TriState.DEFAULT
        }

        ServerLivingEntityEvents.AFTER_DEATH.register { entity: LivingEntity, damage: DamageSource ->
            if (entity.type.isIn(EntityTypeTags.UNDEAD) && damage.isIn(DamageTypeTags.IS_PLAYER_ATTACK)) {
                dropStackAt(entity, Items.NETHER_STAR.defaultStack)
            }
        }*/

        // range mining
        /*PlayerBlockBreakEvents.AFTER.register { world: World, player: PlayerEntity, pos: BlockPos, _: BlockState, _: BlockEntity? ->
            val enchant: RegistryEntry<Enchantment> =
                world.getEntry(RegistryKeys.ENCHANTMENT, Enchantments.UNBREAKING) ?: return@register
            val stack: ItemStack = player.getStackInHand(Hand.MAIN_HAND)
            val enchantLevel: Int = EnchantmentHelper.getLevel(enchant, stack)
            if (enchantLevel > 0) {
                breakRangedBlock(
                    world,
                    pos,
                    enchantLevel,
                    player,
                    stack
                )
            }
        }*/

        // spawn oblivion cube when oblivion cluster broken
        PlayerBlockBreakEvents.AFTER.register { world: World, player: PlayerEntity, pos: BlockPos, state: BlockState, _: BlockEntity? ->
            if (!player.isCreative) {
                if (state.isOf(RagiumContents.OBLIVION_CLUSTER)) {
                    RagiumEntityTypes.OBLIVION_CUBE.create(world)?.let {
                        it.refreshPositionAndAngles(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 0.0F, 0.0F)
                        world.spawnEntity(it)
                        it.playSpawnEffects()
                    }
                }
            }
        }

        // open backpack
        UseItemCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand ->
            val stack: ItemStack = player.getStackInHand(hand)
            (stack.get(RagiumComponentTypes.INVENTORY) as? HTBackpackInventory)
                ?.openInventory(world, player, stack)
                ?: TypedActionResult.pass(stack)
        }

        // open ender chest
        UseItemCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand ->
            val stack: ItemStack = player.getStackInHand(hand)
            if (stack.isOf(RagiumContents.ENDER_BACKPACK)) {
                openEnderChest(world, player)
                TypedActionResult.success(stack, world.isClient)
            } else {
                TypedActionResult.pass(stack)
            }
        }

        UseItemCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand ->
            val stack: ItemStack = player.getStackInHand(hand)
            if (stack.hasEnchantments()) {
                stack.enchantments
                    .toLevelMap()
                    .map(EnchantedBookItem::forEnchantment)
                    .onEach { dropStackAt(player, it) }
                stack.remove(DataComponentTypes.ENCHANTMENTS)
                TypedActionResult.success(stack, world.isClient)
            }
            TypedActionResult.pass(stack)
        }
    }

    @JvmStatic
    private fun <T : RecipeInput, U : Recipe<T>> applyRecipe(
        drop: ItemStack,
        world: World,
        breaker: Entity?,
        tool: ItemStack,
        recipeType: RecipeType<U>,
        factory: (ItemStack) -> T,
    ): ItemStack {
        val input: T = factory(drop)
        return world.recipeManager
            .getFirstMatch(recipeType, input, world)
            .map(RecipeEntry<U>::value)
            .map { it.craft(input, world.registryManager) }
            .map { drop1: ItemStack ->
                breaker
                    ?.let { it as? LivingEntity }
                    ?.let { tool.damage(1, it, EquipmentSlot.MAINHAND) }
                drop1
            }.orElse(drop)
    }

    @JvmStatic
    private fun applyMachineRecipe(
        drop: ItemStack,
        world: World,
        breaker: Entity?,
        tool: ItemStack,
        machineType: HTMachineConvertible,
    ): ItemStack = applyRecipe(drop, world, breaker, tool, RagiumRecipeTypes.MACHINE) {
        HTMachineRecipe.Input.create(
            machineType,
            HTMachineTier.PRIMITIVE,
            it,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ComponentMap.EMPTY,
        )
    }
}
