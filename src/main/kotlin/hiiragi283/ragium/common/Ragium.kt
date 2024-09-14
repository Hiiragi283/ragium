package hiiragi283.ragium.common

import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.item.HTPortableScreenType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.resource.HTHardModeResourceCondition
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
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

    @JvmStatic
    var config: RagiumConfig = RagiumConfig()
        private set

    override fun onInitialize() {
        log { info("Registering game objects...") }

        AutoConfig.register(RagiumConfig::class.java, ::GsonConfigSerializer)
        config = AutoConfig.getConfigHolder(RagiumConfig::class.java).get()

        RagiumComponentTypes
        RagiumBlockEntityTypes
        RagiumBlocks
        HTMachineType.init()
        RagiumItems
        RagiumFluids.init()

        HTPortableScreenType.init()
        HTMachineRecipe.Serializer
        RagiumGenerations

        RagiumMetalItemFamilies

        RagiumItemGroup.init()
        RagiumCauldronBehaviors.init()
        HTHardModeResourceCondition.init()

        /*UseBlockCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand, hitResult: BlockHitResult ->
            val stack: ItemStack = player.getStackInHand(hand)
            if (!stack.isEmpty) {
                if (stack.isOf(RagiumItems.POWER_METER)) {
                    if (!world.isClient) {
                        val pos: BlockPos = hitResult.blockPos
                        val state: BlockState = world.getBlockState(pos)
                        val blockEntity: BlockEntity? = world.getBlockEntity(pos)
                        val side: Direction = hitResult.side
                        val tier: HTMachineTier? = HTMachineTier.SIDED_LOOKUP.find(world, pos, state, blockEntity, side)
                        player.sendMessage(
                            Text.literal("Current Tier - $tier"),
                            true
                        )
                    }
                    ActionResult.success(world.isClient)
                }
            }
            ActionResult.PASS
        }*/

        log { info("Ragium initialized!") }
    }
}