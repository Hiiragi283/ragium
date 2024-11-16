package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.blockState
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.extension.teleport
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LodestoneTrackerComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.GlobalPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.jvm.optionals.getOrNull

object HTWarpedCrystalItem : Item(itemSettings()) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack: ItemStack = user.getStackInHand(hand)
        user.itemCooldownManager.set(this, 20)
        val tracker: LodestoneTrackerComponent =
            stack.get(DataComponentTypes.LODESTONE_TRACKER) ?: return super.use(world, user, hand)
        val globalPos: GlobalPos = tracker.target.getOrNull() ?: return super.use(world, user, hand)
        if (!world.getBlockState(globalPos.pos).isOf(RagiumBlocks.TELEPORT_ANCHOR)) {
            stack.remove(DataComponentTypes.LODESTONE_TRACKER)
            return super.use(world, user, hand)
        }
        if (HTMachineTier.ADVANCED.consumerEnergy(world, null, 64)) {
            world.server?.getWorld(globalPos.dimension)?.let { worldTo: ServerWorld ->
                val vec3d = Vec3d(
                    globalPos.pos.x.toDouble() + 0.5,
                    globalPos.pos.y.toDouble() + 1.0,
                    globalPos.pos.z.toDouble() + 0.5,
                )
                teleport(user, worldTo, vec3d)
            }
            return TypedActionResult.success(stack, world.isClient())
        }
        return super.use(world, user, hand)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if (context.blockState.isOf(RagiumBlocks.TELEPORT_ANCHOR)) {
            context.stack.set(
                DataComponentTypes.LODESTONE_TRACKER,
                LodestoneTrackerComponent(Optional.of(GlobalPos(context.world.registryKey, context.blockPos)), false),
            )
            return ActionResult.success(context.world.isClient())
        }
        return super.useOnBlock(context)
    }

    override fun hasGlint(stack: ItemStack): Boolean = super.hasGlint(stack) || stack.contains(DataComponentTypes.LODESTONE_TRACKER)

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        stack.get(DataComponentTypes.LODESTONE_TRACKER)?.let { component: LodestoneTrackerComponent ->
            tooltip.add(Text.literal("Destination: ${component.target.getOrNull()}"))
        }
    }
}
