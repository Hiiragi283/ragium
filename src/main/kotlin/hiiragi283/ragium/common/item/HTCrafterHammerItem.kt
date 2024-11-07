package hiiragi283.ragium.common.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.extension.packetCodecOf
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumToolMaterials
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.*
import net.minecraft.item.tooltip.TooltipAppender
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.screen.slot.Slot
import net.minecraft.sound.SoundEvents
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.Consumer

object HTCrafterHammerItem :
    MiningToolItem(RagiumToolMaterials.STEEL, BlockTags.AIR, itemSettings().rarity(Rarity.RARE)) {
    private fun getComponent(stack: ItemStack): Component = stack.getOrDefault(Component.COMPONENT_TYPE, Component.DEFAULT)

    private fun setComponent(stack: ItemStack, component: Component? = null) {
        stack.set(Component.COMPONENT_TYPE, component)
    }

    private fun canUse(stack: ItemStack): Boolean = stack.damage < stack.maxDamage - 1

    // insert modules
    override fun onClicked(
        stack: ItemStack,
        otherStack: ItemStack,
        slot: Slot,
        clickType: ClickType,
        player: PlayerEntity,
        cursorStackReference: StackReference,
    ): Boolean {
        if (clickType == ClickType.LEFT && slot.canTakePartial(player)) {
            val currentComponent: Component = getComponent(stack)
            if (!currentComponent.isDefault) {
                return false
            } else {
                val foundModule: Behavior = Behavior.entries
                    .firstOrNull { it.asItem() == otherStack.item }
                    ?: return false
                setComponent(stack, currentComponent.copy(behavior = foundModule))
                otherStack.decrement(1)
                player.playSound(SoundEvents.BLOCK_SMITHING_TABLE_USE)
                return true
            }
        } else {
            return false
        }
    }

    // drop modules
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack: ItemStack = user.getStackInHand(hand)
        val component: Component = getComponent(stack)
        return if (!component.isDefault) {
            if (!world.isClient) {
                dropStackAt(user, component.behavior.asItem().defaultStack)
            }
            setComponent(stack, null)
            TypedActionResult.success(stack, world.isClient)
        } else {
            super.use(world, user, hand)
        }
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult = context.stack
        .let(::getComponent)
        .useOnBlock(context)

    override fun canRepair(stack: ItemStack, ingredient: ItemStack): Boolean = false

    override fun isCorrectForDrops(stack: ItemStack, state: BlockState): Boolean = getComponent(stack).isSuitableFor(state)

    override fun getMiningSpeed(stack: ItemStack, state: BlockState): Float = when {
        canUse(stack) -> super.getMiningSpeed(stack, state)
        else -> 1.0f
    }

    override fun canMine(
        state: BlockState,
        world: World,
        pos: BlockPos,
        miner: PlayerEntity,
    ): Boolean = canUse(miner.getStackInHand(Hand.MAIN_HAND))

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        getComponent(stack).appendTooltip(context, tooltip::add, type)
    }

    //    SimpleEnergyItem    //

    // override fun getEnergyCapacity(stack: ItemStack): Long = getComponent(stack).tier.energyCapacity

    // override fun getEnergyMaxInput(stack: ItemStack): Long = getEnergyCapacity(stack)

    // override fun getEnergyMaxOutput(stack: ItemStack): Long = 0

    //    Component    //

    data class Component(val tier: HTMachineTier, val behavior: Behavior) : TooltipAppender {
        companion object {
            @JvmField
            val DEFAULT = Component(
                HTMachineTier.PRIMITIVE,
                Behavior.DEFAULT,
            )

            @JvmField
            val CODEC: Codec<Component> =
                RecordCodecBuilder.create { instance ->
                    instance
                        .group(
                            HTMachineTier.CODEC
                                .optionalFieldOf("tier", HTMachineTier.PRIMITIVE)
                                .forGetter(Component::tier),
                            Behavior.CODEC
                                .fieldOf("behavior")
                                .forGetter(
                                    Component::behavior,
                                ),
                        ).apply(instance, ::Component)
                }

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Component> =
                PacketCodec.tuple(
                    HTMachineTier.PACKET_CODEC,
                    Component::tier,
                    Behavior.PACKET_CODEC,
                    Component::behavior,
                    ::Component,
                )

            @JvmField
            val COMPONENT_TYPE: ComponentType<Component> =
                ComponentType
                    .builder<Component>()
                    .codec(CODEC)
                    .packetCodec(PACKET_CODEC)
                    .build()
        }

        val isDefault: Boolean
            get() = behavior == Behavior.DEFAULT

        fun isSuitableFor(state: BlockState): Boolean = state.isIn(behavior.mineableTag)

        fun useOnBlock(context: ItemUsageContext): ActionResult = when {
            canUse(context.stack) -> behavior.useOnBlock(context)
            else -> ActionResult.PASS
        }

        //    TooltipAppender    //

        override fun appendTooltip(context: TooltipContext, tooltip: Consumer<Text>, type: TooltipType) {
            tooltip.accept(tier.tierText)
            tooltip.accept(behavior.tooltipText)
        }
    }

    //    Behavior    //

    enum class Behavior(val mineableTag: TagKey<Block>) :
        HTContent<Item>,
        StringIdentifiable {
        DEFAULT(BlockTags.AIR) {
            override fun useOnBlock(context: ItemUsageContext): ActionResult = ActionResult.PASS
        },
        AXE(BlockTags.AXE_MINEABLE) {
            override fun useOnBlock(context: ItemUsageContext): ActionResult = Items.WOODEN_AXE.useOnBlock(context)
        },
        HOE(BlockTags.HOE_MINEABLE) {
            override fun useOnBlock(context: ItemUsageContext): ActionResult = Items.WOODEN_HOE.useOnBlock(context)
        },
        PICKAXE((BlockTags.PICKAXE_MINEABLE)) {
            override fun useOnBlock(context: ItemUsageContext): ActionResult {
                val world: World = context.world
                val pos: BlockPos = context.blockPos
                if (!canPlaceTorch(context)) return ActionResult.PASS
                val side: Direction = context.side
                val pos1: BlockPos = pos.offset(side)
                if (world.canSetBlock(pos1)) {
                    if (Items.TORCH.useOnBlock(context).isAccepted) {
                        context.player?.let { player: PlayerEntity ->
                            context.stack.damage(1, player, LivingEntity.getSlotForHand(context.hand))
                        }
                    }
                }
                return ActionResult.success(world.isClient)
            }

            private fun canPlaceTorch(context: ItemUsageContext): Boolean {
                val player: PlayerEntity = context.player ?: return false
                return when {
                    player.shouldCancelInteraction() -> false
                    context.hand != Hand.MAIN_HAND -> false
                    else -> true
                }
            }
        },
        SHOVEL((BlockTags.SHOVEL_MINEABLE)) {
            override fun useOnBlock(context: ItemUsageContext): ActionResult = Items.WOODEN_SHOVEL.useOnBlock(context)
        },
        ;

        companion object {
            @JvmField
            val CODEC: Codec<Behavior> = codecOf(entries)

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Behavior> = packetCodecOf(entries)
        }

        abstract fun useOnBlock(context: ItemUsageContext): ActionResult

        val text: Text
            get() = Text.translatable(asItem().translationKey).formatted(Formatting.WHITE)
        val tooltipText: MutableText
            get() = Text.translatable(RagiumTranslationKeys.CRAFTER_HAMMER_MODULE, text).formatted(Formatting.GRAY)

        //    HTContent    //

        override val registry: Registry<Item> = Registries.ITEM
        override val key: RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id("${asString()}_module"))

        //    StringIdentifiable    //

        override fun asString(): String = name.lowercase()
    }
}
