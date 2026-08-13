package hiiragi283.ragium.api

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.HTIdLike
import net.neoforged.neoforge.common.ModConfigSpec

data object RagiumConfig {
    @JvmField
    val COMMON_SPEC: ModConfigSpec

    @JvmField
    val SERVER_SPEC: ModConfigSpec

    @JvmField
    val COMMON: Common

    @JvmField
    val SERVER: Server

    init {
        val (common: Common, commonSpec: ModConfigSpec) = ModConfigSpec.Builder().configure(::Common)
        COMMON_SPEC = commonSpec
        COMMON = common
        val (server: Server, serverSpec: ModConfigSpec) = ModConfigSpec.Builder().configure(::Server)
        SERVER_SPEC = serverSpec
        SERVER = server
    }

    class Common(builder: ModConfigSpec.Builder)

    class Server(builder: ModConfigSpec.Builder) {
        @JvmField
        val modIdComparator: Comparator<HTIdLike> = Comparator
            .comparingInt { id: HTIdLike ->
                val modIds: List<String> = tagOutputPriority.get()
                when (val priority: Int = modIds.indexOf(id.namespace)) {
                    -1 -> modIds.size
                    else -> priority
                }
            }.thenBy(HTIdLike::namespace)

        @JvmField
        val tagOutputPriority: ModConfigSpec.ConfigValue<List<String>> =
            builder
                .worldRestart()
                .defineList(
                    "tagOutputModIds",
                    listOf(
                        RagiumAPI.MOD_ID,
                        HTConstants.MINECRAFT,
                        "alltheores",
                        "mekanism",
                    ),
                    { "" },
                    { obj: Any -> obj is String },
                )
    }
}
