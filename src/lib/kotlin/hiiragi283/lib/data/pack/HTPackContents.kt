package hiiragi283.lib.data.pack

import hiiragi283.lib.resource.toId
import hiiragi283.ragium.api.RagiumAPI
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.collections.iterator
import kotlin.io.path.writeBytes
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.resources.IoSupplier

/**
 * 参照 : [GregTech Modern - GTDynamicPackContents](https://github.com/GregTechCEu/GregTech-Modern/blob/1.21/src/main/java/com/gregtechceu/gtceu/data/pack/GTDynamicPackContents.java)
 *
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
internal class HTPackContents {
    companion object {
        @JvmStatic
        fun dumpData(id: Identifier, parent: Path, bytes: ByteArray) {
            runCatching {
                val file: Path = parent.resolve(id.namespace).resolve(id.path)
                Files.createDirectories(file.parent)
                file.writeBytes(bytes)
            }.onFailure { RagiumAPI.LOGGER.error("Failed to dump json for file {}", id, it) }
        }
    }

    internal class Node {
        // GT Modernの実装では`tags/block/ore.json`と`tags/block/ore/xx.json`が共存できないので，それを解決するためにこうした
        val children: MutableMap<String, Node> = hashMapOf()
        internal var contents: IoSupplier<InputStream>? = null
        val terminal: IoSupplier<InputStream> get() = contents ?: error("node has no data")

        fun getChild(name: String): Node? = children[name]

        fun collectResources(namespace: String, pathComponents: List<String>, curIndex: Int, output: PackResources.ResourceOutput) {
            if (curIndex < pathComponents.size) {
                val component: String = pathComponents[curIndex]
                getChild(component)?.collectResources(namespace, pathComponents, curIndex + 1, output)
            } else {
                outputResources(namespace, pathComponents.joinToString(separator = "/"), output)
            }
        }

        fun outputResources(namespace: String, path: String, output: PackResources.ResourceOutput) {
            for ((pathIn: String, node: Node) in children) {
                node.outputResources(namespace, "$path/$pathIn", output)
            }
            contents?.let { output.accept(namespace.toId(path), it) }
        }
    }

    private val root = Node()
    private val lock = ReentrantReadWriteLock()

    fun addToData(id: Identifier, bytes: ByteArray) {
        addToData(id, bytes::inputStream)
    }

    fun addToData(id: Identifier, supplier: IoSupplier<InputStream>) {
        val pathComponents: List<String> = id.path.split("/")
        val lock: ReentrantReadWriteLock.WriteLock = this.lock.writeLock()
        lock.lock()
        try {
            var node: Node = root.children.getOrPut(id.namespace, ::Node)
            for (component: String in pathComponents) {
                node = node.children.getOrPut(component, ::Node)
            }
            node.contents = supplier
        } finally {
            lock.unlock()
        }
    }

    fun clearData() {
        val lock: ReentrantReadWriteLock.WriteLock = this.lock.writeLock()
        lock.lock()
        try {
            root.children.clear()
            root.contents = null
        } finally {
            lock.unlock()
        }
    }

    fun getResource(location: Identifier): IoSupplier<InputStream>? {
        val lock: ReentrantReadWriteLock.ReadLock = this.lock.readLock()
        lock.lock()
        try {
            var node: Node? = root.getChild(location.namespace)
            val pathComponents: List<String> = location.path.split("/")
            for (component: String in pathComponents) {
                if (node == null) return null
                node = node.getChild(component)
            }
            return node?.terminal
        } finally {
            lock.unlock()
        }
    }

    fun listResources(namespace: String, path: String, output: PackResources.ResourceOutput) {
        val lock: ReentrantReadWriteLock.ReadLock = this.lock.readLock()
        lock.lock()
        try {
            root.getChild(namespace)?.collectResources(namespace, path.split("/"), 0, output)
        } finally {
            lock.unlock()
        }
    }
}
