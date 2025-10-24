package hiiragi283.ragium.server

import com.google.common.primitives.Ints
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.context.CommandContext
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent

@EventBusSubscriber
object RagiumCommand {
    @SubscribeEvent
    fun register(event: RegisterCommandsEvent) {
        event.dispatcher.register(
            Commands
                .literal(RagiumAPI.MOD_ID)
                .requires { stack: CommandSourceStack -> stack.hasPermission(2) }
                .then(
                    Commands
                        .literal("energy_network")
                        .then(Commands.literal("get").executes(::getEnergy))
                        .then(
                            Commands
                                .literal("add")
                                .then(
                                    Commands
                                        .argument("value", LongArgumentType.longArg())
                                        .executes(::addEnergy),
                                ),
                        ).then(
                            Commands
                                .literal("set")
                                .then(
                                    Commands
                                        .argument("value", LongArgumentType.longArg())
                                        .executes { context: CommandContext<CommandSourceStack> ->
                                            setEnergy(context, LongArgumentType.getLong(context, "value"))
                                        },
                                ),
                        ).then(
                            Commands
                                .literal("clear")
                                .executes { context: CommandContext<CommandSourceStack> -> setEnergy(context, 0) },
                        ),
                ),
        )
    }

    @JvmStatic
    private fun getEnergy(context: CommandContext<CommandSourceStack>): Int {
        val source: CommandSourceStack = context.source
        val amount: Long = getEnergyNetwork(source)?.getAmountAsLong() ?: 0
        source.sendSuccess({ Component.literal("$amount FE in the energy network") }, true)
        return Ints.saturatedCast(amount)
    }

    @JvmStatic
    private fun addEnergy(context: CommandContext<CommandSourceStack>): Int {
        val source: CommandSourceStack = context.source
        val value: Long = LongArgumentType.getLong(context, "value")
        val received: Long = getEnergyNetwork(source)?.insertEnergy(value, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL) ?: 0
        source.sendSuccess({ Component.literal("Add $received FE into the energy network") }, true)
        return Ints.saturatedCast(received)
    }

    @JvmStatic
    private fun setEnergy(context: CommandContext<CommandSourceStack>, value: Long): Int {
        val source: CommandSourceStack = context.source
        (getEnergyNetwork(source) as? HTEnergyBattery.Mutable)?.setAmountAsLong(value)
        source.sendSuccess({ Component.literal("Set amount of the energy network to $value FE") }, true)
        return Ints.saturatedCast(value)
    }

    @JvmStatic
    private fun getEnergyNetwork(source: CommandSourceStack): HTEnergyBattery? = RagiumPlatform.INSTANCE.getEnergyNetwork(source.level)
}
