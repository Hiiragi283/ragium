package hiiragi283.ragium.test

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest
import net.minecraft.test.GameTest
import net.minecraft.test.GameTestException
import net.minecraft.test.TestContext

class RagiumGameTest : FabricGameTest {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    fun testOfTest(context: TestContext) {
        try {
            RagiumAPI.getInstance().machineRegistry
        } catch (e: Exception) {
            throw GameTestException("Game test failed!")
        }

        context.complete()
    }
}
