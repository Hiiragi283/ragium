package hiiragi283.ragium.api.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.extension.packetCodecOf
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.item.tooltip.TooltipAppender
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.Consumer

data class HTModularToolComponent(val tier: HTMachineTier, private val behavior: Behavior) : TooltipAppender {
    companion object {
        @JvmField
        val DEFAULT = HTModularToolComponent(HTMachineTier.PRIMITIVE, Behavior.DEFAULT)

        @JvmField
        val CODEC: Codec<HTModularToolComponent> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTMachineTier.CODEC
                        .optionalFieldOf("tier", HTMachineTier.PRIMITIVE)
                        .forGetter(HTModularToolComponent::tier),
                    Behavior.CODEC.fieldOf("behavior").forGetter(HTModularToolComponent::behavior),
                ).apply(instance, ::HTModularToolComponent)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTModularToolComponent> = PacketCodec.tuple(
            HTMachineTier.PACKET_CODEC,
            HTModularToolComponent::tier,
            Behavior.PACKET_CODEC,
            HTModularToolComponent::behavior,
            ::HTModularToolComponent,
        )

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTModularToolComponent> = ComponentType
            .builder<HTModularToolComponent>()
            .codec(CODEC)
            .packetCodec(PACKET_CODEC)
            .build()
    }

    fun isSuitableFor(state: BlockState): Boolean = state.isIn(behavior.mineableTag)

    fun useOnBlock(context: ItemUsageContext): ActionResult {
        val stack: ItemStack = context.stack
        return when {
            stack.damage >= stack.maxDamage - 1 -> ActionResult.PASS
            else -> behavior.useOnBlock(context)
        }
    }

    //    TooltipAppender    //

    override fun appendTooltip(context: Item.TooltipContext, tooltip: Consumer<Text>, type: TooltipType) {
        tooltip.accept(tier.tierText)
        tooltip.accept(Text.literal("Behavior; $behavior"))
    }

    //    Behavior    //

    enum class Behavior(val mineableTag: TagKey<Block>) :
        ItemConvertible,
        StringIdentifiable {
        DEFAULT(BlockTags.AIR) {
            override fun useOnBlock(context: ItemUsageContext): ActionResult = ActionResult.PASS
        },
        AXE_STRIPPING(BlockTags.AXE_MINEABLE) {
            override fun useOnBlock(context: ItemUsageContext): ActionResult = Items.WOODEN_AXE.useOnBlock(context)
        },
        HOE_TILLING(BlockTags.HOE_MINEABLE) {
            override fun useOnBlock(context: ItemUsageContext): ActionResult = Items.WOODEN_HOE.useOnBlock(context)
        },
        PICKAXE_LIGHTING((BlockTags.PICKAXE_MINEABLE)) {
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
        SHOVEL_FLATTENING((BlockTags.SHOVEL_MINEABLE)) {
            override fun useOnBlock(context: ItemUsageContext): ActionResult = Items.WOODEN_SHOVEL.useOnBlock(context)
        },
        ;

        companion object {
            @JvmField
            val CODEC: Codec<Behavior> = codecOf(::fromString)

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Behavior> = packetCodecOf(::fromString)

            @JvmStatic
            fun fromString(name: String): Behavior = entries.first { it.asString() == name }
        }

        abstract fun useOnBlock(context: ItemUsageContext): ActionResult

        //    ItemConvertible    //

        private val item = Item(itemSettings())

        override fun asItem(): Item = item

        //    StringIdentifiable    //

        override fun asString(): String = name.lowercase()
    }
}
