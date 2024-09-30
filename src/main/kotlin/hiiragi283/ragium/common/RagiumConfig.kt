package hiiragi283.ragium.common

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import hiiragi283.ragium.common.world.hardModeManager
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.world.World

object RagiumConfig {
    @JvmStatic
    var isHardMode: Boolean = false
        private set

    @JvmStatic
    fun init() {
        ServerWorldEvents.LOAD.register { server: MinecraftServer, world: ServerWorld ->
            if (world.registryKey == World.OVERWORLD) {
                isHardMode = server.hardModeManager.isHardMode
            }
        }
    }

    //    Commands    //

    @JvmField
    val ARGUMENT_BUILDER: LiteralArgumentBuilder<ServerCommandSource> =
        CommandManager
            .literal("hard_mode")
            .executes(::showCurrentMode)
            .then(
                CommandManager
                    .argument("flag", BoolArgumentType.bool())
                    .executes(::changeHardMode),
            )

    @JvmStatic
    private fun showCurrentMode(context: CommandContext<ServerCommandSource>): Int {
        context.source.sendMessage(Text.literal("Hard mode; $isHardMode"))
        return Command.SINGLE_SUCCESS
    }

    @JvmStatic
    private fun changeHardMode(context: CommandContext<ServerCommandSource>): Int {
        val bool: Boolean = BoolArgumentType.getBool(context, "flag")
        context.source.server.hardModeManager.isHardMode = bool
        isHardMode = bool
        context.source.sendFeedback({
            Text.literal(
                when (isHardMode) {
                    true -> "Enabled hard mode, please run /reload"
                    false -> "Disabled hard mode, please run /reload"
                },
            )
        }, true)
        return Command.SINGLE_SUCCESS
    }
}
