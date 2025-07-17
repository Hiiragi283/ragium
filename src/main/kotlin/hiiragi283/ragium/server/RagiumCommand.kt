package hiiragi283.ragium.server

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.energy.IEnergyStorage
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
                                        .argument("value", IntegerArgumentType.integer())
                                        .executes(::addEnergy),
                                ),
                        ).then(
                            Commands
                                .literal("set")
                                .then(
                                    Commands
                                        .argument("value", IntegerArgumentType.integer())
                                        .executes { context: CommandContext<CommandSourceStack> ->
                                            setEnergy(context, IntegerArgumentType.getInteger(context, "value"))
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
        val network: IEnergyStorage = RagiumAPI
            .getInstance()
            .getEnergyNetworkManager()
            .getNetworkFromServer(source.level)
        val amount: Int = network.energyStored
        source.sendSuccess({ Component.literal("$amount FE in the energy network") }, true)
        return amount
    }

    @JvmStatic
    private fun addEnergy(context: CommandContext<CommandSourceStack>): Int {
        val source: CommandSourceStack = context.source
        val value: Int = IntegerArgumentType.getInteger(context, "value")

        val received: Int = RagiumAPI
            .getInstance()
            .getEnergyNetworkManager()
            .getNetworkFromServer(source.level)
            .receiveEnergy(value, false)
        source.sendSuccess({ Component.literal("Add $received FE into the energy network") }, true)
        return received
    }

    @JvmStatic
    private fun setEnergy(context: CommandContext<CommandSourceStack>, value: Int): Int {
        val source: CommandSourceStack = context.source

        val network: HTEnergyNetwork = RagiumAPI
            .getInstance()
            .getEnergyNetworkManager()
            .getNetworkFromServer(source.level)
            as? HTEnergyNetwork ?: return -1
        network.amount = value
        source.sendSuccess({ Component.literal("Set amount of the energy network to $value FE") }, true)
        return value
    }
}
