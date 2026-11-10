package hiiragi283.ragium.gametest

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.Registries
import net.minecraft.gametest.framework.GameTestHelper
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.RegisterEvent
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
data object RagiumGameTests {
    @SubscribeEvent
    fun register(event: RegisterEvent) {
        event.register(Registries.TEST_FUNCTION) { registry ->
            registry.register(RagiumAPI.id("assembler_accepts_energy"), ::assemblerAcceptsEnergy)
        }
    }

    private fun assemblerAcceptsEnergy(helper: GameTestHelper) {
        val pos: BlockPos = BlockPos.ZERO
        helper.setBlock(pos, RagiumBlocks.ASSEMBLER.getOrThrow())
        val side: Direction = helper.getAbsoluteDirection(Direction.WEST)
        val energy: EnergyHandler = HTEnergyCapabilities.getCapability(helper.level, helper.absolutePos(pos), side)
            ?: throw helper.assertionException("The assembler has no energy capability on the test side")

        val initialAmount: Long = energy.amountAsLong
        val transferred: Int = EnergyHandlerUtil.move(InfiniteEnergyHandler.INSTANCE, energy, 1, null)
        helper.assertTrue(transferred == 1, "The assembler did not accept one unit of energy")
        helper.assertTrue(energy.amountAsLong == initialAmount + 1, "The assembler did not store the transferred energy")
        helper.succeed()
    }
}
