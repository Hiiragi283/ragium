package hiiragi283.ragium.common

import hiiragi283.ragium.common.energy.HTRagiPower
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTMachineType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Ragium : ModInitializer {

    const val MOD_ID = "ragium"
    const val MOD_NAME = "Ragium"

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

    @JvmField
    val logger: Logger = LoggerFactory.getLogger(MOD_NAME)

    @JvmStatic
    inline fun log(action: Logger.() -> Unit) {
        logger.action()
    }

    override fun onInitialize() {
        log { info("Registering game objects...") }

        RagiumComponentTypes
        RagiumBlockEntityTypes
        RagiumBlocks
        HTMachineType.init()
        RagiumItems

        RagiumItemGroup.init()

        HTMachineRecipe.Serializer
        RagiumGenerations

        UseBlockCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand, hitResult: BlockHitResult ->
            val stack: ItemStack = player.getStackInHand(hand)
            if (!stack.isEmpty) {
                if (stack.isOf(RagiumItems.POWER_METER)) {
                    if (!world.isClient) {
                        val pos: BlockPos = hitResult.blockPos
                        val state: BlockState = world.getBlockState(pos)
                        val blockEntity: BlockEntity? = world.getBlockEntity(pos)
                        val side: Direction = hitResult.side
                        val power: HTRagiPower? = HTRagiPower.SIDED_LOOKUP.find(world, pos, state, blockEntity, side)
                        player.sendMessage(
                            Text.literal("Current Power - $power"),
                            true
                        )
                    }
                    ActionResult.success(world.isClient)
                }
            }
            ActionResult.PASS
        }

        log { info("Ragium initialized!") }
    }
}