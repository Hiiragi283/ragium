package hiiragi283.ragium.common.init

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.context.CommandContext
import hiiragi283.ragium.api.extension.energyNetwork
import hiiragi283.ragium.api.extension.networkMap
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.world.HTEnergyNetwork
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.block.BlockState
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.BlockPosArgumentType
import net.minecraft.command.argument.ItemStackArgument
import net.minecraft.command.argument.ItemStackArgumentType
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumCommands {
    @JvmStatic
    fun init() {
        CommandRegistrationCallback.EVENT.register(RagiumCommands::register)
    }

    @JvmStatic
    private fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        access: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment,
    ) {
        dispatcher.register(
            CommandManager
                .literal("ragium")
                .then(
                    CommandManager
                        .literal("floating_item")
                        .then(
                            CommandManager
                                .argument("item", ItemStackArgumentType.itemStack(access))
                                .executes(::floatItem),
                        ),
                ).then(
                    CommandManager
                        .literal("build_multiblock")
                        .requires { it.hasPermissionLevel(2) }
                        .then(
                            CommandManager
                                .argument("pos", BlockPosArgumentType.blockPos())
                                .executes { buildMultiblock(it, false) }
                                .then(
                                    CommandManager
                                        .argument("replace", BoolArgumentType.bool())
                                        .executes { buildMultiblock(it, BoolArgumentType.getBool(it, "replace")) },
                                ),
                        ),
                ).then(
                    CommandManager
                        .literal("energy_network")
                        .then(
                            CommandManager
                                .literal("show")
                                .executes(::showEnergy),
                        ).then(
                            CommandManager
                                .literal("set")
                                .then(
                                    CommandManager
                                        .argument("value", LongArgumentType.longArg(0))
                                        .executes(::setEnergy),
                                ),
                        ),
                ),
        )
    }

    //    Floating Item    //

    @JvmStatic
    private fun floatItem(context: CommandContext<ServerCommandSource>): Int {
        val itemArgument: ItemStackArgument = ItemStackArgumentType.getItemStackArgument(context, "item")
        val stack: ItemStack = itemArgument.createStack(1, false)
        context.source.player?.let { RagiumNetworks.sendFloatingItem(it, stack) }
        return Command.SINGLE_SUCCESS
    }

    //    Multiblock    //

    @JvmStatic
    private fun buildMultiblock(context: CommandContext<ServerCommandSource>, replace: Boolean): Int {
        val pos: BlockPos = BlockPosArgumentType.getBlockPos(context, "pos")
        val world: ServerWorld = context.source.world
        val holder: HTControllerHolder? = HTControllerHolder.LOOKUP.find(world, pos, null)
        if (holder != null) {
            runCatching {
                holder.buildMultiblock(replace)
            }.onSuccess {
                context.source.sendFeedback({ Text.literal("Built Multiblock at $pos!") }, true)
            }.onFailure {
                context.source.sendError(Text.literal("Failed to build multiblock!"))
            }
        } else {
            context.source.sendError(Text.literal("No multiblock controller exists at $pos!"))
        }
        return Command.SINGLE_SUCCESS
    }

    /*private class Constructor(
        private val world: World,
        private val pos: BlockPos,
        private val provider: HTMultiblockProvider,
        private val replace: Boolean = false,
    ) : HTMultiblockBuilder {
        private var isValid: Boolean = true

        override fun add(
            x: Int,
            y: Int,
            z: Int,
            pattern: HTMultiblockPattern,
        ) {
            val pos1: BlockPos = pos.add(x, y, z)
            if (isValid) {
                if (replace) {
                    pattern.getPlacementState(world, pos1, provider)?.let {
                        world.setBlockState(pos1, it)
                    }
                } else if (!world.isAir(pos1)) {
                    isValid = false
                } else {
                    pattern.getPlacementState(world, pos1, provider)?.let {
                        world.setBlockState(pos1, it)
                    }
                }
            } else {
                isValid = false
            }
        }
    }*/

    fun HTControllerHolder.buildMultiblock(replace: Boolean = false) {
        val controller: HTControllerDefinition = getController() ?: return
        val world: World = controller.world
        val absoluteMap: HTMultiblockMap.Absolute = getMultiblockMap()?.convertAbsolute(controller) ?: return
        if (absoluteMap.isEmpty()) return
        for ((pos: BlockPos, component: HTMultiblockComponent) in absoluteMap.entries) {
            val state: BlockState = component.getPlacementState(controller) ?: continue
            if (replace || world.isAir(pos)) {
                world.setBlockState(pos, state)
            }
        }
    }

    //    Energy Network    //

    @JvmStatic
    private fun showEnergy(context: CommandContext<ServerCommandSource>): Int {
        context.source.run {
            server.networkMap.forEach { (key: RegistryKey<World>, network: HTEnergyNetwork) ->
                sendFeedback({ Text.literal("${key.value} - ${network.amount} E") }, true)
            }
        }
        return Command.SINGLE_SUCCESS
    }

    @JvmStatic
    private fun setEnergy(context: CommandContext<ServerCommandSource>): Int {
        val value: Long = LongArgumentType.getLong(context, "value")
        context.source.run {
            world.energyNetwork.amount = value
            sendFeedback({ Text.literal("Set Energy to $value E") }, true)
        }
        return Command.SINGLE_SUCCESS
    }
}
