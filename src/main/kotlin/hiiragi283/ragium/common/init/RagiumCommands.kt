package hiiragi283.ragium.common.init

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.context.CommandContext
import hiiragi283.ragium.api.extension.energyNetwork
import hiiragi283.ragium.api.extension.getMultiblockController
import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.extension.networkMap
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockConstructor
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
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
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.collections.component1
import kotlin.collections.component2

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
                /*.then(
                    CommandManager
                        .literal("drive_manager")
                        .then(
                            CommandManager
                                .literal("add")
                                .then(
                                    CommandManager
                                        .argument("id", IdentifierArgumentType.identifier())
                                        .suggests(SuggestionProviders.ALL_RECIPES)
                                        .executes(::addDriveRecipe),
                                ),
                        ).then(
                            CommandManager
                                .literal("remove")
                                .then(
                                    CommandManager
                                        .argument("id", IdentifierArgumentType.identifier())
                                        .suggests(SuggestionProviders.ALL_RECIPES)
                                        .executes(::removeDriveRecipe),
                                ),
                        ),
                )*/
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

    //    Data Drive    //

    /*@JvmStatic
    private fun addDriveRecipe(context: CommandContext<ServerCommandSource>): Int {
        val id: Identifier = IdentifierArgumentType.getIdentifier(context, "id")
        val recipeEntry: RecipeEntry<*>? = context.source.world.recipeManager
            .get(id)
            ?.getOrNull()
        if (recipeEntry != null) {
            val recipe: Recipe<*> = recipeEntry.value
            if (recipe is HTRequireScanRecipe && recipe.requireScan) {
                val manager: HTDataDriveManager = context.source.server.dataDriveManager
                if (id !in manager) {
                    manager.add(id)
                    context.source.sendFeedback({ Text.literal("Unlocked the recipe; $id!") }, true)
                } else {
                    context.source.sendError(Text.literal("The recipe; $id is already unlocked!"))
                }
            } else {
                context.source.sendError(Text.literal("The recipe; $id does not require scanning!"))
            }
        } else {
            context.source.sendError(Text.literal("Could not find recipe; $id"))
        }
        return Command.SINGLE_SUCCESS
    }

    @JvmStatic
    private fun removeDriveRecipe(context: CommandContext<ServerCommandSource>): Int {
        val id: Identifier = IdentifierArgumentType.getIdentifier(context, "id")
        val recipeEntry: RecipeEntry<*>? = context.source.world.recipeManager
            .get(id)
            ?.getOrNull()
        if (recipeEntry != null) {
            val recipe: Recipe<*> = recipeEntry.value
            if (recipe is HTRequireScanRecipe && recipe.requireScan) {
                val manager: HTDataDriveManager = context.source.server.dataDriveManager
                if (id in manager) {
                    manager.remove(id)
                    context.source.sendFeedback({ Text.literal("Locked the recipe; $id!") }, true)
                } else {
                    context.source.sendError(Text.literal("The recipe; $id is already locked!"))
                }
            } else {
                context.source.sendError(Text.literal("The recipe; $id does not require scanning!"))
            }
        } else {
            context.source.sendError(Text.literal("Could not find recipe; $id"))
        }
        return Command.SINGLE_SUCCESS
    }*/

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
        val state: BlockState = world.getBlockState(pos)
        val controller: HTMultiblockController? = world.getMultiblockController(pos)
        if (controller != null) {
            val result: Boolean = if (!controller.isValid(state, world, pos)) {
                val facing: Direction =
                    state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
                controller.buildMultiblock(HTMultiblockConstructor(world, pos, replace).rotate(facing))
                true
            } else {
                false
            }
            if (result) {
                context.source.sendFeedback({ Text.literal("Built Multiblock at $pos!") }, true)
            } else {
                context.source.sendError(Text.literal("Failed to build multiblock!"))
            }
        } else {
            context.source.sendError(Text.literal("No multiblock controller exists at $pos!"))
        }
        return Command.SINGLE_SUCCESS
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
